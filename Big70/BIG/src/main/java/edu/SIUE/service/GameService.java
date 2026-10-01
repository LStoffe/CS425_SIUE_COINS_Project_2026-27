package edu.SIUE.service;

import edu.calpoly.its.mas.big.db.BIGDatabaseFacade;
import edu.calpoly.its.mas.big.game.Game;
import edu.calpoly.its.mas.big.users.admin.Admin;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.text.ParsePosition;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

/**
 * GameService.java
 *
 * Service layer for business logic related to game creation, management, and operations
 * within the BIG simulation platform.
 *
 * Responsibilities:
 *   - Handles core game creation including parsing, validation, and business rule enforcement.
 *   - Integrates with BIGDatabaseFacade for persistent logic.
 *   - Provides methods to initialize games, manage game types, and trigger related domain events.
 *   - Connects to the underlying database using Spring's JdbcTemplate as needed.
 *
 * Typical Usage:
 *   - Invoked by controller or UI layers to process admin game creation requests and management actions.
 *   - Calls related services (e.g., JobService) to set up default or initial game data as part of workflow.
 *
 * Dependencies:
 *   - edu.calpoly.its.mas.big.db.BIGDatabaseFacade
 *   - edu.calpoly.its.mas.big.game.Game
 *   - Spring's JdbcTemplate and dependency injection.
 * 
 */

