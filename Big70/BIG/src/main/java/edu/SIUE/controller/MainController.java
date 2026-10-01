package edu.SIUE.controller;

import edu.SIUE.service.DatabaseStatusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
 * MainController.java
 *
 * Controller responsible for routing and handling the core public pages
 * of the BIG simulation platform, including the main menu, overview, and
 * other general entry points.
 *
 * Responsibilities:
 *   - Serves the application's main landing and informational pages
 *   - Provides database connection status and summary info (such as company count)
 *     to the frontend for display in the main menu
 *   - Handles session user data for personalized content when available
 *   - Intended as the main entry controller for unauthenticated or general
 *     application access, distinct from company/admin feature controllers
 *
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
public class MainController {

    @Autowired
    private DatabaseStatusService databaseStatusService;

    /**
     * Handles GET requests for the main menu page.
     * 
     * This method responds to both the root ("/") and "/main-menu" paths.
     * It returns the name of the view template for the main menu.
     *
     * @param model the Spring model for passing data to the view
     * @param session the current HTTP session
     * @return the view name for the main menu page
     */
    @GetMapping({"/", "/main-menu"})
    public String mainMenu(Model model, HttpSession session) {
        return "main-menu"; // Renders templates/main-menu.html
    }




    /**
     * Handles GET requests for the overview page.
     *
     * @return the view name for the overview page (renders templates/overview.html)
     */
    @GetMapping("/overview")
    public String overview() {
        return "overview";
    }




    // Secret easter egg - shows image briefly before redirecting to YouTube
    @GetMapping("/unused")
    public String unused() {
        return "unused"; // templates/unused.html
    }

}
