package edu.SIUE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

import edu.SIUE.service.ConsultingService;
import edu.SIUE.service.GameService;

import java.util.List;
import java.util.Map;
/*
 * ConsultingServicesController.java
 *
 * Controller for handling company-facing consulting services pages within the BIG simulation platform.
 *
 * Responsibilities:
 *   - Routes and manages all consulting services-related URLs for company users
 *     (e.g., materials cost index, weather forecast, future demand, etc.)
 *   - Ensures user authentication (company login) before serving any consulting service feature
 *   - Serves menu and report pages under the /company/consulting-services/ namespace
 *
 * All endpoints in this controller are restricted to authenticated company users only.
 *
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
@RequestMapping("/company")
public class ConsultingServicesController {

    @Autowired
    private ConsultingService consultingService;

    @Autowired
    private GameService gameService;

    private boolean isCompanyLoggedIn(HttpSession session) {
        return session.getAttribute("companyId") != null;
    }

    private int getCurrentPeriod(int gameId) {
        Map<String, Object> game = gameService.getGameById(gameId);
        return ((Number) game.get("currentPeriod")).intValue();
    }

    // Consulting Services Menu Page
    @GetMapping("/consulting-services-menu")
    public String consultingServicesMenu(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/consulting-services/consulting-services-menu";
    }




    
    /**
     * Displays the Materials Cost Index consulting service page.
     * Only accessible by logged-in companies.
     * 
     * @param session the HTTP session to check login status
     * @param model the Spring UI Model to inject data for the view
     * @return The view for the materials cost index consulting service
     */
    @GetMapping("/consulting-services/materials-cost-index")
    public String materialsCostIndex(HttpSession session, Model model) {
        // Redirect to login page if user is not logged in as a company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Add the report cost to the model so it can be displayed on the page
        model.addAttribute("reportCost", "$500");
        // Return the materials cost index page view
        return "company/consulting-services/materials-cost-index";
    }

    /**
     * Handles the purchase of a material cost index report for the company.
     * Deducts funds, generates forecast, and displays the generated report.
     * Handles insufficient funds gracefully.
     *
     * @param session the HTTP session holding user and company info
     * @param model the UI model to inject result data and errors
     * @return The generated report view, or the selection screen if not enough funds
     */
    @PostMapping("/consulting-services/purchase-material-cost-report")
    public String purchaseMaterialCostReport(HttpSession session, Model model) {
        // Redirect to login if not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Retrieve the current user's company and game IDs from the session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Attempt to deduct funds to purchase the report
        if (!consultingService.purchaseReport(gameId, companyId)) {
            // Not enough money: set an error and stay on the selection screen
            model.addAttribute("error", "Insufficient funds to purchase this report.");
            return "company/consulting-services/materials-cost-index";
        }

        // Purchase succeeded: get forecast data for the current period
        int currentPeriod = getCurrentPeriod(gameId);
        List<Map<String, Object>> forecast = consultingService.getMaterialsCostIndex(gameId, currentPeriod);
        // Add results to model for display in the generated report view
        model.addAttribute("forecast", forecast);
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("username", session.getAttribute("username"));
        // Show the generated materials cost report
        return "company/consulting-services/generated-material-cost-report";
    }

    /**
     * Handles GET requests directly to the generated material cost report page.
     * This endpoint simply redirects back to the cost index selection to prevent
     * users from accessing report pages without purchasing via POST.
     * 
     * @param session the HTTP session for authentication check
     * @return redirect back to the consulting service menu if not logged in,
     *         otherwise redirect to the selection screen
     */
    @GetMapping("/consulting-services/generated-material-cost-report")
    public String generatedMaterialCostReport(HttpSession session) {
        // Redirect to login if not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Do not allow direct access; redirect to the report selection page
        return "redirect:/company/consulting-services/materials-cost-index";
    }





    /**
     * Displays the weather forecast consulting service page.
     * Shows the cost of purchasing a weather forecast report.
     * 
     * @param session HTTP session for authentication and user data.
     * @param model   Spring MVC model to add attributes for rendering.
     * @return        View name for the weather forecast selection page.
     */
    @GetMapping("/consulting-services/weather-forecast")
    public String weatherForecast(HttpSession session, Model model) {
        // Redirect to login if not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Add report cost to the model for display
        model.addAttribute("reportCost", "$500");
        // Render the weather forecast selection page
        return "company/consulting-services/weather-forecast";
    }

    /**
     * Handles POST request to purchase a weather forecast report.
     * Deducts funds if possible and generates the weather forecast report.
     * 
     * @param session HTTP session for user identification and authentication
     * @param model   Spring MVC model to add attributes for rendering
     * @return        View with generated report if purchase succeeds,
     *                otherwise return to forecast selection with an error
     */
    @PostMapping("/consulting-services/purchase-weather-report")
    public String purchaseWeatherReport(HttpSession session, Model model) {
        // Redirect to login if not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Get current user's company and game IDs from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Attempt to purchase report; check if funds are sufficient
        if (!consultingService.purchaseReport(gameId, companyId)) {
            // Insufficient funds: show error and stay on selection screen
            model.addAttribute("error", "Insufficient funds to purchase this report.");
            return "company/consulting-services/weather-forecast";
        }

        // On successful purchase, generate and show the weather forecast
        int currentPeriod = getCurrentPeriod(gameId);
        List<Map<String, Object>> forecast = consultingService.getWeatherForecast(gameId, currentPeriod);
        // Add forecast data and other info to the model
        model.addAttribute("forecast", forecast);
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("username", session.getAttribute("username"));
        // Render the generated weather report view
        return "company/consulting-services/generated-weather-report";
    }

    /**
     * Blocks direct GET access to the generated weather report.
     * Redirects to the report selection so reports must be POST-purchased.
     * 
     * @param session HTTP session for authentication
     * @return        Redirect to forecast selection, or login if not authenticated
     */
    @GetMapping("/consulting-services/generated-weather-report")
    public String generatedWeatherReport(HttpSession session) {
        // Redirect to login if not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Block direct access and redirect to weather forecast selection
        return "redirect:/company/consulting-services/weather-forecast";
    }




    
    /**
     * Displays the future demand forecasting consulting service selection screen.
     * Shows the report cost and ensures the company is authenticated before allowing access.
     *
     * @param session The HTTP session for identifying the company/user.
     * @param model   Spring MVC model to add attributes for the view.
     * @return        The view for selecting and purchasing a future demand report,
     *                or redirects to login if not authenticated.
     */
    @GetMapping("/consulting-services/future-demand")
    public String futureDemand(HttpSession session, Model model) {
        // Redirect unauthenticated users to the login page
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Set the report cost to display on the selection screen
        model.addAttribute("reportCost", "$500");
        // Render the future demand consulting service view
        return "company/consulting-services/future-demand";
    }

    /**
     * Handles the purchasing of a future demand consulting report.
     * Deducts the cost if funds are sufficient and renders the generated report.
     * If insufficient funds, shows an error on the selection page instead.
     *
     * @param session HTTP session to get company/game IDs and authentication
     * @param model   Model for passing data to the view
     * @return        The generated future demand report view on success,
     *                or the selection view with an error on insufficient funds,
     *                or redirect to login if unauthenticated.
     */
    @PostMapping("/consulting-services/purchase-future-demand")
    public String purchaseFutureDemand(HttpSession session, Model model) {
        // Ensure the user is authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Retrieve company and game identifiers from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Attempt to purchase the report, check for sufficient funds
        if (!consultingService.purchaseReport(gameId, companyId)) {
            // Not enough funds, show error, keep user on future demand selection
            model.addAttribute("error", "Insufficient funds to purchase this report.");
            return "company/consulting-services/future-demand";
        }

        // On successful purchase, fetch generated future demand forecast
        int currentPeriod = getCurrentPeriod(gameId);
        List<Map<String, Object>> forecast = consultingService.getFutureDemand(gameId, currentPeriod);
        // Pass information to the view for display
        model.addAttribute("forecast", forecast);
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("username", session.getAttribute("username"));
        // Show the generated future demand report
        return "company/consulting-services/generated-future-demand";
    }

    /**
     * Blocks direct GET access to the generated future demand report page.
     * Reports must be POST-purchased. Redirect GET access back to the selection page.
     *
     * @param session HTTP session for authentication
     * @return        Redirect to report selection, or login page if not authenticated.
     */
    @GetMapping("/consulting-services/generated-future-demand")
    public String generatedFutureDemand(HttpSession session) {
        // Only allow access if company is logged in; otherwise redirect to login
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Redirect direct GET access back to selection page instead of report view
        return "redirect:/company/consulting-services/future-demand";
    }





    /**
     * Displays the appraisal metrics consultation purchase page.
     * Shows the cost of the report and allows a company to initiate the purchase.
     *
     * @param session HTTP session to check authentication
     * @param model   Model for passing attributes to the view
     * @return        The appraisal metrics purchase page, or redirect to login if unauthenticated.
     */
    @GetMapping("/consulting-services/appraisal-metrics")
    public String appraisalMetrics(HttpSession session, Model model) {
        // Ensure the user is logged in as a company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Add the standard report cost to the model for display
        model.addAttribute("reportCost", "$500");
        return "company/consulting-services/appraisal-metrics";
    }

    /**
     * Handles the POST request to purchase an appraisal metrics report.
     * Deducts cost, checks for sufficient funds, and generates the report if successful.
     *
     * @param session HTTP session for authentication and company info
     * @param model   Model for attributes to be shown in the view
     * @return        The generated appraisal metrics report if purchased, 
     *                or back to purchase page with error if funds insufficient,
     *                or redirect to login if not authenticated.
     */
    @PostMapping("/consulting-services/purchase-appraisal-metrics")
    public String purchaseAppraisalMetrics(HttpSession session, Model model) {
        // Only allow authenticated company users
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Get company and game IDs from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Ensure the company has enough funds to purchase report
        if (!consultingService.purchaseReport(gameId, companyId)) {
            // Not enough money, show error on same page
            model.addAttribute("error", "Insufficient funds to purchase this report.");
            return "company/consulting-services/appraisal-metrics";
        }

        // Get report for current period after successful purchase
        int currentPeriod = getCurrentPeriod(gameId);
        Map<String, Object> metrics = consultingService.getAppraisalMetrics(companyId, currentPeriod);

        // Add report and additional info to model for display
        model.addAttribute("metrics", metrics);
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("username", session.getAttribute("username"));

        // Show the generated report
        return "company/consulting-services/generated-appraisal-metrics-report";
    }

    /**
     * Blocks direct GET access to the generated appraisal metrics report.
     * Users must POST-purchase the report to view.
     * Redirects to the purchase/appraisal selection page if accessed directly.
     *
     * @param session HTTP session to check authentication
     * @return        Redirect to selection/purchase page, or login if not authenticated.
     */
    @GetMapping("/consulting-services/generated-appraisal-metrics-report")
    public String generatedAppraisalMetricsReport(HttpSession session) {
        // Ensure only companies who POST-purchased can access; others redirected
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "redirect:/company/consulting-services/appraisal-metrics";
    }




    
    /**
     * Displays the labor availability consulting report purchase page.
     * Only accessible to authenticated company users.
     *
     * @param session HTTP session for authentication checks.
     * @param model   Spring model for adding attributes to the view.
     * @return        The labor availability purchase page, or redirect to login if not authenticated.
     */
    @GetMapping("/consulting-services/labor-availability")
    public String laborAvailability(HttpSession session, Model model) {
        // Redirect to login if user is not an authenticated company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Add the report cost to the model for display on the page
        model.addAttribute("reportCost", "$500");
        // Return the labor availability purchase view
        return "company/consulting-services/labor-availability";
    }

    /**
     * Handles POST request to purchase the labor availability report.
     * Checks for funds, deducts cost, and displays generated report to company users.
     *
     * @param session HTTP session for authentication and user info.
     * @param model   Spring model for passing data to the view.
     * @return        The generated labor report if purchase succeeds;
     *                the purchase page with error if funds insufficient or not authenticated.
     */
    @PostMapping("/consulting-services/purchase-labor-report")
    public String purchaseLaborReport(HttpSession session, Model model) {
        // Redirect to login if user is not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve company and game IDs from session attributes
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Attempt to purchase report. If funds insufficient, show error and redisplay purchase page
        if (!consultingService.purchaseReport(gameId, companyId)) {
            model.addAttribute("error", "Insufficient funds to purchase this report.");
            return "company/consulting-services/labor-availability";
        }

        // On successful purchase, get the current period for the game
        int currentPeriod = getCurrentPeriod(gameId);
        // Get labor availability forecast data for this period
        List<Map<String, Object>> forecast = consultingService.getLaborAvailability(gameId, currentPeriod);

        // Add forecast, period date, and username to the model for rendering in the report view
        model.addAttribute("forecast", forecast);
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("username", session.getAttribute("username"));

        // Show the generated labor availability report
        return "company/consulting-services/generated-labor-report";
    }

    /**
     * Blocks direct GET access to the generated labor report.
     * Users must POST-purchase the report to view.
     * Redirects to the purchase page if accessed directly.
     *
     * @param session HTTP session to check authentication
     * @return        Redirect to labor availability purchase page or to login if not authenticated.
     */
    @GetMapping("/consulting-services/generated-labor-report")
    public String generatedLaborReport(HttpSession session) {
        // Ensure only authenticated companies can access; otherwise redirect to login
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Otherwise redirect to purchase/selection page
        return "redirect:/company/consulting-services/labor-availability";
    }





}