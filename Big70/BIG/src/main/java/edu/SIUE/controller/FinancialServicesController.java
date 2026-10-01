package edu.SIUE.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import edu.SIUE.service.LoanService;
import edu.SIUE.service.GameService;
import edu.SIUE.service.CompanyService;
import edu.SIUE.service.PersonnelService;

import java.util.List;
import java.util.Map;

/*
 * FinancialServicesController.java
 *
 * Controller for handling company-facing financial services features within the BIG simulation platform.
 *
 * Responsibilities:
 *   - Routes and manages all financial services-related pages for company users
 *     (e.g., requesting loans, managing personnel, viewing personnel reports, etc.)
 *   - Ensures only authenticated company users can access financial services entry points
 *   - Serves menu and management/report pages under the /company/financial-services/ namespace
 *
 * All methods in this controller perform a company login check before granting access.
 *
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
@RequestMapping("/company")
public class FinancialServicesController {

    @Autowired
    private PersonnelService personnelService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private GameService gameService;

    @Autowired
    private CompanyService companyService;

    private boolean isCompanyLoggedIn(HttpSession session) {
        return session.getAttribute("companyId") != null;
    }

    // Financial Services Menu Page
    @GetMapping("/financial-services-menu")
    public String financialServicesMenu(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/financial-services/financial-services-menu";
    }





    /**
     * Handles GET request for the Request a Loan page.
     * - Checks if a company user is logged in via the session.
     * - Retrieves company and game IDs from the session.
     * - Fetches the company's active loans and current cash-on-hand.
     * - Adds the active loan list, cash value, and loan count to the model.
     * - Returns the Thymeleaf view for the loan request form page.
     *
     * @param session HTTP session object for authentication and state
     * @param model Spring model to add attributes for view rendering
     * @return the view name for the Request a Loan page, or redirect to login if unauthenticated
     */
    @GetMapping("/financial-services/request-a-loan")
    public String requestALoan(HttpSession session, Model model) {
        // Redirect to login if not authenticated as a company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve company and game IDs from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get active loans and current company info
        List<Map<String, Object>> activeLoans = loanService.getActiveLoans(gameId, companyId);
        Map<String, Object> company = companyService.getCompanyById(companyId, gameId);

        // Populate the model with loan information and cash-on-hand
        model.addAttribute("activeLoans", activeLoans);
        model.addAttribute("cashOnHand", company.get("cash_on_hand"));
        model.addAttribute("activeLoanCount", activeLoans.size());

        return "company/financial-services/request-a-loan";
    }

    /**
     * Handles POST request to submit a new loan request.
     * - Verifies company user is logged in.
     * - Parses the loan request amount (removing symbols/letters).
     * - If parsing fails, re-renders form with an error message.
     * - Checks validation/business rules by calling loanService.requestLoan().
     * - On error, redisplays form with appropriate message and loan list.
     * - On success, redirects to the GET loan page with a success parameter.
     *
     * @param loanAmount The (possibly formatted) loan amount string from the request
     * @param session User session (used to extract company and game context)
     * @param model Spring model to add error/loan info (if any)
     * @return Redirects to loan form on error, or reloads on success
     */
    @PostMapping("/financial-services/request-a-loan")
    public String submitLoanRequest(@RequestParam String loanAmount,
                                    HttpSession session,
                                    Model model) {
        // Ensure only logged-in companies can request a loan
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        double amount;
        try {
            // Parse the numeric value from the input string, stripping symbols
            amount = Double.parseDouble(loanAmount.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            // On parse error, show the form again with an error and current loans
            model.addAttribute("error", "Please enter a valid loan amount.");
            model.addAttribute("activeLoans", loanService.getActiveLoans(gameId, companyId));
            return "company/financial-services/request-a-loan";
        }

        // Look up current period via game object
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Attempt to submit loan request; returns error string if it fails validation
        String error = loanService.requestLoan(gameId, companyId, currentPeriod, amount);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("activeLoans", loanService.getActiveLoans(gameId, companyId));
            return "company/financial-services/request-a-loan";
        }

        // On success, redirect with ?success=true to enable "submitted" UI message
        return "redirect:/company/financial-services/request-a-loan?success=true";
    }


    



    /**
     * Handles GET requests for the Manage Personnel page.
     * 
     * - Checks if a company is logged in. If not, redirects to login page.
     * - Retrieves the current company and game IDs from the session.
     * - Looks up the current game object and current period from the game service.
     * - Retrieves the available personnel types from the personnel service.
     * - Gets the current personnel counts for this company and period.
     * - Merges the personnel type information with the current counts.
     * - Adds the combined personnel data to the model.
     * - Returns the view for managing personnel.
     *
     * @param session the current HTTP session
     * @param model the Spring model to add attributes to
     * @return the name of the view to render, or a redirect to login if not authenticated
     */
    @GetMapping("/financial-services/manage-personnel")
    public String managePersonnel(HttpSession session, Model model) {
        // Ensure only logged-in companies can view/manage personnel
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve company and game context from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get current game information and period
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Fetch all available personnel types/roles
        List<Map<String, Object>> availablePersonnel = personnelService.getAvailablePersonnel();

        // Get the number of each personnel currently employed by the company
        Map<String, Integer> currentCounts = personnelService.getCompanyPersonnelCounts(gameId, companyId, currentPeriod);

        // Combine personnel type info with the current count per type for this company
        List<Map<String, Object>> personnel = new java.util.ArrayList<>();
        for (Map<String, Object> row : availablePersonnel) {
            Map<String, Object> p = new java.util.HashMap<>(row);
            String title = (String) row.get("employee_title");
            p.put("current_count", currentCounts.getOrDefault(title, 0));
            personnel.add(p);
        }

        // Add personnel information to the model for the view
        model.addAttribute("personnel", personnel);

        // Render the manage personnel page
        return "company/financial-services/manage-personnel";
    }

    /**
     * Handles POST requests for updating personnel management actions.
     *
     * <p>
     * - Ensures the company is logged in; redirects to login if not authenticated.
     * - Retrieves company and game context from the session.
     * - Obtains the current game and period information.
     * - Parses form data to collect hiring/firing changes requested for each personnel type.
     * - Calls the personnel service to apply the requested updates.
     * - If an error occurs (e.g., invalid hire/fire), repopulates the personnel list and shows error on the same page.
     * - On success, redirects to the personnel management page with a success message.
     * </p>
     *
     * @param request the HTTP servlet request containing personnel form data
     * @param session the current HTTP session
     * @param model the Spring model for returning attributes to the view
     * @return the name of the view to render, or a redirect URL
     */
    @PostMapping("/financial-services/manage-personnel")
    public String updatePersonnel(HttpServletRequest request,
                                 HttpSession session,
                                 Model model) {
        // Check if the company user is logged in; redirect to login page if not
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve company and game IDs from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get current game info, specifically the current period number
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Parse hire/fire personnel changes from submitted form parameters
        Map<String, Integer> changes = new java.util.HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (key.startsWith("hire_")) {
                String title = key.substring(5).replace("_", " ");
                try {
                    int change = Integer.parseInt(values[0]);
                    if (change != 0) changes.put(title, change);
                } catch (NumberFormatException ignored) {
                    // Ignore invalid numbers
                }
            }
        });

        // Attempt to update personnel based on submitted changes
        String error = personnelService.updatePersonnel(gameId, companyId, currentPeriod, changes);
        if (error != null) {
            // If there was an error, add error message and repopulate personnel info for the view
            model.addAttribute("error", error);

            List<Map<String, Object>> availablePersonnel = personnelService.getAvailablePersonnel();
            Map<String, Integer> currentCounts = personnelService.getCompanyPersonnelCounts(gameId, companyId, currentPeriod);

            // Merge personnel type info with current personnel counts
            List<Map<String, Object>> personnel = new java.util.ArrayList<>();
            for (Map<String, Object> row : availablePersonnel) {
                Map<String, Object> p = new java.util.HashMap<>(row);
                String title = (String) row.get("employee_title");
                p.put("current_count", currentCounts.getOrDefault(title, 0));
                personnel.add(p);
            }
            model.addAttribute("personnel", personnel);

            // Stay on manage personnel page with error displayed
            return "company/financial-services/manage-personnel";
        }

        // On successful update, redirect to personnel management page with success indicator
        return "redirect:/company/financial-services/manage-personnel?success=true";
    }





    /**
     * Handles GET requests for viewing the personnel report page for a company.
     * Requires the company to be logged in.
     *
     * @param session the current HTTP session, used to retrieve company and user attributes
     * @param model the model holding attributes for rendering the view
     * @return the view template name for the personnel report, or redirect to login if not authenticated
     */
    @GetMapping("/financial-services/view-personnel-report")
    public String viewPersonnelReport(HttpSession session, Model model) {
        // Check if the company user is logged in; redirect to login page if not
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve company and game IDs from the current session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Fetch game info and determine the current period number
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Get personnel report data for the company and current period
        List<Map<String, Object>> report = personnelService.getPersonnelReport(gameId, companyId, currentPeriod);

        // Add report data and other relevant attributes to the model
        model.addAttribute("report", report);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("currentPeriod", currentPeriod);

        // Return the view template for displaying the personnel report
        return "company/financial-services/view-personnel-report";
    }




}