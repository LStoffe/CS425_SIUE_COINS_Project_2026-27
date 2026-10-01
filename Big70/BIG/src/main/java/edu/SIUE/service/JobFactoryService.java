package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/*
 * JobFactoryService.java
 *
 * This is a secondary job service file, created because the original JobService.java
 * became too long and unwieldy. Use this class for additional job-related logic, 
 * helper methods, or experimental job management features that don't fit cleanly
 * in the primary job service.
 *
 * All job creation, processing, or utility code that would overcomplicate JobService
 * should live here.
 */

@Service
public class JobFactoryService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of active jobs for a given company in a specific game.
     *
     * @param gameId    The ID of the game session.
     * @param companyId The ID of the company for which to retrieve active jobs.
     * @return List of maps, where each map contains job details (jobid, job_type_id, job_lu_size, lus_remaining, completion_deadline, period_created, direct_cost).
     */
    public List<Map<String, Object>> getCompanyActiveJobs(int gameId, int companyId) {
        try {
           String sql = """
                SELECT j.jobid, j.job_type_id, j.job_lu_size, j.lus_remaining,
                    j.completion_deadline, j.period_created, j.direct_cost,
                    j.estimated_workdays, j.period_awarded, j.current_method,
                    j.method_changed_period
                FROM public.job j
                WHERE j.gameid = ? AND j.companyid = ? AND j.is_active = true
                ORDER BY j.jobid ASC
                """;
            return jdbcTemplate.queryForList(sql, gameId, companyId);
        } catch (Exception e) {
            System.err.println("getCompanyActiveJobs error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }





    // --- Job Production Method Constants ---
    /** Standard method code (no speed or cost change). */
    public static final int METHOD_STANDARD = 1;
    /** Accelerated method code (faster, higher cost). */
    public static final int METHOD_ACCELERATED = 2;
    /** Overtime method code (much faster, much higher cost). */
    public static final int METHOD_OVERTIME = 3;
    /** Economy method code (slower, reduced cost). */
    public static final int METHOD_ECONOMY = 4;

    /** Human-readable names for each method, keyed by method constant. */
    public static final Map<Integer, String> METHOD_NAMES = Map.of(
        METHOD_STANDARD, "Standard",
        METHOD_ACCELERATED, "Accelerated",
        METHOD_OVERTIME, "Overtime",
        METHOD_ECONOMY, "Economy"
    );

    /** Descriptions for each production method's effects and cost implications. */
    public static final Map<Integer, String> METHOD_DESCRIPTIONS = Map.of(
        METHOD_STANDARD, "Normal pace — no additional cost",
        METHOD_ACCELERATED, "25% faster — costs 10% of bid amount per period",
        METHOD_OVERTIME, "50% faster — costs 20% of bid amount per period",
        METHOD_ECONOMY, "25% slower — saves 5% of bid amount per period"
    );

    /**
     * Attempts to change the method for a given job.
     * Enforces a limit of one change per period for each job.
     * Returns an error message if validation fails, or null if successful.
     *
     * @param gameId         The game session ID.
     * @param companyId      The company ID.
     * @param jobId          The job ID to change the method for.
     * @param newMethod      The method code to change to (see constants above).
     * @param currentPeriod  The current game period.
     * @return Error message if change is not allowed; null if successful.
     */
    public String changeJobMethod(int gameId, int companyId, int jobId, int newMethod, int currentPeriod) {
        try {
            // Retrieve the current method and the last period in which a change was made
            Map<String, Object> job = jdbcTemplate.queryForMap(
                "SELECT current_method, method_changed_period FROM public.job WHERE jobid = ? AND gameid = ? AND companyid = ?",
                jobId, gameId, companyId);

            int methodChangedPeriod = ((Number) job.get("method_changed_period")).intValue();

            // Only allow one change per period
            if (methodChangedPeriod == currentPeriod) {
                return "You can only change the method once per period.";
            }

            // Perform the method change and update the change timestamp
            jdbcTemplate.update(
                "UPDATE public.job SET current_method = ?, method_changed_period = ? WHERE jobid = ? AND gameid = ? AND companyid = ?",
                newMethod, currentPeriod, jobId, gameId, companyId);

            // Success
            return null;

        } catch (Exception e) {
            System.err.println("changeJobMethod error: " + e.getMessage());
            return "An error occurred while changing the method.";
        }
    }





    /**
     * Calculates the extra cost (or savings) for a job in one period due to its current method.
     * Used during period income processing to apply additional charges or savings based on the
     * production method.
     *
     * @param currentMethod  The current method code (see constants above).
     * @param bidAmount      The total bid amount for the job.
     * @return The extra cost (positive for extra charge, negative for savings, 0 for standard).
     */
    public double getMethodExtraCost(int currentMethod, double bidAmount) {
        return switch (currentMethod) {
            case METHOD_ACCELERATED -> bidAmount * 0.10;
            case METHOD_OVERTIME   -> bidAmount * 0.20;
            case METHOD_ECONOMY    -> -(bidAmount * 0.05); // negative means savings
            default                -> 0.0;
        };
    }





    /**
     * Retrieves the speed modifier for a job based on its current production method.
     * Used to compute the effective number of periods needed to complete a job.
     * - Values < 1 make the job faster (fewer periods).
     * - Values > 1 make the job slower (more periods).
     *
     * @param currentMethod The method code (see constants above).
     * @return A speed multiplier (e.g., 0.75 for 25% faster).
     */
    public double getMethodSpeedModifier(int currentMethod) {
        return switch (currentMethod) {
            case METHOD_ACCELERATED -> 0.75; // 25% faster
            case METHOD_OVERTIME   -> 0.50;  // 50% faster
            case METHOD_ECONOMY    -> 1.25;  // 25% slower
            default                -> 1.0;   // standard
        };
    }





    /**
     * Processes income for all active jobs in the specified game for the current period.
     * Distributes scheduled income for each active job, applies any method-related cost
     * adjustments, and checks whether jobs have been completed (based on effective periods).
     * If a job is completed, flags it as inactive and sets its completion period.
     *
     * This method assumes all relevant period state and job/method data is accurate in DB.
     *
     * @param gameId The game session ID.
     */
    public void processJobIncome(int gameId) {
        try {
            // Prepare SQL for retrieving currently active jobs and their bid amounts
            String sql = """
                SELECT j.jobid, j.companyid, j.estimated_workdays, j.period_awarded,
                    j.current_method, b.bid_amount
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid 
                    AND j.companyid = b.companyid 
                    AND j.gameid = b.gameid
                    AND b.outcome_code = 1
                WHERE j.gameid = ? AND j.companyid IS NOT NULL AND j.is_active = true
                """;

            // List of all currently active jobs for the specified game
            List<Map<String, Object>> activeJobs = jdbcTemplate.queryForList(sql, gameId);

            // Get the current period in this game
            int currentPeriod = jdbcTemplate.queryForObject(
                "SELECT current_period FROM public.game WHERE gameid = ?",
                Integer.class, gameId);

            // Process each job
            for (Map<String, Object> job : activeJobs) {
                int jobId = ((Number) job.get("jobid")).intValue();
                int companyId = ((Number) job.get("companyid")).intValue();
                int estimatedWorkdays = ((Number) job.get("estimated_workdays")).intValue();
                int periodAwarded = ((Number) job.get("period_awarded")).intValue();
                int currentMethod = ((Number) job.get("current_method")).intValue();
                double bidAmount = ((Number) job.get("bid_amount")).doubleValue();

                // Calculate the number of (base) periods required at standard method
                int basePeriodsToComplete = Math.max(1, estimatedWorkdays / 40);
                // Adjust for selected method's speed effect
                double speedModifier = getMethodSpeedModifier(currentMethod);
                int effectivePeriodsToComplete = Math.max(1, (int) Math.round(basePeriodsToComplete * speedModifier));

                // Determine how much income to give this period (even division)
                double incomePerPeriod = bidAmount / effectivePeriodsToComplete;

                // Credit the calculated income to the company's bank account
                jdbcTemplate.update(
                    "UPDATE public.company SET cash_on_hand = cash_on_hand + ? WHERE companyid = ? AND gameid = ?",
                    incomePerPeriod, companyId, gameId);

                // Deduct any method-related extra cost (or credit for savings)
                double extraCost = getMethodExtraCost(currentMethod, bidAmount);
                if (extraCost != 0) {
                    jdbcTemplate.update(
                        "UPDATE public.company SET cash_on_hand = cash_on_hand - ? WHERE companyid = ? AND gameid = ?",
                        extraCost, companyId, gameId);
                }


                // Compute how many periods have elapsed since the job was awarded
                int periodsWorked = currentPeriod - periodAwarded;

                // If the job is finished (worked effective number of periods), mark it as completed
                if (periodsWorked >= effectivePeriodsToComplete) {
                    jdbcTemplate.update(
                        "UPDATE public.job SET is_active = false, period_completed = ? WHERE jobid = ? AND gameid = ?",
                        currentPeriod, jobId, gameId);
                }
            }

        } catch (Exception e) {
            System.err.println("processJobIncome error: " + e.getMessage());
            e.printStackTrace();
        }
    }




    /**
     * Retrieves the payment history for a company.
     * 
     * This method returns the list of payments received by the company from completed jobs.
     * Each entry in the returned list is a map containing the following job and payment fields:
     * - jobid: Unique identifier for the job.
     * - job_type_id: Identifier for the type of job.
     * - estimated_workdays: Number of workdays the job was estimated to take.
     * - period_awarded: Game period in which the job was awarded.
     * - current_method: The current job production method in use.
     * - bid_amount: The amount the company bid for and was awarded the job.
     * 
     * Only jobs for which the company submitted a winning bid (outcome_code = 1) are included.
     * Results are sorted by jobid in ascending order.
     *
     * @param gameId    The ID of the current game session.
     * @param companyId The ID of the company whose payment history is requested.
     * @return List of maps, each representing a job and its corresponding payment amount.
     */
    public List<Map<String, Object>> getPaymentsReceived(int gameId, int companyId) {
        try {
            // SQL query to select relevant job and payment fields for the specified company and game
            String sql = """
                SELECT j.jobid, j.job_type_id, j.estimated_workdays, j.period_awarded,
                    j.current_method, b.bid_amount
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid 
                    AND j.companyid = b.companyid 
                    AND j.gameid = b.gameid
                    AND b.outcome_code = 1
                WHERE j.gameid = ? AND j.companyid = ?
                ORDER BY j.jobid ASC
                """;
            // Execute query and return list of results
            return jdbcTemplate.queryForList(sql, gameId, companyId);
        } catch (Exception e) {
            // Print error to standard error for debugging
            System.err.println("getPaymentsReceived error: " + e.getMessage());
            // Return empty list in case of any errors
            return new java.util.ArrayList<>();
        }
    }



    
}