@Service
public class GameService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobService jobService;

    /**
     * Creates a new game entry in the database for a given admin.
     * 
     * @param adminId   The ID of the admin creating the game.
     * @param gameName  The name of the new game.
     * @param startYear The starting year of the game (as a String).
     * @param building  Whether the "building" game type is enabled.
     * @param heavy     Whether the "heavy" game type is enabled.
     * @return true if the game was created successfully, false otherwise.
     */
    public boolean createGame(int adminId, String gameName, String startYear, boolean building, boolean heavy) {
        try {
            // Compose start date string in MM/dd/yyyy format and parse it into a Date object
            String startDateString = "1/1/" + startYear;
            Date startDate = new SimpleDateFormat("MM/dd/yyyy")
                .parse(startDateString, new ParsePosition(0));

            // Determine the game type based on input options
            int gametype = 0;
            if (building && !heavy)  gametype = 1;
            if (!building && heavy)  gametype = 2;
            if (building && heavy)   gametype = 3;

            // Create Game object
            Game game = new Game(adminId, gameName, startDate, gametype);

            // Initialize database facade, add the game, and close the connection
            BIGDatabaseFacade dbFacade = new BIGDatabaseFacade();
            dbFacade.getGameDBHandler().addGame(game);
            dbFacade.close();
            
            jobService.generateInitialJobs(game.getGameID(), adminId);

            return true;

        } catch (Exception e) {
            // Print error and return false if any exception occurs
            System.err.println("createGame error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }





    /**
     * Deletes a game and all related database records for a specific admin and game ID.
     *
     * @param adminId The admin's ID (currently unused, but could be used for authorization).
     * @param gameId  The ID of the game to delete.
     */
    public void deleteGame(int adminId, int gameId) {
        try {
            // Delete all related data in correct order (children before parents)
            jdbcTemplate.update("DELETE FROM activity WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM activity_parameters WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM available_equipment WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM available_personnel WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM balance_sheet WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM bid WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM bid_method WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM billing_breakdown WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM billings WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM cash_flow WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM company_members WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM company_personnel WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM component WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM computer_controlled_contractors WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM contract_reports WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM current_methods WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM dates_update_policy WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM days_update_policy WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM equip_network_dep WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM estimates_available WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM estimates_per_period WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM financial_report WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_ccc WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_job_size WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_jobs_per_period WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_labor_avail WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_liquidated_damages WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_material_cost_idx WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_misc WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_negotiation WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_overhead WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_percent_takeoff WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_rainfall WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_report_costs WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM game_params_temperature WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM grading_template WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM interval_update_policy WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM job_allowed_companies WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM job_financial_info WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM job_type_req_mos WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM loan WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM main_office_skills WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM method WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM mos_point_conversion WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM mosp_cost WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM mosp_generation_jrnl WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM mosp_unfilled_jrnl WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM negotiated_job WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM overtime WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM period WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM ratios WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM schedule_estimated_methods WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM update_policy WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM job WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM company WHERE gameid = ?", gameId);
            jdbcTemplate.update("DELETE FROM public.game WHERE gameid = ?", gameId);


        } catch (Exception e) {
            // Print error if any exception occurs during deletion
            System.err.println("deleteGame error: " + e.getMessage());
            e.printStackTrace();
        }
    }





    /**
     * Retrieves the details of a game from the database by its ID.
     * 
     * @param gameId The ID of the game to retrieve.
     * @return A Map containing game details, or null if not found or in case of error.
     */
    public Map<String, Object> getGameById(int gameId) {
        try {
            // Query to retrieve game details by ID
            String sql = """
                SELECT gameid, gamename, gametype, start_year, current_period, is_active
                FROM public.game
                WHERE gameid = ?
                """;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, gameId);
            if (rows.isEmpty()) return null;

            Map<String, Object> row = rows.get(0);
            Map<String, Object> game = new java.util.HashMap<>();
            // Populate result map with game info for the view layer
            game.put("id", row.get("gameid"));
            game.put("name", row.get("gamename"));
            game.put("startYear", row.get("start_year").toString().substring(0, 4));
            game.put("currentPeriod", row.get("current_period"));
            game.put("status", Boolean.TRUE.equals(row.get("is_active")) ? "Active" : "Inactive");

            // Determine human-readable game type
            Object gametypeObj = row.get("gametype");
            String type = "Unknown";
            if (gametypeObj != null) {
                int gametype = (int) gametypeObj;
                type = gametype == 1 ? "Building" : gametype == 2 ? "Heavy" : "Building & Heavy";
            }
            game.put("type", type);
            return game;

        } catch (Exception e) {
            // Print error and return null if any exception occurs
            System.err.println("getGameById error: " + e.getMessage());
            return null;
        }
    }





    /**
     * Returns the date string representing the game period for the specified game.
     *
     * The result is a string with the names of two consecutive months and the year,
     * based on the game's start year and current period. Each period represents two months.
     *
     * @param gameId The ID of the game.
     * @return A string in the format "Month1 & Month2 Year" (e.g., "January & February 2023"),
     *         or "Unknown Period" if an error occurs.
     */
    public String getPeriodDateString(int gameId) {
        try {
            // Query for the current_period and start_year of the game
            String sql = "SELECT current_period, start_year FROM public.game WHERE gameid = ?";
            Map<String, Object> game = jdbcTemplate.queryForMap(sql, gameId);

            // Get current period and start year
            int currentPeriod = ((Number) game.get("current_period")).intValue();
            java.sql.Date startYear = (java.sql.Date) game.get("start_year");

            // Set up the calendar with the start year and advance it by (currentPeriod * 2) months
            Calendar cal = Calendar.getInstance();
            cal.setTime(startYear);
            cal.add(Calendar.MONTH, currentPeriod * 2);

            // Format for month names
            java.text.SimpleDateFormat monthFormatter = new java.text.SimpleDateFormat("MMMM");
            String firstMonth = monthFormatter.format(cal.getTime());

            // Advance calendar by one month for the second month
            cal.add(Calendar.MONTH, 1);
            String secondMonth = monthFormatter.format(cal.getTime());

            // Format for year
            java.text.SimpleDateFormat yearFormatter = new java.text.SimpleDateFormat("yyyy");
            String year = yearFormatter.format(cal.getTime());

            // Combine and return the result string
            return firstMonth + " & " + secondMonth + " " + year;

        } catch (Exception e) {
            // Log and return an error string if there is a failure
            System.err.println("getPeriodDateString error: " + e.getMessage());
            return "Unknown Period";
        }
    }



    
}

