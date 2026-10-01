package edu.SIUE.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import edu.SIUE.service.AdminAuthService;
import edu.SIUE.service.GameService;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * AdminController.java
 *
 * Controller for administrator-exclusive pages and operations in the BIG simulation platform.
 * 
 * Responsibilities:
 *  - Routes all admin-only URLs (base path: /admin)
 *  - Provides authentication guard for admin pages
 *  - Serves admin dashboard and administrative game management interfaces
 *  - Interfaces with AdminAuthService for authentication
 *  - Interfaces with GameService for high-level simulation setup and management
 *
 * All methods in this class should check for admin login before proceeding, redirecting
 * unauthorized users to the login page.
 *
 * Developed as part of the SIUE BIG simulation web application.
 *
 */

@Controller
@RequestMapping("/admin") // Base URL mapping for all admin pages
public class AdminController {

    @Autowired
    private AdminAuthService adminAuthService;

    @Autowired
    private GameService gameService;

    // Checks whether an admin is currently logged in by verifying the presence of "adminId" in the session
    private boolean isAdminLoggedIn(HttpSession session) {
        return session.getAttribute("adminId") != null;
    }

    // Admin Menu / Dashboard
    @GetMapping("/admin-menu")
    public String adminMenu(HttpSession session) {
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }
        return "admin/admin-menu"; // templates/admin/admin-menu.html
    }

    
    /**
     * Displays the "Create New Game" page for the admin.
     * Only accessible if an admin is logged in.
     */
    @GetMapping("/create-new-game")
    public String createNewGame(HttpSession session) {
        // Check to make sure the admin is logged in
        if (!isAdminLoggedIn(session)) {
            // If not logged in, redirect to login page
            return "redirect:/login";
        }
        // Serve the create new game HTML page (admin/create-new-game.html)
        return "admin/create-new-game";
    }

    /**
     * Handles the POST request for creating a new game.
     *
     * Validates game type selection, delegates game creation to GameService,
     * and provides feedback via the model if there are errors.
     * Redirects to admin menu upon success.
     */
    @PostMapping("/create-new-game")
    public String createNewGame(@RequestParam String gameName,
                                @RequestParam String startYear,
                                @RequestParam(defaultValue = "false") boolean building,
                                @RequestParam(defaultValue = "false") boolean heavy,
                                HttpSession session,
                                Model model) {

        // Ensure the user is an authenticated admin
        if (!isAdminLoggedIn(session)) {
            // If not logged in, redirect to login page
            return "redirect:/login";
        }

        // Ensure at least one game type (building or heavy) is selected
        if (!building && !heavy) {
            // Add an error message to the model to display on the page
            model.addAttribute("error", "Please select at least one game type.");
            // Return to the create new game page
            return "admin/create-new-game";
        }

        // Retrieve the admin ID from the session
        int adminId = (int) session.getAttribute("adminId");

        // Attempt to create the game using GameService
        boolean success = gameService.createGame(adminId, gameName, startYear, building, heavy);

        // If there is a failure in game creation, inform the user
        if (!success) {
            // Add an error message to the model to display on the page
            model.addAttribute("error", "Failed to create game. Please try again.");
            // Return to the create new game page
            return "admin/create-new-game";
        }

        // If game creation was successful, redirect to the admin menu/dashboard
        return "redirect:/admin/admin-menu";
    }

    /**
     * Handles GET requests for viewing all games associated with the logged-in admin.
     * Populates the model with a list of games (excluding the template game).
     * Redirects to the login page if the admin is not logged in.
     */
    @GetMapping("/view-games")
    public String viewGames(HttpSession session, Model model) {
        // Check if admin is logged in
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }

        // Retrieve admin ID from session
        int adminId = (int) session.getAttribute("adminId");
        // Get all games for this admin
        List<Map<String, Object>> rawGames = adminAuthService.getGamesByAdmin(adminId);
        List<Map<String, Object>> games = new ArrayList<>();

        // Iterate through each game record and construct the response objects
        for (Map<String, Object> row : rawGames) {
            Integer gameId = ((Number) row.get("gameid")).intValue();

            // Skip the template game (gameid == 0)
            if (gameId == 0) continue;

            Map<String, Object> game = new HashMap<>();
            game.put("id", gameId); // Game ID
            game.put("name", row.get("gamename")); // Game Name

            // Format start year
            java.sql.Date startDate = (java.sql.Date) row.get("start_year");
            game.put("startYear", startDate.toLocalDate().getYear());

            game.put("currentPeriod", row.get("current_period")); // Current period

            // Set game status as "Active" or "Inactive"
            boolean active = Boolean.TRUE.equals(row.get("is_active"));
            game.put("status", active ? "Active" : "Inactive");

            // Determine game type
            Object gametypeObj = row.get("gametype");
            String type = "Unknown";
            if (gametypeObj != null) {
                int gametype = ((Number) gametypeObj).intValue();
                type = gametype == 1 ? "Building"
                     : gametype == 2 ? "Heavy"
                     : "Building & Heavy";
            }
            game.put("type", type);

            games.add(game);
        }

        // Add the games list to the model for rendering
        model.addAttribute("games", games);

        // Return the Thymeleaf view for displaying games
        return "admin/view-games";
    }

    // Display the change password page (GET request)
    @GetMapping("/change-password")
    public String changePassword(HttpSession session) {
        // Check if admin is logged in, otherwise redirect to login page
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }
        // Render the change password view
        return "admin/change-password"; // templates/admin/change-password.html
    }

    // Handle the change password form submission (POST request)
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                HttpSession session,
                                Model model) {

        // Ensure the admin is logged in before proceeding
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }

        // Check if the new password and confirmation match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "admin/change-password";
        }

        // Retrieve admin ID from session
        int adminId = (int) session.getAttribute("adminId");
        // Attempt to update the password using the adminAuthService
        boolean success = adminAuthService.updatePassword(adminId, oldPassword, newPassword);

        // If updatePassword returns false, the old password was incorrect
        if (!success) {
            model.addAttribute("error", "Current password is incorrect.");
            return "admin/change-password";
        }

        // Password updated successfully, show a success message
        model.addAttribute("success", "Password updated successfully.");
        return "admin/change-password";
    }

    /**
     * Shows the "Change Email" form to the logged-in admin.
     * @param session the current HTTP session
     * @return the change-email view, or redirects to login if not authenticated
     */
    @GetMapping("/change-email")
    public String changeEmail(HttpSession session) {
        // If the admin is not logged in, redirect to the login page
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }
        // Display the change-email page (templates/admin/change-email.html)
        return "admin/change-email";
    }

    /**
     * Handles POST submission for changing the admin's email address.
     * @param email the new email address to be set
     * @param session the current HTTP session
     * @param model Model to add success/error messages
     * @return stays on change-email page with success/error message, or redirects to login if not authenticated
     */
    @PostMapping("/change-email")
    public String changeEmail(@RequestParam String email,
                              HttpSession session,
                              Model model) {

        // If the admin is not logged in, redirect to the login page
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }

        // Retrieve the admin ID from the session
        int adminId = (int) session.getAttribute("adminId");
        // Attempt to update the email for the admin
        boolean success = adminAuthService.updateEmail(adminId, email);

        // If the update fails, add an error message to the model and stay on change-email page
        if (!success) {
            model.addAttribute("error", "Failed to update email.");
            return "admin/change-email";
        }

        // If update succeeded, add a success message to the model
        model.addAttribute("success", "Email updated successfully.");
        return "admin/change-email";
    }

    // Displays the register admin form
    @GetMapping("/register-admin")
    public String registerAdmin(HttpSession session) {
        // Redirect to login if not logged in as admin
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }
        // Show the register admin page
        return "admin/register-admin"; // templates/admin/register-admin.html
    }

    // Handles the POST request to add a new admin
    @PostMapping("/register-admin")
    public String addAdmin(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String confirmPassword,
                        @RequestParam String email,
                        HttpSession session,
                        Model model) {

        // Redirect to login if not logged in as admin
        if (!isAdminLoggedIn(session)) {
            return "redirect:/login";
        }

        // Check that passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "admin/register-admin";
        }

        // Attempt to register the new admin
        boolean success = adminAuthService.registerAdmin(username, password, email);
        if (!success) {
            model.addAttribute("error", "Username already exists.");
            return "admin/register-admin";
        }

        // Redirect to the admin menu on success
        return "redirect:/admin/admin-menu";
    }

}
