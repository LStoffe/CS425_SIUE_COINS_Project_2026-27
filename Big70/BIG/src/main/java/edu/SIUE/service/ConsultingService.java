package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Calendar;

/**
 * ConsultingService.java
 *
 * Service class handling business logic for consulting service reports in the COINS simulation game.
 *
 * Provides support for:
 *   - Weather forecast reports
 *   - Materials cost index reports
 *   - Labor availability forecasts
 *   - Future demand estimation
 *   - Appraisal metrics generation
 *
 * Handles report cost deduction, data fetching from the database, and business logic
 * to generate randomized or calculated reports for company consulting services.
 *
 * Author: [Your Name or Team, if desired]
 * Created: [Creation Date, if known]
 */

@Service
public class ConsultingService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final double REPORT_COST = 500.0;

    /**
     * Attempts to purchase a consulting report for the given company in the specified game.
     * 
     * - Checks if the company has enough cash on hand to afford the report.
     * - Deducts the report cost from the company's available funds if affordable.
     * - Returns true if the purchase is successful, or false if there are insufficient funds.
     *
     * @param gameId    The ID of the game the company is playing in.
     * @param companyId The ID of the company attempting the purchase.
     * @return          true if purchase succeeded and funds deducted, false otherwise.
     */
    public boolean purchaseReport(int gameId, int companyId) {
        try {
            // Query the current cash on hand for the company
            double cashOnHand = jdbcTemplate.queryForObject(
                "SELECT cash_on_hand FROM public.company WHERE companyid = ? AND gameid = ?",
                Double.class, companyId, gameId);

            // If not enough cash, report cannot be purchased
            if (cashOnHand < REPORT_COST) {
                return false;
            }

            // Deduct report cost from company's cash_on_hand
            jdbcTemplate.update(
                "UPDATE public.company SET cash_on_hand = cash_on_hand - ? WHERE companyid = ? AND gameid = ?",
                REPORT_COST, companyId, gameId);

            // Purchase successful
            return true;
        } catch (Exception e) {
            // Log any errors that occur
            System.err.println("purchaseReport error: " + e.getMessage());
            return false;
        }
    }





    /**
     * Gets the weather forecast for the next 3 periods.
     * 
     * Uses game parameters (seasonal/monthly) to produce randomized temperature and rainfall
     * values for each future period. Returns a list of maps, each containing:
     * "period" (int) - the period number,
     * "temperature" (int) - the randomized temperature,
     * "rainfall" (int) - the randomized rainfall.
     * 
     *
     * @param gameId        the ID of the game to get parameters for
     * @param currentPeriod the current period (1-based index)
     * @return a List of Map<String, Object> projections for the next three periods, never null
     *
     */
    public List<Map<String, Object>> getWeatherForecast(int gameId, int currentPeriod) {
        try {
            // List to hold the forecast for each of the next 3 periods
            List<Map<String, Object>> forecast = new java.util.ArrayList<>();
            Random r = new Random();

            // Retrieve the start year/month for the game, used for seasonal calculation
            java.sql.Date startYear = jdbcTemplate.queryForObject(
                "SELECT start_year FROM public.game WHERE gameid = ?",
                java.sql.Date.class, gameId);

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startYear);
            int startMonth = startCal.get(Calendar.MONTH); // Calendar.MONTH is 0-based (0=January)

            for (int i = 1; i <= 3; i++) {
                // Calculate the DB period and calendar month for this forecast (2 months per DB period)
                int totalMonths = startMonth + (currentPeriod + i) * 2;
                int monthOfYear = totalMonths % 12; // 0 through 11

                // Map current monthOfYear (0-11) into a season period (1-12), each period = 2 months
                int seasonPeriod = (monthOfYear / 2) + 1;
                // Clamp period between 1 and 12
                seasonPeriod = Math.max(1, Math.min(12, seasonPeriod));

                // Query temperature parameters for this season period from static/base parameters
                Map<String, Object> tempParams = jdbcTemplate.queryForMap(
                    "SELECT temp_mean, temp_stddev, temp_min, temp_max " +
                    "FROM public.game_params_temperature " +
                    "WHERE gameid = 0 AND adminid = 0 AND periodid = ?", seasonPeriod);

                // Query rainfall parameters for this season period from static/base parameters
                Map<String, Object> rainParams = jdbcTemplate.queryForMap(
                    "SELECT rainfall_mean, rainfall_stddev, rainfall_min, rainfall_max " +
                    "FROM public.game_params_rainfall " +
                    "WHERE gameid = 0 AND adminid = 0 AND periodid = ?", seasonPeriod);

                // Get temperature parameters
                int tempMean   = ((Number) tempParams.get("temp_mean")).intValue();
                int tempStddev = ((Number) tempParams.get("temp_stddev")).intValue();
                int tempMin    = ((Number) tempParams.get("temp_min")).intValue();
                int tempMax    = ((Number) tempParams.get("temp_max")).intValue();

                // Generate a Gaussian-randomized temperature and clamp to allowed min/max
                int temp = (int) Math.round((r.nextGaussian() * tempStddev) + tempMean);
                temp = Math.max(tempMin, Math.min(tempMax, temp));

                // Get rainfall parameters
                int rainMean   = ((Number) rainParams.get("rainfall_mean")).intValue();
                int rainStddev = ((Number) rainParams.get("rainfall_stddev")).intValue();
                int rainMin    = ((Number) rainParams.get("rainfall_min")).intValue();
                int rainMax    = ((Number) rainParams.get("rainfall_max")).intValue();

                // Generate a Gaussian-randomized rainfall and clamp to allowed min/max
                int rain = (int) Math.round((r.nextGaussian() * rainStddev) + rainMean);
                rain = Math.max(rainMin, Math.min(rainMax, rain));

                // Prepare forecast Map for this period
                Map<String, Object> periodForecast = new java.util.HashMap<>();
                periodForecast.put("period", currentPeriod + i);   // Forecast is period N+1, N+2, N+3
                periodForecast.put("temperature", temp);
                periodForecast.put("rainfall", rain);

                forecast.add(periodForecast);
            }

            return forecast;
        } catch (Exception e) {
            // Log and return empty list in case of any errors
            System.err.println("getWeatherForecast error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }





    /**
     * Gets materials cost index forecast for the next 3 periods.
     * 
     * @param gameId        the game id (not used; uses static/base params)
     * @param currentPeriod the current period (the forecast is for next 3)
     * @return A list of maps, each containing:
     *         - "period": the future period number
     *         - "index":  randomized, clamped materials cost index for period
     */
    public List<Map<String, Object>> getMaterialsCostIndex(int gameId, int currentPeriod) {
        try {
            // Prepare forecast list to store period results
            List<Map<String, Object>> forecast = new java.util.ArrayList<>();
            Random r = new Random();

            // Forecast for the next 3 periods
            for (int i = 1; i <= 3; i++) {
                // Clamp period number to maximum (e.g., 12)
                int period = Math.min(currentPeriod + i, 12);

                // Query parameters for material cost index from base params
                Map<String, Object> params = jdbcTemplate.queryForMap("""
                    SELECT material_cost_index_mean, material_cost_index_stddev,
                           material_cost_index_min, material_cost_index_max
                    FROM public.game_params_material_cost_idx
                    WHERE gameid = 0 AND adminid = 0 AND periodid = ?
                    """, period);

                // Retrieve parameter values as int
                int mean = ((Number) params.get("material_cost_index_mean")).intValue();
                int stddev = ((Number) params.get("material_cost_index_stddev")).intValue();
                int min = ((Number) params.get("material_cost_index_min")).intValue();
                int max = ((Number) params.get("material_cost_index_max")).intValue();

                // Generate Gaussian random value, then clamp to [min, max]
                int index = (int) Math.round((r.nextGaussian() * stddev) + mean);
                index = Math.max(min, Math.min(max, index));

                // Prepare result map for this period
                Map<String, Object> periodData = new java.util.HashMap<>();
                periodData.put("period", currentPeriod + i);
                periodData.put("index", index);
                forecast.add(periodData);
            }

            // Return forecast list
            return forecast;
        } catch (Exception e) {
            // Log error and return an empty list if anything fails
            System.err.println("getMaterialsCostIndex error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }





    /**
     * Gets labor availability forecast for the next 3 periods.
     *
     * For each of the next 3 periods (clamped to a max of period 12), this method:
     *   - Fetches mean, standard deviation, min, and max for labor availability from the database.
     *   - Generates a normally distributed forecast value (clamped to [min, max]).
     *   - Builds a list of maps containing forecast data per period.
     *
     * @param gameId        The ID of the game (currently not used in query, stubbed as 0)
     * @param currentPeriod The current period (integer, 1-based)
     * @return A list of maps, each with keys "period" and "availability" for the next three periods.
     */
    public List<Map<String, Object>> getLaborAvailability(int gameId, int currentPeriod) {
        try {
            // List to store forecast data for each period
            List<Map<String, Object>> forecast = new java.util.ArrayList<>();
            // Use Java's random number generator for normal distribution
            Random r = new Random();

            // Loop for the next three periods
            for (int i = 1; i <= 3; i++) {
                // Clamp period to a maximum allowed (e.g., 12)
                int period = Math.min(currentPeriod + i, 12);

                // Get labor availability parameters for the given period from the database
                Map<String, Object> params = jdbcTemplate.queryForMap("""
                    SELECT labor_avail_mean, labor_avail_stddev,
                           labor_avail_min, labor_avail_max
                    FROM public.game_params_labor_avail
                    WHERE gameid = 0 AND adminid = 0 AND periodid = ?
                    """, period);

                // Extract values from query result and cast to int
                int mean = ((Number) params.get("labor_avail_mean")).intValue();
                int stddev = ((Number) params.get("labor_avail_stddev")).intValue();
                int min = ((Number) params.get("labor_avail_min")).intValue();
                int max = ((Number) params.get("labor_avail_max")).intValue();

                // Generate random labor availability (normally distributed, clamped to [min, max])
                int availability = (int) Math.round((r.nextGaussian() * stddev) + mean);
                availability = Math.max(min, Math.min(max, availability));

                // Store results in a map, include period and generated availability
                Map<String, Object> periodData = new java.util.HashMap<>();
                periodData.put("period", currentPeriod + i);
                periodData.put("availability", availability);
                forecast.add(periodData);
            }

            // Return the full forecast list for all three periods
            return forecast;
        } catch (Exception e) {
            // Print error message for debugging and return an empty list
            System.err.println("getLaborAvailability error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }





    /**
     * Retrieves a future demand forecast for the next 3 simulation periods.
     * This forecast estimates the expected number of new jobs available to the company
     * for each upcoming period, using a normal distribution based on historical simulation
     * parameters stored in the database.
     *
     * @param gameId        ID of the active simulation game (currently unused, always 0 in query)
     * @param currentPeriod The current period or round of the game (1-based, e.g., 1-12)
     * @return              A list of maps, each containing "period" (int) and "expectedJobs" (int)
     */
    public List<Map<String, Object>> getFutureDemand(int gameId, int currentPeriod) {
        try {
            // List to store demand forecasts for each of the next 3 periods
            List<Map<String, Object>> forecast = new java.util.ArrayList<>();
            // Random object for generating normally distributed job counts
            Random r = new Random();

            // Generate forecast for the next 3 periods
            for (int i = 1; i <= 3; i++) {
                // Clamp period to a maximum (e.g., 12, the game's maximum period)
                int period = Math.min(currentPeriod + i, 12);

                // Query database for distribution parameters for jobs in this period
                Map<String, Object> params = jdbcTemplate.queryForMap("""
                    SELECT number_jobs_mean, number_jobs_stddev,
                           number_jobs_min, number_jobs_max
                    FROM public.game_params_jobs_per_period
                    WHERE gameid = 0 AND adminid = 0 AND periodid = ?
                    """, period);

                // Retrieve mean (average), standard deviation, min and max as ints
                int mean = ((Number) params.get("number_jobs_mean")).intValue();
                int stddev = ((Number) params.get("number_jobs_stddev")).intValue();
                int min = ((Number) params.get("number_jobs_min")).intValue();
                int max = ((Number) params.get("number_jobs_max")).intValue();

                // Generate expected jobs: normal distribution, clamped to [min, max]
                int jobs = (int) Math.round((r.nextGaussian() * stddev) + mean);
                jobs = Math.max(min, Math.min(max, jobs));

                // Store results for this period in a map
                Map<String, Object> periodData = new java.util.HashMap<>();
                periodData.put("period", currentPeriod + i);   // The actual period number forecasted
                periodData.put("expectedJobs", jobs);           // The generated job count
                forecast.add(periodData);
            }

            // Return the list of forecasted job data
            return forecast;
        } catch (Exception e) {
            // Handle possible database or casting errors gracefully
            System.err.println("getFutureDemand error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }



    

    /**
     * Retrieves appraisal metrics for a given company and period.
     *
     * @param companyId the ID of the company whose appraisal metrics are being requested.
     * @param currentPeriod the simulation period for which the metrics should be retrieved.
     * @return a Map containing the appraisal metrics if found;
     *         null if no metrics exist for the given company and period,
     *         or if an error occurs during querying.
     */
    public Map<String, Object> getAppraisalMetrics(int companyId, int currentPeriod) {
        try {
            // Query the appraisal_metrics table for this company and period.
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
                SELECT financial_liquidity, financial_success, bid_responsibility,
                       pace, ethics, name_recognition, apartments, schools, offices,
                       hospitals, industrial
                FROM public.appraisal_metrics
                WHERE companyid = ? AND periodid = ?
                """, companyId, currentPeriod);

            // If no data is found, return null. Otherwise, return the first row (should be only one per company & period).
            return rows.isEmpty() ? null : rows.get(0);
        } catch (Exception e) {
            // Log the error and return null to indicate that metrics could not be retrieved.
            System.err.println("getAppraisalMetrics error: " + e.getMessage());
            return null;
        }
    }




}