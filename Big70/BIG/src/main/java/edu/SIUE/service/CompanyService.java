package edu.SIUE.service;

import edu.calpoly.its.mas.big.db.BIGDatabaseFacadeInterface;
import edu.calpoly.its.mas.big.game.Game;
import edu.calpoly.its.mas.big.users.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CompanyService.java
 *
 * Service layer responsible for company management and registration within the SIUE BIG simulation platform.
 *
 * Responsibilities:
 *   - Handles business logic and processing for registering new companies, validating unique usernames,
 *     and storing company records securely.
 *   - Integrates with Spring's JdbcTemplate for database access and uses PasswordEncoder to securely hash passwords.
 *   - Supports creation of company accounts by administrators, calculation of derived financial limits,
 *     and enforcement of business constraints (such as unique usernames across all users).
 *
 * Typical Usage:
 *   - Invoked by controller layers to process company registration forms and administrative company management actions.
 *   - Interacts directly with the 'company' table in the database.
 *
 */

@Service
public class CompanyService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new company to a game session.
     *
     * @param gameId               The ID of the game to register the company under.
     * @param adminId              The ID of the admin creating the company.
     * @param username             The company's login username.
     * @param password             The company's login password.
     * @param title                The company's display title.
     * @param cashOnHand           Starting cash on hand.
     * @param wipLimit             Work in progress bond limit.
     * @param perJobLimitAsWIPPercent Per job limit as a percentage of WIP limit.
     * @return true if successful, false otherwise.
     */
    public boolean registerCompany(int gameId, int adminId, String username,
                                String password, String title, long cashOnHand,
                                long wipLimit, int perJobLimitAsWIPPercent) {
        try {
            // Check if username already exists
            int adminCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.admin WHERE username = ?", Integer.class, username);
            int companyCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.company WHERE username = ?", Integer.class, username);

            if (adminCount > 0 || companyCount > 0) {
                System.err.println("registerCompany error: Username already exists.");
                return false;
            }

            // Calculate derived values
            long perJobLimit = Math.round((((float) perJobLimitAsWIPPercent) / 100f) * wipLimit);
            int wipLimitPercent = Math.round((((float) cashOnHand) / ((float) wipLimit)) * 100f);

            // Get next company ID
            int companyId = jdbcTemplate.queryForObject(
                "SELECT COALESCE(MAX(companyid), 0) + 1 FROM public.company", Integer.class);

            String encodedPassword = passwordEncoder.encode(password);

            String insertSql = """
                INSERT INTO public.company 
                (companyid, gameid, username, password, title, cash_on_hand, wip_limit, 
                wip_limit_percent, per_job_limit, per_job_wip_percent, wip, 
                missionstatement, corevalues, companyurl, license)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 'Mission Statement', 'Core Values', 'http://', 0)
                """;

            // Then change the jdbcTemplate.update call:
            jdbcTemplate.update(insertSql, companyId, gameId, username, encodedPassword,
                title, cashOnHand, wipLimit, wipLimitPercent,
                perJobLimit, perJobLimitAsWIPPercent);

            return true;

        } catch (Exception e) {
            System.err.println("registerCompany error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }





    /**
     * Retrieves a company's information by companyId and gameId.
     *
     * @param companyId the unique ID of the company
     * @param gameId the game session ID the company belongs to
     * @return a map containing the company's properties or null if not found or on error
     */
    public Map<String, Object> getCompanyById(int companyId, int gameId) {
        try {
            String sql = """
                SELECT companyid, gameid, username, title, cash_on_hand, wip_limit,
                       wip_limit_percent, per_job_limit, per_job_wip_percent, wip,
                       missionstatement, corevalues, companyurl, license
                FROM public.company
                WHERE companyid = ? AND gameid = ?
                """;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, companyId, gameId);
            return rows.isEmpty() ? null : rows.get(0);
        } catch (Exception e) {
            System.err.println("getCompanyById error: " + e.getMessage());
            return null;
        }
    }





    /**
     * Retrieves all companies associated with a specific game.
     *
     * @param gameId the ID of the game session
     * @return a list of maps, each containing a company's basic properties;
     *         returns an empty list if no companies found or on error
     */
    public List<Map<String, Object>> getCompaniesByGame(int gameId) {
        try {
            String sql = """
                SELECT companyid, title, username, cash_on_hand, 
                       wip_limit, per_job_limit, wip, license
                FROM public.company
                WHERE gameid = ?
                ORDER BY title ASC
                """;
            return jdbcTemplate.queryForList(sql, gameId);
        } catch (Exception e) {
            System.err.println("getCompaniesByGame error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }





    /**
     * Deletes a company and all associated data from the database for a given game.
     *
     * Performs deletion in related tables before removing the company row
     * to preserve referential integrity and avoid orphan data.
     *
     * @param gameId the ID of the game session
     * @param companyId the unique ID of the company to delete
     */
    public void deleteCompany(int gameId, int companyId) {
        try {
            // Delete all related data from dependent tables before deleting the company
            jdbcTemplate.update("DELETE FROM company_members WHERE companyid = ? AND gameid = ?", companyId, gameId);
            jdbcTemplate.update("DELETE FROM company_personnel WHERE companyid = ? AND gameid = ?", companyId, gameId);
            jdbcTemplate.update("DELETE FROM bid WHERE companyid = ?", companyId);
            jdbcTemplate.update("DELETE FROM loan WHERE companyid = ?", companyId);
            jdbcTemplate.update("DELETE FROM balance_sheet WHERE companyid = ? AND gameid = ?", companyId, gameId);
            jdbcTemplate.update("DELETE FROM billings WHERE companyid = ? AND gameid = ?", companyId, gameId);
            jdbcTemplate.update("DELETE FROM cash_flow WHERE companyid = ? AND gameid = ?", companyId, gameId);
            jdbcTemplate.update("DELETE FROM ratios WHERE companyid = ? AND gameid = ?", companyId, gameId);
            jdbcTemplate.update("DELETE FROM contract_reports WHERE companyid = ? AND gameid = ?", companyId, gameId);
            jdbcTemplate.update("DELETE FROM company WHERE companyid = ? AND gameid = ?", companyId, gameId);

            
        } catch (Exception e) {
            System.err.println("deleteCompany error: " + e.getMessage());
            e.printStackTrace();
        }
    }





    /**
     * Updates the company information (title and/or password) in the database.
     *
     * If a new password is provided and is not empty, both the company title and password
     * will be updated (with the password securely encoded). If no new password is provided,
     * only the company title will be updated.
     *
     * @param companyId    The unique ID of the company to update.
     * @param gameId       The unique ID of the game the company belongs to.
     * @param newTitle     The new company title to set.
     * @param newPassword  The new password to set (if not null/empty).
     * @return true if the update succeeds, false otherwise.
     */
    public boolean updateCompanyInfo(int companyId, int gameId, String newTitle, String newPassword) {
        try {
            // If a non-empty new password is provided, update both title and password
            if (newPassword != null && !newPassword.isEmpty()) {
                String encodedPassword = passwordEncoder.encode(newPassword);
                jdbcTemplate.update(
                    "UPDATE public.company SET title = ?, password = ? WHERE companyid = ? AND gameid = ?",
                    newTitle, encodedPassword, companyId, gameId
                );
            } else {
                // Otherwise, update only the title
                jdbcTemplate.update(
                    "UPDATE public.company SET title = ? WHERE companyid = ? AND gameid = ?",
                    newTitle, companyId, gameId
                );
            }
            return true;
        } catch (Exception e) {
            System.err.println("updateCompanyInfo error: " + e.getMessage());
            return false;
        }
    }


    /**
     * Updates the core values for a given company in the specified game.
     *
     * @param companyId   The unique ID of the company.
     * @param gameId      The unique ID of the game.
     * @param coreValues  The new core values to set for the company.
     */
    public void updateCoreValues(int companyId, int gameId, String coreValues) {
        try {
            jdbcTemplate.update(
                "UPDATE public.company SET corevalues = ? WHERE companyid = ? AND gameid = ?",
                coreValues, companyId, gameId);
        } catch (Exception e) {
            System.err.println("updateCoreValues error: " + e.getMessage());
        }
    }




    /**
     * Updates the mission statement for a given company in the specified game.
     *
     * @param companyId        The unique ID of the company.
     * @param gameId           The unique ID of the game.
     * @param missionStatement The new mission statement to set for the company.
     */
    public void updateMissionStatement(int companyId, int gameId, String missionStatement) {
        try {
            jdbcTemplate.update(
                "UPDATE public.company SET missionstatement = ? WHERE companyid = ? AND gameid = ?",
                missionStatement, companyId, gameId);
        } catch (Exception e) {
            System.err.println("updateMissionStatement error: " + e.getMessage());
        }
    }




    /**
     * Updates the company URL for a given company in the specified game.
     *
     * @param companyId  The unique ID of the company.
     * @param gameId     The unique ID of the game.
     * @param companyUrl The new company URL to set for the company.
     */
    public void updateCompanyUrl(int companyId, int gameId, String companyUrl) {
        try {
            jdbcTemplate.update(
                "UPDATE public.company SET companyurl = ? WHERE companyid = ? AND gameid = ?",
                companyUrl, companyId, gameId);
        } catch (Exception e) {
            System.err.println("updateCompanyUrl error: " + e.getMessage());
        }
    }



    
}