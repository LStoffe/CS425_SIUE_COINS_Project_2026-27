package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * PersonnelService.java
 *
 * Handles all business logic related to company personnel management.
 * Includes hiring, firing, payroll deductions, and personnel reports.
 */
@Service
public class PersonnelService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Gets all available employee types with their costs.
     */
    public List<Map<String, Object>> getAvailablePersonnel() {
        try {
            return jdbcTemplate.queryForList("""
                SELECT employee_title, employee_cost_per_period, 
                       employee_hiring_cost, employee_firing_cost, category
                FROM public.available_personnel
                WHERE gameid = 0 AND adminid = 0
                ORDER BY category ASC, employee_title ASC
                """);
        } catch (Exception e) {
            System.err.println("getAvailablePersonnel error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }





    /**
     * Gets current personnel roster for a company in the current period.
     * Returns a map of employee_title -> count for easy lookup.
     */
    public Map<String, Integer> getCompanyPersonnelCounts(int gameId, int companyId, int currentPeriod) {
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("""
                SELECT employee_title, employee_type_count
                FROM public.company_personnel
                WHERE gameid = ? AND companyid = ? AND periodid = ?
                """, gameId, companyId, currentPeriod);

            Map<String, Integer> counts = new java.util.HashMap<>();
            for (Map<String, Object> row : rows) {
                String title = (String) row.get("employee_title");
                int count = ((Number) row.get("employee_type_count")).intValue();
                counts.put(title, count);
            }
            return counts;
        } catch (Exception e) {
            System.err.println("getCompanyPersonnelCounts error: " + e.getMessage());
            return new java.util.HashMap<>();
        }
    }





    /**
     * Updates company personnel based on hire/fire changes submitted from the form.
     * Deducts hiring/firing costs from cash on hand.
     * Saves updated counts to company_personnel for the current period.
     *
     * @param gameId        The game session ID
     * @param companyId     The company making changes
     * @param currentPeriod The current game period
     * @param changes       Map of employee_title -> change amount (positive = hire, negative = fire)
     * @return error message if validation fails, null if successful
     */
    public String updatePersonnel(int gameId, int companyId, int currentPeriod, Map<String, Integer> changes) {
        try {
            // Get current counts
            Map<String, Integer> currentCounts = getCompanyPersonnelCounts(gameId, companyId, currentPeriod);

            // Get all available personnel with costs
            List<Map<String, Object>> availablePersonnel = getAvailablePersonnel();
            Map<String, Map<String, Object>> personnelMap = new java.util.HashMap<>();
            for (Map<String, Object> p : availablePersonnel) {
                personnelMap.put((String) p.get("employee_title"), p);
            }

            double totalHiringCost = 0;
            double totalFiringCost = 0;

            // Calculate total hiring/firing costs
            for (Map.Entry<String, Integer> entry : changes.entrySet()) {
                String title = entry.getKey();
                int change = entry.getValue();
                if (change == 0) continue;

                Map<String, Object> personnel = personnelMap.get(title);
                if (personnel == null) continue;

                if (change > 0) {
                    totalHiringCost += change * ((Number) personnel.get("employee_hiring_cost")).doubleValue();
                } else {
                    totalFiringCost += Math.abs(change) * ((Number) personnel.get("employee_firing_cost")).doubleValue();
                }
            }

            double totalCost = totalHiringCost + totalFiringCost;

            // Check if company can afford hiring costs
            double cashOnHand = jdbcTemplate.queryForObject(
                "SELECT cash_on_hand FROM public.company WHERE companyid = ? AND gameid = ?",
                Double.class, companyId, gameId);

            if (totalCost > cashOnHand) {
                return "Insufficient funds to cover hiring and firing costs of $" +
                    String.format("%,.2f", totalCost);
            }

            // Apply changes
            for (Map.Entry<String, Integer> entry : changes.entrySet()) {
                String title = entry.getKey();
                int change = entry.getValue();
                if (change == 0) continue;

                int currentCount = currentCounts.getOrDefault(title, 0);
                int newCount = Math.max(0, currentCount + change);
                int adds = change > 0 ? change : 0;
                int losses = change < 0 ? Math.abs(change) : 0;

                // Check if record exists for this period
                int exists = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM public.company_personnel WHERE gameid = ? AND companyid = ? AND periodid = ? AND employee_title = ?",
                    Integer.class, gameId, companyId, currentPeriod, title);

                if (exists > 0) {
                    jdbcTemplate.update("""
                        UPDATE public.company_personnel 
                        SET employee_type_count = ?, employee_type_adds = employee_type_adds + ?, 
                            employee_type_loss = employee_type_loss + ?
                        WHERE gameid = ? AND companyid = ? AND periodid = ? AND employee_title = ?
                        """, newCount, adds, losses, gameId, companyId, currentPeriod, title);
                } else {
                    jdbcTemplate.update("""
                        INSERT INTO public.company_personnel 
                        (gameid, companyid, periodid, employee_title, employee_type_count, employee_type_adds, employee_type_loss)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """, gameId, companyId, currentPeriod, title, newCount, adds, losses);
                }
            }

            // Deduct hiring/firing costs from cash on hand
            if (totalCost > 0) {
                jdbcTemplate.update(
                    "UPDATE public.company SET cash_on_hand = cash_on_hand - ? WHERE companyid = ? AND gameid = ?",
                    totalCost, companyId, gameId);
            }

            return null; // success

        } catch (Exception e) {
            System.err.println("updatePersonnel error: " + e.getMessage());
            return "An error occurred while updating personnel.";
        }
    }





    /**
     * Processes payroll for all companies in a game when period advances.
     * Deducts employee_cost_per_period * count from cash on hand for each company.
     */
    public void processPayroll(int gameId, int currentPeriod) {
        try {
            // Get all companies in this game
            List<Map<String, Object>> companies = jdbcTemplate.queryForList(
                "SELECT companyid FROM public.company WHERE gameid = ?", gameId);

            for (Map<String, Object> company : companies) {
                int companyId = ((Number) company.get("companyid")).intValue();

                // Get current personnel counts
                List<Map<String, Object>> personnel = jdbcTemplate.queryForList("""
                    SELECT cp.employee_title, cp.employee_type_count, ap.employee_cost_per_period
                    FROM public.company_personnel cp
                    JOIN public.available_personnel ap ON cp.employee_title = ap.employee_title AND ap.gameid = 0 AND ap.adminid = 0
                    WHERE cp.gameid = ? AND cp.companyid = ? AND cp.periodid = ?
                    """, gameId, companyId, currentPeriod);

                double totalPayroll = 0;
                for (Map<String, Object> p : personnel) {
                    int count = ((Number) p.get("employee_type_count")).intValue();
                    double costPerPeriod = ((Number) p.get("employee_cost_per_period")).doubleValue();
                    totalPayroll += count * costPerPeriod;
                }

                if (totalPayroll > 0) {
                    jdbcTemplate.update(
                        "UPDATE public.company SET cash_on_hand = cash_on_hand - ? WHERE companyid = ? AND gameid = ?",
                        totalPayroll, companyId, gameId);

                    // Copy personnel counts to next period so they persist
                    for (Map<String, Object> p : personnel) {
                        String title = (String) p.get("employee_title");
                        int count = ((Number) p.get("employee_type_count")).intValue();
                        jdbcTemplate.update("""
                            INSERT INTO public.company_personnel 
                            (gameid, companyid, periodid, employee_title, employee_type_count, employee_type_adds, employee_type_loss)
                            VALUES (?, ?, ?, ?, ?, 0, 0)
                            ON CONFLICT DO NOTHING
                            """, gameId, companyId, currentPeriod + 1, title, count);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("processPayroll error: " + e.getMessage());
            e.printStackTrace();
        }
    }





    /**
     * Gets full personnel report for a company including costs.
     */
    public List<Map<String, Object>> getPersonnelReport(int gameId, int companyId, int currentPeriod) {
        try {
            return jdbcTemplate.queryForList("""
                SELECT cp.employee_title, cp.employee_type_count, 
                       ap.employee_cost_per_period,
                       (cp.employee_type_count * ap.employee_cost_per_period) AS total_cost
                FROM public.company_personnel cp
                JOIN public.available_personnel ap ON cp.employee_title = ap.employee_title 
                    AND ap.gameid = 0 AND ap.adminid = 0
                WHERE cp.gameid = ? AND cp.companyid = ? AND cp.periodid = ?
                AND cp.employee_type_count > 0
                ORDER BY ap.category ASC, cp.employee_title ASC
                """, gameId, companyId, currentPeriod);
        } catch (Exception e) {
            System.err.println("getPersonnelReport error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }



    
}