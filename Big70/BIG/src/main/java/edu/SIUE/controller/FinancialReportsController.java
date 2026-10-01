package edu.SIUE.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import edu.SIUE.service.ReportService;
import edu.SIUE.service.GameService;
import edu.SIUE.service.JobService;

import java.util.Map;
import java.util.List;

/*
 * FinancialReportsController.java
 *
 * Controller for managing all financial report-related pages and routes
 * for company users in the BIG simulation platform.
 *
 * Responsibilities:
 *   - Routes and serves all financial reporting endpoints under /company
 *   - Ensures only authenticated company users can access financial reports
 *   - Provides page handlers for balance sheet, income statement,
 *     completed contracts, contracts in progress, job cost report, and
 *     the financial reports menu
 *
 * All entry points in this controller are secured via company login checks.
 * 
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
@RequestMapping("/company")
public class FinancialReportsController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private GameService gameService;

    @Autowired
    private JobService jobService;

    private boolean isCompanyLoggedIn(HttpSession session) {
        return session.getAttribute("companyId") != null;
    }

    // Financial Reports Menu Page
    @GetMapping("/financial-reports-menu")
    public String financialReportsMenu(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/financial-reports/financial-reports-menu";
    }





    /**
     * Handles GET request for the Balance Sheet financial report page.
     *
     * <p>
     * Ensures the user is logged in as a company by checking the session. If the user is not
     * authenticated, this method redirects to the login page. If authenticated, it retrieves:
     * </p>
     * <ul>
     *     <li>The current companyId and gameId from the session</li>
     *     <li>The balance sheet data from the ReportService</li>
     *     <li>The username and current period date for display in the UI</li>
     * </ul>
     *
     * Puts the balance sheet data on the model under the attribute "bs" (for use by Thymeleaf).
     *
     * @param session HttpSession object to access current session attributes (user, company, game IDs)
     * @param model   Spring's Model for passing attributes to the view
     * @return        The Thymeleaf page for the balance sheet if authenticated, else a redirect to login
     */
    @GetMapping("/financial-reports/balance-sheet")
    public String balanceSheet(HttpSession session, Model model) {
        // Redirect to login page if not logged in as a company user
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve companyId and gameId from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Retrieve balance sheet from business logic/service layer
        Map<String, Object> balanceSheet = reportService.getBalanceSheet(gameId, companyId);

        // Add relevant data for display to the model
        model.addAttribute("bs", balanceSheet);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));

        // Return the Thymeleaf template for the balance sheet page
        return "company/financial-reports/balance-sheet";
    }



    

    /**
     * Handles GET request for the Income Statement financial report page.
     *
     * <p>
     * Ensures the user is logged in as a company by checking the session. If the user is not
     * authenticated, this method redirects to the login page. If authenticated, it retrieves:
     * </p>
     * <ul>
     *     <li>The current companyId and gameId from the session</li>
     *     <li>The income statement data from the ReportService</li>
     *     <li>The username and current period date for display in the UI</li>
     * </ul>
     *
     * Puts the income statement data on the model under the attribute "is" (for use by Thymeleaf).
     *
     * @param session HttpSession object to access current session attributes (user, company, game IDs)
     * @param model   Spring's Model for passing attributes to the view
     * @return        The Thymeleaf page for the income statement if authenticated, else a redirect to login
     */
    @GetMapping("/financial-reports/income-statement")
    public String incomeStatement(HttpSession session, Model model) {
        // Redirect to login if not logged in as a company user
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve companyId and gameId from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Retrieve income statement from business logic/service layer
        Map<String, Object> incomeStatement = reportService.getIncomeStatement(gameId, companyId);

        // Add relevant data for display to the model
        model.addAttribute("is", incomeStatement);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        
        // Return the Thymeleaf template for the income statement page
        return "company/financial-reports/income-statement";
    }




    /**
     * Handles GET requests for the Completed Contracts report page.
     * 
     * <p>
     * This endpoint checks authentication for the company user,
     * retrieves the completed contracts for the current company and game,
     * attaches job type names for display, and adds relevant attributes
     * to the model for use by the Thymeleaf template.
     * </p>
     *
     * @param session the HTTP session, containing authentication and user/game context
     * @param model   the Spring model to which to add report data and display attributes
     * @return the name of the Thymeleaf template for completed contracts report,
     *         or redirects to login page if not authenticated
     */
    @GetMapping("/financial-reports/completed-contracts")
    public String completedContracts(HttpSession session, Model model) {
        // Redirect to login page if user is not logged in as a company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve companyId and gameId from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Query completed contracts data from the report service
        List<Map<String, Object>> rawContracts = reportService.getCompletedContracts(gameId, companyId);

        // Attach user-friendly job type names to each contract
        List<Map<String, Object>> contracts = new java.util.ArrayList<>();
        for (Map<String, Object> row : rawContracts) {
            Map<String, Object> contract = new java.util.HashMap<>(row);
            int typeId = ((Number) row.get("job_type_id")).intValue();
            contract.put("job_type_name", jobService.getJobTypeName(typeId));
            contracts.add(contract);
        }

        // Add processed contract list and display data to the model
        model.addAttribute("contracts", contracts);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        // Return Thymeleaf template for completed contracts report
        return "company/financial-reports/completed-contracts";
    }





    /**
     * Handles GET requests for the Contracts In Progress report page.
     * 
     * <p>
     * This endpoint checks the company user's authentication, retrieves all contracts currently in progress,
     * calculates each contract's progress based on the elapsed periods, awarded period, estimated workdays, and
     * current construction method, then provides a list of contracts with display data for use by the Thymeleaf template.
     * </p>
     *
     * @param session the HTTP session containing authentication and user/game context
     * @param model   the Spring model to which report data and display fields are added
     * @return the name of the Thymeleaf template for the contracts in progress report,
     *         or redirects to the login page if not authenticated
     */
    @GetMapping("/financial-reports/contracts-in-progress")
    public String contractsInProgress(HttpSession session, Model model) {
        // Check for valid authentication; redirect if not logged in as company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve the current company and game IDs from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get game data to determine which period is current
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Query contracts in progress for the specified company and game
        List<Map<String, Object>> rawContracts = reportService.getContractsInProgress(gameId, companyId);

        List<Map<String, Object>> contracts = new java.util.ArrayList<>();
        for (Map<String, Object> row : rawContracts) {
            // Deep copy each row for safe modification
            Map<String, Object> contract = new java.util.HashMap<>(row);

            // Attach the job type name for display
            int typeId = ((Number) row.get("job_type_id")).intValue();
            contract.put("job_type_name", jobService.getJobTypeName(typeId));

            // Calculate the progress of this contract
            int estimatedWorkdays = ((Number) row.get("estimated_workdays")).intValue();
            int periodAwarded = ((Number) row.get("period_awarded")).intValue();
            int currentMethod = ((Number) row.get("current_method")).intValue();

            // Calculate speed modifier based on job method
            double speedModifier = switch (currentMethod) {
                case 2 -> 0.75;
                case 3 -> 0.50;
                case 4 -> 1.25;
                default -> 1.0;
            };

            // Calculate how many periods are required to complete the job, with minimum 1 period
            int periodsToComplete = Math.max(1, (int) Math.round((estimatedWorkdays / 40) * speedModifier));
            int periodsWorked = currentPeriod - periodAwarded;

            // Calculate percentage progress, capped at 100%
            double progress = Math.min(100.0, (periodsWorked / (double) periodsToComplete) * 100.0);
            contract.put("progress", String.format("%.1f", progress));

            // Add processed contract to the result list
            contracts.add(contract);
        }

        // Add report data and display fields to the model
        model.addAttribute("contracts", contracts);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        // Return view for contracts in progress report
        return "company/financial-reports/contracts-in-progress";
    }



    

    /**
     * Handles GET requests for the Job Cost Report page.
     * 
     * <p>
     * This endpoint retrieves a detailed job cost report for the currently logged-in company
     * in the current game session. The report displays job details, type names, status, profit/loss,
     * and other relevant information for each contract/job the company has been awarded.
     * </p>
     *
     * @param session the current HTTP session containing user authentication and context info
     * @param model   the Spring Model to pass attributes to the Thymeleaf view
     * @return        the name of the Thymeleaf template to render, or redirect to login if not authenticated
     */
    @GetMapping("/financial-reports/job-cost-report")
    public String jobCostReport(HttpSession session, Model model) {
        // Redirect to login page if user is not authenticated as a company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve company and game IDs from the session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get the raw job cost report data from the report service
        List<Map<String, Object>> rawJobs = reportService.getJobCostReport(gameId, companyId);

        // Prepare a processed list of jobs with computed fields for the view
        List<Map<String, Object>> jobs = new java.util.ArrayList<>();
        for (Map<String, Object> row : rawJobs) {
            // Copy row data for safe modification
            Map<String, Object> job = new java.util.HashMap<>(row);

            // Attach the job type name for display purposes
            int typeId = ((Number) row.get("job_type_id")).intValue();
            job.put("job_type_name", jobService.getJobTypeName(typeId));

            // Calculate the profit or loss for this job
            double bidAmount = ((Number) row.get("bid_amount")).doubleValue();
            double directCost = ((Number) row.get("direct_cost")).doubleValue();
            double profitLoss = bidAmount - directCost;
            job.put("profit_loss", profitLoss);

            // Determine the job status (In Progress or Completed)
            boolean isActive = (boolean) row.get("is_active");
            job.put("status", isActive ? "In Progress" : "Completed");

            // Add the processed job to the result list
            jobs.add(job);
        }

        // Add the list of jobs and user context info to the model for the view
        model.addAttribute("jobs", jobs);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));

        // Return the path to the job cost report Thymeleaf template
        return "company/financial-reports/job-cost-report";
    }





    /**
     * Handles GET requests for the Cash Flow Report page.
     * 
     * <p>
     * This endpoint retrieves the cash flow data for the logged-in company and game,
     * attaches the report data, username, and period date to the model, and
     * returns the corresponding view for rendering the cash flow report.
     * </p>
     *
     * @param session the current HTTP session containing user authentication and context info
     * @param model   the Spring Model to pass attributes to the Thymeleaf view
     * @return the name of the Thymeleaf template to render, or redirect to login if not authenticated
     */
    @GetMapping("/financial-reports/cash-flow-report")
    public String cashFlowReport(HttpSession session, Model model) {
        // Redirect to login page if the company is not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve the current company and game IDs from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get the cash flow report data from the report service
        Map<String, Object> cashFlow = reportService.getCashFlow(gameId, companyId);

        // Add the report data and other context to the model for use in the view
        model.addAttribute("cf", cashFlow);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));

        // Return the path to the cash flow report Thymeleaf template
        return "company/financial-reports/cash-flow-report";
    }



    

    /**
     * Handles GET request for the Ratios Report financial report page.
     *
     * <p>
     * Ensures the user is logged in as a company by checking the session. If not authenticated,
     * redirects to the login page. If authenticated, the current companyId and gameId
     * are retrieved from the session, and ratio data is fetched from the ReportService.
     * The data, along with username and period date for display in the UI, are added to the model.
     * </p>
     *
     * <ul>
     *   <li>Puts the ratios data on the model under "ratios" (for use by Thymeleaf).</li>
     *   <li>Puts the user's username and period date as well.</li>
     * </ul>
     *
     * @param session HttpSession object to access current session attributes (user, company, game IDs)
     * @param model   Spring's Model for passing attributes to the view
     * @return        The Thymeleaf page for the ratios report if authenticated, else a redirect to login
     */
    @GetMapping("/financial-reports/ratios-report")
    public String ratiosReport(HttpSession session, Model model) {
        // Redirect to login page if not logged in as a company user
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve companyId and gameId from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Retrieve the ratios data from the business logic/service layer
        Map<String, Object> ratios = reportService.getRatios(gameId, companyId);

        // Add relevant data for display to the model
        model.addAttribute("ratios", ratios);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        
        // Return the Thymeleaf template for the ratios report page
        return "company/financial-reports/ratios-report";
    }




}