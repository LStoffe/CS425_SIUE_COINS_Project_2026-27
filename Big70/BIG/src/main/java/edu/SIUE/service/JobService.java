package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * JobService.java
 *
 * Service component responsible for job-related logic in the SIUE BIG simulation platform.
 *
 * Responsibilities:
 *   - Generates new jobs for game sessions based on configured parameters.
 *   - Retrieves job information for given games and periods.
 *   - Leverages job type definitions and properties consistent with the legacy Big60 JobFactory design,
 *     including type names, probabilities, and size parameters, but omits the complete activity simulation present in Big60.
 *
 * Usage:
 *   - Invoked by GameService and other service layers to seed or manage job data when a new game is created,
 *     or when a new period starts within a game session.
 *   - Integrates with Spring's JdbcTemplate for direct database access.
 *   - Fully managed by Spring's dependency injection.
 */

@Service
public class JobService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Job type names matching Big60
    private static final Map<Integer, String> JOB_TYPE_NAMES = Map.of(
        1, "Apartment Building",
        2, "School Building",
        3, "Office Building",
        4, "Hospital",
        5, "Industrial Building",
        6, "Heavy - Highway",
        7, "Heavy - Bridge",
        8, "Heavy - Site Development",
        9, "Heavy - Mass Excavation",
        10, "Heavy - Underground Utilities"
    );

    /**
     * Generates jobs for a game at the start of a period.
     * Uses game parameters to determine number and size of jobs.
     *
     * @param gameId   The game to generate jobs for
     * @param adminId  The admin who owns the game
     * @param periodId The current period number
     */
    public void generateJobsForPeriod(int gameId, int adminId, int periodId) {
        try {
            // Get jobs per period parameters from default admin params (gameid=0, adminid=0)
            String paramSql = """
                SELECT number_jobs_mean, number_jobs_stddev, number_jobs_min, number_jobs_max
                FROM public.game_params_jobs_per_period
                WHERE gameid = 0 AND adminid = 0 AND periodid = ?
                LIMIT 1
                """;

            List<Map<String, Object>> params = jdbcTemplate.queryForList(
                paramSql, Math.min(periodId, 12));

            if (params.isEmpty()) {
                System.err.println("generateJobsForPeriod: No job params found for period " + periodId);
                return;
            }

            java.sql.Date startYear = jdbcTemplate.queryForObject(
                "SELECT start_year FROM public.game WHERE gameid = ?",
                java.sql.Date.class, gameId);

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startYear);
            int gameStartYear = startCal.get(Calendar.YEAR);

            // To this:
            Integer gametypeObj = jdbcTemplate.queryForObject(
                "SELECT gametype FROM public.game WHERE gameid = ?",
                Integer.class, gameId);

            int gametype = gametypeObj != null ? gametypeObj : 1;

            // Calculate number of jobs to generate using normal distribution
            Map<String, Object> jobParams = params.get(0);
            int mean   = ((Number) jobParams.get("number_jobs_mean")).intValue();
            int stddev = ((Number) jobParams.get("number_jobs_stddev")).intValue();
            int min    = ((Number) jobParams.get("number_jobs_min")).intValue();
            int max    = ((Number) jobParams.get("number_jobs_max")).intValue();

            Random r = new Random();
            int numJobs = (int) Math.round((r.nextGaussian() * stddev) + mean);
            numJobs = Math.max(min, Math.min(max, numJobs));

            // Check current job count — enforce 40 job limit
            int currentJobCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.job WHERE gameid = ? AND companyid IS NULL AND is_active = true",
                Integer.class, gameId);

            int slotsAvailable = 40 - currentJobCount;
            if (slotsAvailable <= 0) {
                return;
            }

            // Cap numJobs to available slots
            numJobs = Math.min(numJobs, slotsAvailable);

            // Get next job ID
            int nextJobId = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(jobid), 0) + 1 FROM public.job", Integer.class);

            

            // Generate each job
            for (int i = 0; i < numJobs; i++) {
                generateSingleJob(gameId, adminId, periodId, nextJobId + i, r, gameStartYear, gametype);
            }

        } catch (Exception e) {
            System.err.println("generateJobsForPeriod error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates a single job and inserts it into the database.
     */
    private void generateSingleJob(int gameId, int adminId, int periodId, int jobId, Random r, int gameStartYear, int gametype) {
        try {
            int jobType = getRandomJobType(r, gametype);

            // Get size parameters from default admin params (gameid=0, adminid=0)
            String sizeSql = """
                SELECT job_size_mean, job_size_stddev, job_size_min, job_size_max
                FROM public.game_params_job_size
                WHERE gameid = 0 AND adminid = 0 AND job_type_id = ?
                LIMIT 1
                """;

            List<Map<String, Object>> sizeParams = jdbcTemplate.queryForList(sizeSql, jobType);
            if (sizeParams.isEmpty()) {
                System.err.println("generateSingleJob: No size params for job type " + jobType);
                return;
            }

            Map<String, Object> sp = sizeParams.get(0);
            int sizeMean   = ((Number) sp.get("job_size_mean")).intValue();
            int sizeStddev = ((Number) sp.get("job_size_stddev")).intValue();
            int sizeMin    = ((Number) sp.get("job_size_min")).intValue();
            int sizeMax    = ((Number) sp.get("job_size_max")).intValue();

            // Calculate job size using normal distribution
            int jobSize = (int) Math.round((r.nextGaussian() * sizeStddev) + sizeMean);
            jobSize = Math.max(sizeMin, Math.min(sizeMax, jobSize));

            // Calculate direct cost — roughly $50-100 per LU
            long directCost = Math.round(jobSize * (50 + r.nextInt(50)));

            // Calculate liquidated damages
            int liquidatedDamages = calculateLiquidatedDamages(directCost, r);

            // Calculate completion deadline — 3-9 periods from now (each period = 2 months)
            int periodsToComplete = 3 + r.nextInt(7);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, gameStartYear);
            cal.set(Calendar.MONTH, 0); // January
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.add(Calendar.MONTH, periodId * 2);
            cal.add(Calendar.MONTH, periodsToComplete * 2);
            java.sql.Date completionDeadline = new java.sql.Date(cal.getTimeInMillis());

            int estimatedWorkdays = periodsToComplete * 40;

            // Insert job into database
            String insertSql = """
                INSERT INTO public.job (
                    jobid, gameid, companyid, retentionid, job_type_id,
                    liquidation_damages, completion_deadline, job_lu_size,
                    lus_remaining, is_active, is_auto, direct_cost,
                    period_created, period_completed, estimated_workdays,
                    retained_amount, method_scaling_factor, change_order_lus,
                    change_order_pay, amount_retained_from_subs, billed_to_date,
                    type_modifier, customerid
                ) VALUES (
                    ?, ?, NULL, 1, ?,
                    ?, ?, ?,
                    ?, true, false, ?,
                    ?, 0, ?,
                    0, 1.0, 0,
                    0, 0, 0,
                    0, 0
                )
                """;

            jdbcTemplate.update(insertSql,
                jobId, gameId, jobType,
                liquidatedDamages, completionDeadline, jobSize,
                jobSize, directCost,
                periodId, estimatedWorkdays
            );

            

        } catch (Exception e) {
            System.err.println("generateSingleJob error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns a random job type using same probabilities as Big60 JobFactory.
     * 1=Apartments(50%), 2=School(15%), 3=Office(15%), 4=Hospital(10%), 5=Industrial(10%)
     */
    private int getRandomJobType(Random r, int gametype) {
        if (gametype == 2) {
            // Heavy only — types 6-10
            int spin = r.nextInt(100);
            if (spin < 30) return 6;       // Highway 30%
            else if (spin < 55) return 7;  // Bridge 25%
            else if (spin < 75) return 8;  // Site Development 20%
            else if (spin < 90) return 9;  // Mass Excavation 15%
            else return 10;                // Underground Utilities 10%
        } else if (gametype == 3) {
            // Both — mix of all types
            int spin = r.nextInt(100);
            if (spin < 25) return 1;       // Apartments 25%
            else if (spin < 40) return 2;  // School 15%
            else if (spin < 55) return 3;  // Office 15%
            else if (spin < 65) return 4;  // Hospital 10%
            else if (spin < 75) return 5;  // Industrial 10%
            else if (spin < 82) return 6;  // Highway 7%
            else if (spin < 88) return 7;  // Bridge 6%
            else if (spin < 93) return 8;  // Site Development 5%
            else if (spin < 97) return 9;  // Mass Excavation 4%
            else return 10;                // Underground Utilities 3%
        } else {
            // Building only (gametype = 1) — types 1-5, same as before
            int spin = r.nextInt(100);
            if (spin < 50) return 1;
            else if (spin < 65) return 2;
            else if (spin < 80) return 3;
            else if (spin < 90) return 4;
            else return 5;
        }
    }

    /**
     * Calculates liquidated damages based on game parameters.
     */
    private int calculateLiquidatedDamages(long directCost, Random r) {
        try {
            String sql = """
                SELECT mean_dc_percent, stddev_dc_percent, min_dc_percent, 
                    max_dc_percent, percent_jobs
                FROM public.game_params_liquidated_damages
                WHERE gameid = 0 AND adminid = 0
                LIMIT 1
                """;

            List<Map<String, Object>> params = jdbcTemplate.queryForList(sql);
            if (params.isEmpty()) return 1000;

            Map<String, Object> p = params.get(0);
            double mean       = ((Number) p.get("mean_dc_percent")).doubleValue();
            double stddev     = ((Number) p.get("stddev_dc_percent")).doubleValue();
            double min        = ((Number) p.get("min_dc_percent")).doubleValue();
            double max        = ((Number) p.get("max_dc_percent")).doubleValue();
            int percentJobs   = ((Number) p.get("percent_jobs")).intValue();

            if (r.nextInt(100) >= percentJobs) return 0;

            double damagePercent = (r.nextGaussian() * stddev) + mean;
            damagePercent = Math.max(min, Math.min(max, damagePercent));

            int damages = (int) Math.round((damagePercent / 100.0) * directCost);

            // Round to 1 significant digit like Big60
            if (damages < 100)    return 0;
            else if (damages < 1000)   return ((int) Math.round(damages / 100.0)) * 100;
            else if (damages < 10000)  return ((int) Math.round(damages / 1000.0)) * 1000;
            else if (damages < 100000) return ((int) Math.round(damages / 10000.0)) * 10000;
            else return 100000;

        } catch (Exception e) {
            System.err.println("calculateLiquidatedDamages error: " + e.getMessage());
            return 1000;
        }
    }

    /**
     * Gets all available jobs for a game (not yet awarded to a company).
     */
    public List<Map<String, Object>> getAvailableJobs(int gameId) {
        try {
            // Get the game's current date based on start year and current period
            Map<String, Object> game = jdbcTemplate.queryForMap(
                "SELECT start_year, current_period FROM public.game WHERE gameid = ?", gameId);

            java.sql.Date startYear = (java.sql.Date) game.get("start_year");
            int currentPeriod = ((Number) game.get("current_period")).intValue();

            Calendar cal = Calendar.getInstance();
            cal.setTime(startYear);
            cal.add(Calendar.MONTH, currentPeriod * 2);
            java.sql.Date gameCurrentDate = new java.sql.Date(cal.getTimeInMillis());


            int rowsUpdated = jdbcTemplate.update("""
                UPDATE public.job 
                SET is_active = false 
                WHERE gameid = ? 
                AND companyid IS NULL 
                AND completion_deadline < ?
                AND jobid NOT IN (
                    SELECT DISTINCT jobid FROM public.bid WHERE gameid = ?
                )
                """, gameId, gameCurrentDate, gameId);


            // Fetch available jobs
            String sql = """
                SELECT jobid, job_type_id, job_lu_size, liquidation_damages,
                    completion_deadline, direct_cost, period_created
                FROM public.job
                WHERE gameid = ? AND companyid IS NULL AND is_active = true
                ORDER BY jobid ASC
                """;
            return jdbcTemplate.queryForList(sql, gameId);

        } catch (Exception e) {
            System.err.println("getAvailableJobs error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Returns the display name for a job type ID.
     */
    public String getJobTypeName(int jobTypeId) {
        return JOB_TYPE_NAMES.getOrDefault(jobTypeId, "Unknown Type");
    }

    public void generateInitialJobs(int gameId, int adminId) {
        try {
            // Fetch game start year and type
            Map<String, Object> game = jdbcTemplate.queryForMap(
                "SELECT start_year, gametype FROM public.game WHERE gameid = ?", gameId);

            java.sql.Date startYear = (java.sql.Date) game.get("start_year");
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startYear);
            int gameStartYear = startCal.get(Calendar.YEAR);
            int gametype = game.get("gametype") != null ? ((Number) game.get("gametype")).intValue() : 1;

            Random r = new Random();
            int nextJobId = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(jobid), 0) + 1 FROM public.job", Integer.class);

            for (int i = 0; i < 20; i++) {
                generateSingleJob(gameId, adminId, 0, nextJobId + i, r, gameStartYear, gametype);
            }
        } catch (Exception e) {
            System.err.println("generateInitialJobs error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}