package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * ReportService.java
 *
 * Handles all business logic for financial report generation.
 */
@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves and calculates balance sheet data for a specific company in a game.
     *
     * <p>
     * This method pulls cash and loan data from the database for the company identified by
     * {@code companyId} and {@code gameId}, and computes relevant balance sheet items,
     * including assets, liabilities, and equity.
     * </p>
     *
     * @param gameId    The ID of the game/simulation context.
     * @param companyId The ID of the company for which the balance sheet is generated.
     * @return A map containing:
     *    <ul>
     *      <li>{@code "cash"} - Cash on hand</li>
     *      <li>{@code "totalCurrentAssets"} - Total current assets (currently only cash)</li>
     *      <li>{@code "totalAssets"} - Total assets (currently only cash)</li>
     *      <li>{@code "loanBalance"} - Outstanding (active) loan balance</li>
     *      <li>{@code "totalLiabilities"} - Total liabilities (currently only loan balance)</li>
     *      <li>{@code "retainedEarnings"} - Retained earnings (assets minus liabilities)</li>
     *      <li>{@code "totalLiabilitiesAndEquity"} - Sum of liabilities and retained earnings</li>
     *    </ul>
     *    The map will be empty if an error occurs.
     */
    public Map<String, Object> getBalanceSheet(int gameId, int companyId) {
        try {
            // Retrieve cash and other company financial data from the company table
            Map<String, Object> company = jdbcTemplate.queryForMap("""
                SELECT cash_on_hand, wip_limit, per_job_limit, wip
                FROM public.company
                WHERE companyid = ? AND gameid = ?
                """, companyId, gameId);

            // Retrieve the sum of active loan balances (where payments are still remaining)
            List<Map<String, Object>> loans = jdbcTemplate.queryForList("""
                SELECT COALESCE(SUM(balance), 0) as total_loan_balance
                FROM public.loan
                WHERE companyid = ? AND gameid = ? AND payments_remaining > 0
                """, companyId, gameId);

            // Extract values from query results
            double cashOnHand = ((Number) company.get("cash_on_hand")).doubleValue();
            double loanBalance = loans.isEmpty() ? 0 :
                ((Number) loans.get(0).get("total_loan_balance")).doubleValue();

            // Calculate balance sheet values (expand as you add more asset or liability categories)
            double totalCurrentAssets = cashOnHand; // Only cash currently
            double totalAssets = totalCurrentAssets;
            double totalLiabilities = loanBalance;  // Only loans currently
            double retainedEarnings = totalAssets - totalLiabilities;
            double totalLiabilitiesAndEquity = totalLiabilities + retainedEarnings;

            // Assemble results in a map
            Map<String, Object> balanceSheet = new java.util.HashMap<>();
            balanceSheet.put("cash", cashOnHand);
            balanceSheet.put("totalCurrentAssets", totalCurrentAssets);
            balanceSheet.put("totalAssets", totalAssets);
            balanceSheet.put("loanBalance", loanBalance);
            balanceSheet.put("totalLiabilities", totalLiabilities);
            balanceSheet.put("retainedEarnings", retainedEarnings);
            balanceSheet.put("totalLiabilitiesAndEquity", totalLiabilitiesAndEquity);

            return balanceSheet;

        } catch (Exception e) {
            // Log error and return empty result map on failure
            System.err.println("getBalanceSheet error: " + e.getMessage());
            return new java.util.HashMap<>();
        }
    }



    /**
     * Retrieves the cash flow report data for a company in the specified game and current period.
     *
     * <p>This report includes:
     * - Starting cash on hand at the beginning of the period
     * - Total job income earned this period for in-progress jobs
     * - Total inflows (job income)
     * - Loan payments and loan interest due this period
     * - Payroll costs for all employees this period
     * - Total outflows (loan + payroll)
     * - Ending cash on hand
     *
     * @param gameId    the id for the game session
     * @param companyId the id for the company
     * @return a Map of key-value pairs describing the cash flow report data
     */
    public Map<String, Object> getCashFlow(int gameId, int companyId) {
        try {
            // Query the company's current cash on hand
            double cashOnHand = jdbcTemplate.queryForObject(
                "SELECT cash_on_hand FROM public.company WHERE companyid = ? AND gameid = ?",
                Double.class, companyId, gameId);

            // Query for all active jobs to sum up the periodic job income
            // Only jobs with outcome_code 1 and is_active = true are included
            List<Map<String, Object>> activeJobs = jdbcTemplate.queryForList("""
                SELECT j.estimated_workdays, j.current_method, b.bid_amount
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid 
                    AND j.companyid = b.companyid 
                    AND j.gameid = b.gameid
                    AND b.outcome_code = 1
                WHERE j.gameid = ? AND j.companyid = ? AND j.is_active = true
                """, gameId, companyId);

            // Accumulate job income for the period
            // Each job's bid is spread across periods based on estimated workdays and method
            double totalJobIncome = 0;
            for (Map<String, Object> job : activeJobs) {
                int estimatedWorkdays = ((Number) job.get("estimated_workdays")).intValue();
                int currentMethod = ((Number) job.get("current_method")).intValue();
                double bidAmount = ((Number) job.get("bid_amount")).doubleValue();

                // Speed modifier can be changed by work method
                double speedModifier = switch (currentMethod) {
                    case 2 -> 0.75;
                    case 3 -> 0.50;
                    case 4 -> 1.25;
                    default -> 1.0;
                };
                // Calculate how many periods required for this job, default minimum 1 period
                int periodsToComplete = Math.max(1, (int) Math.round((estimatedWorkdays / 40) * speedModifier));
                totalJobIncome += bidAmount / periodsToComplete;
            }

            // Query for any loans with payments remaining for this company and game
            List<Map<String, Object>> loans = jdbcTemplate.queryForList("""
                SELECT payment_amount, balance
                FROM public.loan
                WHERE companyid = ? AND gameid = ? AND payments_remaining > 0
                """, companyId, gameId);

            // Calculate combined loan repayment plus 2% interest on current loan balance
            double totalLoanPayments = 0;
            for (Map<String, Object> loan : loans) {
                double paymentAmount = ((Number) loan.get("payment_amount")).doubleValue();
                double balance = ((Number) loan.get("balance")).doubleValue();
                double interest = balance * 0.02;
                totalLoanPayments += paymentAmount + interest;
            }

            // Get the current period number for the game
            int currentPeriod = jdbcTemplate.queryForObject(
                "SELECT current_period FROM public.game WHERE gameid = ?",
                Integer.class, gameId);

            // Query for company personnel this period for payroll calculation
            List<Map<String, Object>> personnel = jdbcTemplate.queryForList("""
                SELECT cp.employee_type_count, ap.employee_cost_per_period
                FROM public.company_personnel cp
                JOIN public.available_personnel ap ON cp.employee_title = ap.employee_title
                    AND ap.gameid = 0 AND ap.adminid = 0
                WHERE cp.gameid = ? AND cp.companyid = ? AND cp.periodid = ?
                AND cp.employee_type_count > 0
                """, gameId, companyId, currentPeriod);

            // Compute total payroll expense for all personnel
            double totalPayroll = 0;
            for (Map<String, Object> p : personnel) {
                int count = ((Number) p.get("employee_type_count")).intValue();
                double cost = ((Number) p.get("employee_cost_per_period")).doubleValue();
                totalPayroll += count * cost;
            }

            // Sum up all outflows (loan repayments and payroll)
            double totalOutflows = totalLoanPayments + totalPayroll;

            // Calculate starting cash for the period
            // cashStart = cashEnd - inflows + outflows
            double cashStart = cashOnHand - totalJobIncome + totalOutflows;

            // Build the cash flow report map to return
            Map<String, Object> cashFlow = new java.util.HashMap<>();
            cashFlow.put("cashStart", cashStart);        // Cash at start of period
            cashFlow.put("jobIncome", totalJobIncome);    // Job inflows this period
            cashFlow.put("totalInflows", totalJobIncome); // Alias for inflows
            cashFlow.put("loanPayments", totalLoanPayments);  // Loan outflows
            cashFlow.put("payroll", totalPayroll);        // Payroll outflows
            cashFlow.put("totalOutflows", totalOutflows); // Sum of outflows
            cashFlow.put("cashEnd", cashOnHand);          // Ending cash balance

            return cashFlow;

        } catch (Exception e) {
            // Log error and return empty result map if anything fails
            System.err.println("getCashFlow error: " + e.getMessage());
            return new java.util.HashMap<>();
        }
    }



    /**
     * Retrieves a list of completed contracts (jobs) for a given company within a specific game.
     *
     * <p>
     * A completed contract is defined as a job record that:
     *   - Has is_active set to false (i.e., the job is finished)
     *   - Has a period_completed > 0
     *   - Has an accepted bid (b.outcome_code = 1)
     * </p>
     *
     * @param gameId    the unique game identifier
     * @param companyId the unique company identifier
     * @return a list of maps, each representing a completed contract with its details,
     *         or an empty list if none are found or upon error
     */
    public List<Map<String, Object>> getCompletedContracts(int gameId, int companyId) {
        try {
            // Define SQL to select completed job info joined with bid details
            String sql = """
                SELECT j.jobid, j.job_type_id, j.job_lu_size, j.period_awarded,
                    j.period_completed, j.direct_cost, b.bid_amount
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid
                    AND j.companyid = b.companyid
                    AND j.gameid = b.gameid
                    AND b.outcome_code = 1
                WHERE j.gameid = ? AND j.companyid = ?
                AND j.is_active = false AND j.period_completed > 0
                ORDER BY j.period_completed DESC
                """;

            // Execute query and return list of completed jobs for this company/game
            return jdbcTemplate.queryForList(sql, gameId, companyId);
        } catch (Exception e) {
            // Log the error and return empty list on failure
            System.err.println("getCompletedContracts error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }



    /**
     * Retrieves all contracts (jobs) currently in progress for a given company and game.
     *
     * <p>
     * A contract is considered "in progress" if the job:
     *   - Belongs to the specified gameId and companyId
     *   - Is marked as active (j.is_active = true)
     *   - Has an accepted bid (b.outcome_code = 1)
     * </p>
     *
     * The returned data includes key job and bid details for each in-progress contract.
     *
     * @param gameId    The unique identifier for the game session.
     * @param companyId The unique identifier for the company.
     * @return A list of maps, each representing an in-progress contract. If there are no contracts or an error occurs, returns an empty list.
     */
    public List<Map<String, Object>> getContractsInProgress(int gameId, int companyId) {
        try {
            // SQL query to select information for in-progress jobs with accepted bids
            String sql = """
                SELECT j.jobid, j.job_type_id, j.job_lu_size, j.period_awarded,
                    j.estimated_workdays, j.current_method, j.completion_deadline,
                    b.bid_amount
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid
                    AND j.companyid = b.companyid
                    AND j.gameid = b.gameid
                    AND b.outcome_code = 1
                WHERE j.gameid = ? AND j.companyid = ? AND j.is_active = true
                ORDER BY j.jobid ASC
                """;

            // Execute the query and return the resulting list of contracts in progress
            return jdbcTemplate.queryForList(sql, gameId, companyId);
        } catch (Exception e) {
            // Log the error and return an empty list on failure
            System.err.println("getContractsInProgress error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }



    /**
     * Retrieves the income statement data for a company in a specific game.
     *
     * <p>
     * This method gathers financial data needed to build an income statement:
     * <ul>
     *     <li>Current cash on hand for the company</li>
     *     <li>Total construction revenue for all active jobs (spread over periods)</li>
     *     <li>Total payroll/general & administrative expenses for current period</li>
     *     <li>Total loan interest for all outstanding loans (2% per period interest)</li>
     *     <li>Gross profit, earnings from operations, earnings before tax, and net earnings</li>
     *     <li>Retained earnings, start and end of period</li>
     * </ul>
     * </p>
     *
     * @param gameId    The unique game identifier.
     * @param companyId The unique company identifier.
     * @return A map with keys for the above income statement metrics.
     */
    public Map<String, Object> getIncomeStatement(int gameId, int companyId) {
        try {
            // Retrieve the current cash on hand for this company/game
            double cashOnHand = jdbcTemplate.queryForObject(
                "SELECT cash_on_hand FROM public.company WHERE companyid = ? AND gameid = ?",
                Double.class, companyId, gameId);

            // Retrieve revenue-related info from all currently active jobs and successful bids
            List<Map<String, Object>> activeJobs = jdbcTemplate.queryForList("""
                SELECT j.estimated_workdays, j.current_method, b.bid_amount
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid
                    AND j.companyid = b.companyid
                    AND j.gameid = b.gameid
                    AND b.outcome_code = 1
                WHERE j.gameid = ? AND j.companyid = ? AND j.is_active = true
                """, gameId, companyId);

            // Calculate construction revenue for this period, spreading income evenly across job duration
            double constructionRevenue = 0;
            for (Map<String, Object> job : activeJobs) {
                int estimatedWorkdays = ((Number) job.get("estimated_workdays")).intValue();
                int currentMethod = ((Number) job.get("current_method")).intValue();
                double bidAmount = ((Number) job.get("bid_amount")).doubleValue();

                // Apply speed modifier based on method (faster or slower construction)
                double speedModifier = switch (currentMethod) {
                    case 2 -> 0.75;
                    case 3 -> 0.50;
                    case 4 -> 1.25;
                    default -> 1.0;
                };

                // Minimum one period to complete; divide bid across periods
                int periodsToComplete = Math.max(1, (int) Math.round((estimatedWorkdays / 40.0) * speedModifier));
                constructionRevenue += bidAmount / periodsToComplete;
            }

            // Get the current period for payroll expense
            int currentPeriod = jdbcTemplate.queryForObject(
                "SELECT current_period FROM public.game WHERE gameid = ?",
                Integer.class, gameId);

            // Retrieve the personnel/employees active for this company in this period, and their cost
            List<Map<String, Object>> personnel = jdbcTemplate.queryForList("""
                SELECT cp.employee_type_count, ap.employee_cost_per_period
                FROM public.company_personnel cp
                JOIN public.available_personnel ap ON cp.employee_title = ap.employee_title
                    AND ap.gameid = 0 AND ap.adminid = 0
                WHERE cp.gameid = ? AND cp.companyid = ? AND cp.periodid = ?
                AND cp.employee_type_count > 0
                """, gameId, companyId, currentPeriod);

            // Calculate total payroll for this period
            double totalPayroll = 0;
            for (Map<String, Object> p : personnel) {
                int count = ((Number) p.get("employee_type_count")).intValue();
                double cost = ((Number) p.get("employee_cost_per_period")).doubleValue();
                totalPayroll += count * cost;
            }

            // Retrieve all active loans for the company (having balance and not fully paid)
            List<Map<String, Object>> loans = jdbcTemplate.queryForList("""
                SELECT balance FROM public.loan
                WHERE companyid = ? AND gameid = ? AND payments_remaining > 0
                """, companyId, gameId);

            // Total interest expense at 2% per period for each loan balance
            double totalInterest = 0;
            for (Map<String, Object> loan : loans) {
                double balance = ((Number) loan.get("balance")).doubleValue();
                totalInterest += balance * 0.02;
            }

            // Calculate all relevant totals for the income statement
            double grossProfit = constructionRevenue;                // Here, cost of construction is not subtracted, if needed, update accordingly
            double totalGAExpense = totalPayroll;                    // G&A = payroll expense; extend for more later
            double earningsFromOperations = grossProfit - totalGAExpense;    // Subtract G&A from gross profit
            double earningsBeforeTax = earningsFromOperations - totalInterest; // Subtract interest expense
            double netEarnings = earningsBeforeTax;                  // No income tax handling yet
            double retainedEarningsStart = cashOnHand - netEarnings; // Approximated by removing earnings from ending cash
            double retainedEarningsEnd = cashOnHand;                 // End of period cash

            // Compose results in a map for the view/controller
            Map<String, Object> is = new java.util.HashMap<>();
            is.put("constructionRevenue", constructionRevenue);
            is.put("grossProfit", grossProfit);
            is.put("payroll", totalPayroll);
            is.put("totalGAExpense", totalGAExpense);
            is.put("earningsFromOperations", earningsFromOperations);
            is.put("interestExpense", totalInterest);
            is.put("earningsBeforeTax", earningsBeforeTax);
            is.put("netEarnings", netEarnings);
            is.put("retainedEarningsStart", retainedEarningsStart);
            is.put("retainedEarningsEnd", retainedEarningsEnd);

            return is;

        } catch (Exception e) {
            // Log any errors for debugging purposes
            System.err.println("getIncomeStatement error: " + e.getMessage());
            return new java.util.HashMap<>();
        }
    }



    /**
     * Retrieves job cost report data for a given company in a game.
     *
     * <p>
     * This report displays, for every job (contract) the company has received in the current game:
     * <ul>
     *   <li>The job ID and job type</li>
     *   <li>Direct cost estimate for the job</li>
     *   <li>Bid amount for which the company won the job</li>
     *   <li>Whether the job is still active or completed</li>
     *   <li>The period in which the contract was awarded and completed</li>
     * </ul>
     * </p>
     *
     * <p>
     * Only jobs awarded to the company (b.outcome_code = 1) are included.
     * Results are sorted by period awarded and job ID for display in the report.
     * </p>
     *
     * @param gameId    The current game ID
     * @param companyId The company ID
     * @return          A list of maps, each containing job cost report info (one per job)
     */
    public List<Map<String, Object>> getJobCostReport(int gameId, int companyId) {
        try {
            // SQL query joins jobs and awarded bids for the company, selecting cost and bid details
            String sql = """
                SELECT j.jobid, j.job_type_id, j.direct_cost, j.is_active,
                       j.period_awarded, j.period_completed, b.bid_amount
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid
                    AND j.companyid = b.companyid
                    AND j.gameid = b.gameid
                    AND b.outcome_code = 1
                WHERE j.gameid = ? AND j.companyid = ?
                ORDER BY j.period_awarded ASC, j.jobid ASC
                """;
            // Run the query and fetch the results as a list of maps
            return jdbcTemplate.queryForList(sql, gameId, companyId);
        } catch (Exception e) {
            // Log errors to standard error and return an empty list on failure
            System.err.println("getJobCostReport error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }



    /**
     * Calculates and returns key financial ratios for a specific company
     * for the current period in a given game.
     *
     * <p>
     * This method retrieves financial and payroll data and computes important
     * financial ratios such as working capital turns, gross/net profit percentage,
     * debt to net worth, return on net worth/assets, G&A to sales, and coverage over interest.
     * Each ratio is formatted for display or set to "N/A" when input data is lacking.
     * </p>
     *
     * @param gameId    The unique identifier for the game session.
     * @param companyId The unique identifier for the company.
     * @return          A Map containing keys and string-formatted values for each ratio.
     */
    public Map<String, Object> getRatios(int gameId, int companyId) {
        try {
            // Retrieve base financial numbers: cash on hand and work-in-progress (WIP)
            Map<String, Object> company = jdbcTemplate.queryForMap(
                "SELECT cash_on_hand, wip FROM public.company WHERE companyid = ? AND gameid = ?",
                companyId, gameId);

            double cashOnHand = ((Number) company.get("cash_on_hand")).doubleValue();
            double wip = ((Number) company.get("wip")).doubleValue();

            // Get total outstanding loan balances for the company
            List<Map<String, Object>> loans = jdbcTemplate.queryForList("""
                SELECT COALESCE(SUM(balance), 0) as total_balance
                FROM public.loan
                WHERE companyid = ? AND gameid = ? AND payments_remaining > 0
                """, companyId, gameId);
            double totalLoanBalance = ((Number) loans.get(0).get("total_balance")).doubleValue();

            // Get active job contracts: used to estimate revenue for the period
            List<Map<String, Object>> activeJobs = jdbcTemplate.queryForList("""
                SELECT j.estimated_workdays, j.current_method, b.bid_amount
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid
                    AND j.companyid = b.companyid
                    AND j.gameid = b.gameid
                    AND b.outcome_code = 1
                WHERE j.gameid = ? AND j.companyid = ? AND j.is_active = true
                """, gameId, companyId);

            double revenue = 0;
            // Calculate revenue by spreading each job's bid amount evenly over its periods
            for (Map<String, Object> job : activeJobs) {
                int estimatedWorkdays = ((Number) job.get("estimated_workdays")).intValue();
                int currentMethod = ((Number) job.get("current_method")).intValue();
                double bidAmount = ((Number) job.get("bid_amount")).doubleValue();

                // Determine job construction speed modifier (method-based)
                double speedModifier = switch (currentMethod) {
                    case 2 -> 0.75;
                    case 3 -> 0.50;
                    case 4 -> 1.25;
                    default -> 1.0;
                };

                // Calculate number of periods needed to complete this job (min 1 period)
                int periodsToComplete = Math.max(1, (int) Math.round((estimatedWorkdays / 40) * speedModifier));
                revenue += bidAmount / periodsToComplete;
            }

            // Get current period from the game's state
            int currentPeriod = jdbcTemplate.queryForObject(
                "SELECT current_period FROM public.game WHERE gameid = ?",
                Integer.class, gameId);

            // Retrieve payroll information (number and cost of employees for the period)
            List<Map<String, Object>> personnel = jdbcTemplate.queryForList("""
                SELECT cp.employee_type_count, ap.employee_cost_per_period
                FROM public.company_personnel cp
                JOIN public.available_personnel ap ON cp.employee_title = ap.employee_title
                    AND ap.gameid = 0 AND ap.adminid = 0
                WHERE cp.gameid = ? AND cp.companyid = ? AND cp.periodid = ?
                AND cp.employee_type_count > 0
                """, gameId, companyId, currentPeriod);

            double totalPayroll = 0;
            // Sum payroll for all employed personnel
            for (Map<String, Object> p : personnel) {
                int count = ((Number) p.get("employee_type_count")).intValue();
                double cost = ((Number) p.get("employee_cost_per_period")).doubleValue();
                totalPayroll += count * cost;
            }

            // Calculate total interest expense on all currently outstanding loans (2% per period)
            double totalInterest = 0;
            List<Map<String, Object>> loanDetails = jdbcTemplate.queryForList(
                "SELECT balance FROM public.loan WHERE companyid = ? AND gameid = ? AND payments_remaining > 0",
                companyId, gameId);
            for (Map<String, Object> loan : loanDetails) {
                totalInterest += ((Number) loan.get("balance")).doubleValue() * 0.02;
            }

            // Financial values needed for ratios
            double totalAssets = cashOnHand;
            double totalLiabilities = totalLoanBalance;
            double equity = totalAssets - totalLiabilities;
            double grossProfit = revenue; // in this context, gross profit = revenue (cost of construction not modeled here)
            double gaExpense = totalPayroll;
            double earningsFromOps = grossProfit - gaExpense;
            double earningsBeforeTax = earningsFromOps - totalInterest;

            Map<String, Object> ratios = new java.util.HashMap<>();

            // Liquidity Ratios
            // Revenue divided by cash on hand - how many times company "turns" capital per period
            ratios.put("workingCapitalTurns", revenue > 0 && cashOnHand > 0 ?
                String.format("%.2f", revenue / cashOnHand) : "N/A");
            // Cash on hand as a multiple of work-in-progress (WIP)
            ratios.put("workingCapitalToWIP", cashOnHand > 0 && wip > 0 ?
                String.format("%.2f", cashOnHand / wip) : "N/A");
            // Debt divided by equity/net worth
            ratios.put("debtToNetWorth", equity > 0 ?
                String.format("%.2f", totalLiabilities / equity) : "N/A");

            // Operating/Profitability Ratios
            // Gross profit as percent of sales (if input is available)
            ratios.put("grossProfitPct", revenue > 0 ?
                String.format("%.1f%%", (grossProfit / revenue) * 100) : "N/A");
            // Net profit as percent of sales
            ratios.put("netProfitPct", revenue > 0 ?
                String.format("%.1f%%", (earningsBeforeTax / revenue) * 100) : "N/A");
            // Return on Net Worth (Net profit / equity)
            ratios.put("returnOnNetWorth", equity > 0 ?
                String.format("%.1f%%", (earningsBeforeTax / equity) * 100) : "N/A");
            // Return on Total Assets (Net profit / total assets)
            ratios.put("returnOnAssets", totalAssets > 0 ?
                String.format("%.1f%%", (earningsBeforeTax / totalAssets) * 100) : "N/A");

            // Cost & Coverage Ratios
            // G&A expense as a percent of revenue (sales)
            ratios.put("gaToSales", revenue > 0 ?
                String.format("%.1f%%", (gaExpense / revenue) * 100) : "N/A");
            // Earnings coverage over interest expense
            ratios.put("earningsCoverageOverInterest", totalInterest > 0 ?
                String.format("%.2f", earningsFromOps / totalInterest) : "N/A");

            return ratios;

        } catch (Exception e) {
            // Log any error to standard error and return an empty ratios map if there is a problem
            System.err.println("getRatios error: " + e.getMessage());
            return new java.util.HashMap<>();
        }
    }




}