package edu.SIUE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.SIUE.service.CompanyService;
import edu.SIUE.service.GameService;
import edu.SIUE.service.GradingService;

import java.util.List;
import java.util.Map;

/*
 * CompanyController.java
 *
 * Controller for handling company user operations and exclusive pages in the BIG simulation platform.
 *
 * Responsibilities:
 *   - Routes and manages all company-only URLs (base path: /company)
 *   - Provides authentication guard for company user pages
 *   - Serves company dashboard (menu), rank/score, and other company-internal views
 *   - Interfaces with CompanyService and GameService for retrieving business logic and database information
 *   - Ensures only authenticated company users (via HttpSession) can access company functions
 *
 * All methods in this class must check for company login before providing access, redirecting
 * unauthorized users to the login page.
 *
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private GameService gameService;

    @Autowired
    private GradingService gradingService;


    private boolean isCompanyLoggedIn(HttpSession session) {
        return session.getAttribute("companyId") != null;
    }

    /**
     * Handles the GET request for the main company menu page.
     * Checks if the user is authenticated (companyId and gameId exist in session).
     * If not authenticated, redirects to the login page.
     * Otherwise, retrieves company information and adds it to the model for rendering.
     *
     * @param session HttpSession object to access session attributes (companyId, companyGameId)
     * @param model   Spring Model to pass attributes to the Thymeleaf view
     * @return        The company menu page if authenticated, otherwise a redirect to login
     */
    @GetMapping("/company-menu")
    public String companyMenu(HttpSession session, Model model) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
        Map<String, Object> game = gameService.getGameById(gameId);

        model.addAttribute("currentPeriod", ((Number) game.get("currentPeriod")).intValue());
        model.addAttribute("company", company);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));


        return "company/company-menu";
    }





    /**
     * Handles GET request for the "Rank and Score" page for a company.
     * 
     * Checks if the company user is authenticated using the session. If not authenticated,
     * redirects to the login page. If authenticated, retrieves the company's scores and ranks in
     * the four grading categories (as determined by GradingService), adds them to the model
     * along with the user's username, and returns the Thymeleaf template for the page.
     *
     * @param session HttpSession for retrieving authentication and relevant IDs (companyId, companyGameId)
     * @param model   Spring Model to pass attributes to the Thymeleaf view
     * @return        The "rank-and-score" Thymeleaf template if authenticated, otherwise a redirect to login
     */
    @GetMapping("/rank-and-score")
    public String rankAndScore(HttpSession session, Model model) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Retrieve scores/rankings for this company in the current game context
        List<Map<String, Object>> scores = gradingService.getCompanyScores(gameId, companyId);

        // Add the score breakdown and username to the model for the view
        model.addAttribute("scores", scores);
        model.addAttribute("username", session.getAttribute("username"));

        return "company/rank-and-score";
    }





   /**
    * Handles GET request for the "Change Company Info" page.
    * - Checks if the company user is logged in by inspecting the session.
    * - If not logged in, redirects to the login page.
    * - Retrieves current company information and adds it to the model so it can be shown in the form.
    *
    * @param session HttpSession for authentication and storing IDs
    * @param model   Model to pass company info to the view
    * @return        The "change-company-info" Thymeleaf page or redirect if not authenticated
    */
   @GetMapping("/change-company-info")
   public String changeCompanyInfo(HttpSession session, Model model) {
       if (!isCompanyLoggedIn(session)) return "redirect:/login";

       Integer companyId = (Integer) session.getAttribute("companyId");
       Integer gameId = (Integer) session.getAttribute("companyGameId");

       Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
       model.addAttribute("company", company);
       return "company/change-company-info";
   }

   /**
    * Handles POST request to update company information (title/password).
    * - Checks if the company user is authenticated.
    * - Validates that password and confirmPassword fields match, if a new password is provided.
    * - Delegates to CompanyService for actual update.
    * - On error or mismatched passwords, reloads the form view with error message.
    * - On success, redirects to the company menu page.
    *
    * @param password         The new password (optional)
    * @param confirmPassword  The retyped password for confirmation (optional)
    * @param title            The new title for the company (required)
    * @param session          HttpSession for authentication and IDs
    * @param model            Model to pass error or updated info back to the view
    * @return                 Redirect to menu on success, form reload on error
    */
   @PostMapping("/change-company-info")
   public String updateCompanyInfo(@RequestParam(required = false) String password,
                                   @RequestParam(required = false) String confirmPassword,
                                   @RequestParam String title,
                                   HttpSession session,
                                   Model model) {
       if (!isCompanyLoggedIn(session)) return "redirect:/login";

       Integer companyId = (Integer) session.getAttribute("companyId");
       Integer gameId = (Integer) session.getAttribute("companyGameId");

       // Validate password matching if changing password
       if (password != null && !password.isEmpty() && !password.equals(confirmPassword)) {
           model.addAttribute("error", "Passwords do not match.");
           Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
           model.addAttribute("company", company);
           return "company/change-company-info";
       }

       boolean success = companyService.updateCompanyInfo(companyId, gameId, title, password);
       if (!success) {
           model.addAttribute("error", "Failed to update company info.");
           Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
           model.addAttribute("company", company);
           return "company/change-company-info";
       }

       return "redirect:/company/company-menu";
   }



   
}
