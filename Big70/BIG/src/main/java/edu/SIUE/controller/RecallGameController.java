package edu.SIUE.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.List;

import edu.SIUE.service.GameService;
import edu.SIUE.service.AdminAuthService;
import edu.SIUE.service.CompanyService;
import edu.SIUE.service.JobService;
import edu.SIUE.service.BidService;
import edu.SIUE.service.LoanService;
import edu.SIUE.service.PersonnelService;
import edu.SIUE.service.JobFactoryService;

/*
 * RecallGameController.java
 *
 * Controller responsible for administering the Recall Game features within the BIG simulation platform.
 *
 * Responsibilities:
 *   - Handles all HTTP requests for managing and operating Recall Game sessions via the admin interface.
 *   - Secures endpoints to allow only authenticated admin users to access recall game controls.
 *   - Routes requests to start, configure, or inspect Recall Game instances.
 *   - Integrates with relevant services (GameService, CompanyService, JobService, BidService) for business logic.
 *
 * Endpoints provided by this controller are all under the /admin/ URL namespace.
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
@RequestMapping("/admin")
public class RecallGameController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private AdminAuthService adminAuthService;

    @Autowired
    private GameService gameService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobFactoryService jobFactoryService;

    @Autowired
    private BidService bidService;

    @Autowired
    private PersonnelService personnelService;

    /**
     * Checks whether an admin is currently logged in by verifying
     * the presence of "adminId" in the session.
     */
    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("adminId") != null;
    }

    /**
     * Handles GET request for Recall Game Menu page.
     * <p>
     * If an admin is not logged in, redirects to the login page.
     * If a game ID parameter is provided, it updates the current game in the session.
     * If no current game is selected in session, redirects to the games list page.
     * Fetches the game info from GameService and makes it available in the model for the Thymeleaf template.
     * Renders the recall game menu view.
     *
     * @param id      Optional game ID to set as current in the session
     * @param session HttpSession containing admin and game state
     * @param model   Spring Model to hold game information for the view
     * @return The view name to render, or a redirect if any preconditions fail
     */
    @GetMapping("/recall-menu")
    public String recallGameMenu(@RequestParam(required = false) Integer id,
                                 HttpSession session,
                                 Model model) {
        // Check if the admin is logged in; redirect to login page if not
        if (session.getAttribute("adminId") == null) {
            return "redirect:/login";
        }

        // If a game ID is provided as a request parameter, set it in the session
        if (id != null) {
            session.setAttribute("currentGameId", id);
        }

        // Get the current game ID from session
        Integer currentGameId = (Integer) session.getAttribute("currentGameId");
        if (currentGameId == null) {
            // Redirect to view-games if no game is selected
            return "redirect:/admin/view-games";
        }

        // Retrieve the game information for the current game
        Map<String, Object> game = gameService.getGameById(currentGameId);
        if (game == null) {
            // Redirect to view-games page if the game is not found
            return "redirect:/admin/view-games";
        }

        // Add the retrieved game object to the model for display in the template
        model.addAttribute("game", game);

        // Return the view name for the recall game menu
        return "admin/recall-game/recall-menu";
    }





    /**
     * Handles the GET request for the Add New Company form.
     * Checks if admin is logged in; if not, redirects to login page.
     * Otherwise, renders the add-new-company page.
     *
     * @param session the current HTTP session
     * @return the view name for the add new company page or redirect to login
     */
    @GetMapping("/add-new-company")
    public String addNewCompany(HttpSession session) {
        // Ensure admin is logged in; otherwise redirect to login
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }
        // Render the add new company form view
        return "admin/recall-game/add-new-company";
    }

    /**
     * Handles the POST submission for adding a new company to the current game.
     * Accepts company information and attempts to register via CompanyService.
     * Performs various checks such as login status, matching passwords, and valid game selection.
     * Adds errors to the model as needed to display messages on the form.
     * On success, redirects to the recall game menu.
     */
    @PostMapping("/add-new-company")
    public String addNewCompany(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String confirmPassword,
                                @RequestParam String title,
                                @RequestParam String cashOnHandString,
                                @RequestParam String WIPLimitString,
                                @RequestParam String perJobLimitAsWIPPercentString,
                                HttpSession session,
                                Model model) {

        // Check if admin is logged in
        if (session.getAttribute("adminId") == null) {
            return "redirect:/login";
        }

        // Get current game ID from session
        Integer gameId = (Integer) session.getAttribute("currentGameId");
        if (gameId == null) {
            return "redirect:/admin/view-games";
        }

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "admin/recall-game/add-new-company";
        }

        // Parse currency and percent strings (removing $/comma for input flexibility)
        long cashOnHand = Long.parseLong(cashOnHandString.replaceAll("[^0-9]", ""));
        long wipLimit = Long.parseLong(WIPLimitString.replaceAll("[^0-9]", ""));
        int perJobLimitAsWIPPercent = Integer.parseInt(perJobLimitAsWIPPercentString.replaceAll("[^0-9]", ""));

        // Attempt to register the company via the service
        int adminId = (int) session.getAttribute("adminId");
        boolean success = companyService.registerCompany(
            gameId, adminId, username.toLowerCase(), password, title, cashOnHand, wipLimit, perJobLimitAsWIPPercent
        );

        // Handle registration failure (likely duplicate username)
        if (!success) {
            model.addAttribute("error", "Failed to register company. Username may already exist.");
            return "admin/recall-game/add-new-company";
        }

        // On success, return to the recall menu
        return "redirect:/admin/recall-menu";
    }





    /**
     * Handles GET request to view all companies for the game's current session.
     * Ensures admin is logged in. Queries for companies and attaches to the model.
     * Renders the companies view.
     */
    @GetMapping("/view-companies")
    public String viewCompanies(HttpSession session, Model model) {
        // Check login
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }

        // Get game ID
        Integer gameId = (Integer) session.getAttribute("currentGameId");
        if (gameId == null) {
            return "redirect:/admin/view-games";
        }

        // Get companies for the game and add to model
        List<Map<String, Object>> companies = companyService.getCompaniesByGame(gameId);
        List<Map<String, Object>> enrichedCompanies = new java.util.ArrayList<>();
        for (Map<String, Object> company : companies) {
            Map<String, Object> c = new java.util.HashMap<>(company);
            int cId = ((Number) company.get("companyid")).intValue();

            int jobsWon = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.job WHERE gameid = ? AND companyid = ?",
                Integer.class, gameId, cId);

            int jobsCompleted = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.job WHERE gameid = ? AND companyid = ? AND is_active = false AND period_completed > 0",
                Integer.class, gameId, cId);

            int totalBids = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.bid WHERE gameid = ? AND companyid = ?",
                Integer.class, gameId, cId);

            c.put("jobsWon", jobsWon);
            c.put("jobsCompleted", jobsCompleted);
            c.put("totalBids", totalBids);
            enrichedCompanies.add(c);
        }

        model.addAttribute("companies", enrichedCompanies);


        // Render companies list view
        return "admin/recall-game/view-companies";
    }

    /**
     * Handles POST request to delete a company from the game.
     * Checks that the admin is logged in and game ID is present in session.
     * Delegates deletion to the CompanyService. Redirects to view companies upon completion.
     *
     * @param companyId The ID of the company to delete.
     * @param session HttpSession to verify admin authentication and current game.
     * @return Redirects to the company listing or login/view-games as appropriate.
     */
    @PostMapping("/delete-company")
    public String deleteCompany(@RequestParam int companyId, HttpSession session) {
        // Ensure admin is logged in
        if (session.getAttribute("adminId") == null)
            return "redirect:/login";

        // Ensure a current game is selected
        Integer gameId = (Integer) session.getAttribute("currentGameId");
        if (gameId == null)
            return "redirect:/admin/view-games";

        // Delete the company from the current game
        companyService.deleteCompany(gameId, companyId);

        // Redirect to the refreshed company list
        return "redirect:/admin/view-companies";
    }





    // Game Parameters
    @GetMapping("/game-parameters")
    public String gameParameters(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        return "admin/recall-game/game-parameters";
    }

    // Game Parameters - Building Construction Activities
    @GetMapping("/game-parameters/building-construction")
    public String gameParametersBuildingConstruction(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        return "admin/recall-game/game-parameters-building-construction";
    }

    // Game Parameters - Heavy Construction Activities
    @GetMapping("/game-parameters/heavy-construction")
    public String gameParametersHeavyConstruction(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        return "admin/recall-game/game-parameters-heavy-construction";
    }

    // Game Components
    @GetMapping("/game-components")
    public String gameComponents(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        return "admin/recall-game/game-components";
    }

    // Auto Add Building Jobs
    @GetMapping("/auto-add-building-jobs")
    public String autoAddBuildingJobs(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        return "admin/recall-game/auto-add-building-jobs";
    }

    // Grading Template
    @GetMapping("/grading-template")
    public String gradingTemplate(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        return "admin/recall-game/grading-template";
    }

    // View Automatic Update Policy
    @GetMapping("/view-automatic-update-policy")
    public String viewAutomaticUpdatePolicy(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        return "admin/recall-game/view-automatic-update-policy";
    }

    // View Period Reports
    @GetMapping("/period-reports")
    public String periodReports(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        return "admin/recall-game/period-reports";
    }



    

    /**
     * Handles GET request for the Advance Period page.
     * Checks if admin is logged in; if not, redirects to the login page.
     * If logged in, returns the advance period view for confirmation.
     *
     * @param session the current HTTP session to check admin authentication
     * @return view name or redirect path
     */
    @GetMapping("/advance-period")
    public String advancePeriodWarning(HttpSession session) {
        // Only allow if admin is logged in
        if (!isAdminLoggedIn(session)) return "redirect:/login";
        // Render the advance period confirmation page
        return "admin/recall-game/advance-period";
    }

    /**
     * Handles POST request to actually advance the game period.
     * - Checks admin authentication.
     * - Validates that a current game is selected in the session.
     * - Retrieves current game and period from the database.
     * - Increments period counter.
     * - Calls jobService to generate jobs for the new period.
     * - Updates the current period in the database.
     * - Redirects back to the recall game menu.
     *
     * @param session the HTTP session containing admin and current game context
     * @return redirect path on completion or error
     */
   @PostMapping("/advance-period")
    public String advancePeriod(HttpSession session) {
        if (!isAdminLoggedIn(session)) return "redirect:/login";

        Integer gameId = (Integer) session.getAttribute("currentGameId");
        if (gameId == null) return "redirect:/admin/view-games";

        Map<String, Object> game = gameService.getGameById(gameId);
        if (game == null) return "redirect:/admin/view-games";

        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();
        int adminId = (int) session.getAttribute("adminId");
        int newPeriod = currentPeriod + 1;

        // Evaluate bids FIRST before anything else
        bidService.evaluateBids(gameId);

        loanService.processLoanRepayments(gameId);

        jobFactoryService.processJobIncome(gameId);

        personnelService.processPayroll(gameId, currentPeriod);

        // Mark expired unclaimed jobs as inactive AFTER bids evaluated
        jdbcTemplate.update("""
            UPDATE public.job 
            SET is_active = false 
            WHERE gameid = ? 
            AND companyid IS NULL 
            AND completion_deadline < CURRENT_DATE
            """, gameId);

        // Generate new jobs for the new period
        jobService.generateJobsForPeriod(gameId, adminId, newPeriod);

        // Advance the period last
        jdbcTemplate.update(
            "UPDATE public.game SET current_period = ? WHERE gameid = ?",
            newPeriod, gameId);

        return "redirect:/admin/recall-menu";
    }




    
    /**
     * Handles GET requests to the end-game page.
     * Checks if an admin is logged in; if not, redirects to login.
     * Retrieves current game from the session.
     * If no current game, redirects to view-games.
     * If game exists, adds game info to model for display on end-game confirmation page.
     */
    @GetMapping("/end-game")
    public String showEndGame(HttpSession session, Model model) {

        // Check if admin is logged in
        if (session.getAttribute("adminId") == null) {
            return "redirect:/login";
        }

        // Get the current game ID from the session
        Integer currentGameId = (Integer) session.getAttribute("currentGameId");
        if (currentGameId == null) return "redirect:/admin/view-games";

        // Retrieve game information and add it to the model
        Map<String, Object> game = gameService.getGameById(currentGameId);
        model.addAttribute("game", game);

        // Render the end-game confirmation page
        return "admin/recall-game/end-game";
    }

    /**
     * Handles POST requests to end (delete) the current game.
     * Checks if admin is logged in; if not, redirects to login.
     * If no current game, redirects to view-games.
     * Otherwise, deletes the current game and removes it from the session.
     */
    @PostMapping("/end-game")
    public String processEndGame(HttpSession session, Model model) {

        // Get the current game ID from the session
        Integer currentGameId = (Integer) session.getAttribute("currentGameId");

        // If no current game is selected, redirect to the view games page
        if (currentGameId == null) {
            return "redirect:/admin/view-games";
        }

        // Prevent accidental deletion of the default game (ID 0)
        if (currentGameId == 0) {
            model.addAttribute("error", "Default game cannot be deleted.");
            return "redirect:/admin/view-games";
        }

        // Get the admin ID from the session (should always exist at this point)
        int adminId = (int) session.getAttribute("adminId");

        // Delete the game using the service
        gameService.deleteGame(adminId, currentGameId);

        // Remove the currentGameId from the session after deletion
        session.removeAttribute("currentGameId");

        // Redirect to the list of games after ending the game
        return "redirect:/admin/view-games";
    }
}