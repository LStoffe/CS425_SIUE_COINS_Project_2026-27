package edu.SIUE.controller;

import edu.SIUE.service.AdminAuthService;
import edu.SIUE.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/*
 * LoginController.java
 *
 * Controller responsible for managing user authentication within the BIG simulation platform.
 *
 * Responsibilities:
 *   - Presents the login form to users and processes login submissions.
 *   - Authenticates users as either company members (via the company table) or admins (via the admin table)
 *     using the configured authentication services.
 *   - Handles session management for authenticated users, redirecting them to the correct dashboard/menu upon login.
 *   - Ensures password verification using hashed credentials compliant with database security.
 *
 * This controller provides the main entry point for the web application's authentication flow.
 *
 * Developed as part of the SIUE BIG simulation web application.
 */


@Controller
public class LoginController {
    
    @Autowired
    private AuthService authService;

    @Autowired
    private AdminAuthService adminAuthService;
    
    /**
     * Handles GET requests to the /login endpoint.
     * 
     * If a user is already logged in as an admin, redirects to the admin menu.
     * If a user is already logged in as a company, redirects to the company menu.
     * Otherwise, returns the login page view.
     *
     * @param session the current HTTP session
     * @return the view name or redirect instruction
     */
    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // Redirect to admin menu if admin is logged in
        if (session.getAttribute("adminId") != null) {
            return "redirect:/admin/admin-menu";
        }
        // Redirect to company menu if company is logged in
        if (session.getAttribute("companyId") != null) {
            return "redirect:/company/company-menu";
        }
        // Otherwise, show the login page
        return "login";
    }
    
    /**
     * Processes POST requests for login form submission.
     * <p>
     * This method attempts to authenticate the user as either an admin or company member.
     * It first checks admin credentials; if successful, sets admin session attributes and redirects to the admin dashboard.
     * If the admin authentication fails, it then attempts to authenticate the user as a company member.
     * If company authentication is successful, it sets company session attributes and redirects to the company dashboard.
     * If neither authentication is successful, it returns the login page with an error indicator.
     * </p>
     *
     * @param username the submitted username from the login form
     * @param password the submitted password from the login form
     * @param session the current HTTP session for storing user attributes
     * @param model the model for passing data to the view
     * @return the view name or redirect instruction
     */
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {
        // Attempt admin authentication first
        Map<String, Object> admin = adminAuthService.authenticateAdmin(username, password);
        if (admin != null) {
            // Successful admin authentication: set session attributes and redirect to admin menu
            session.setAttribute("adminId", admin.get("adminid"));
            session.setAttribute("username", username);
            session.setAttribute("admin", admin);
            return "redirect:/admin/admin-menu";
        }

        // Attempt company authentication if admin auth failed
        Map<String, Object> company = authService.authenticatecompany(username, password);

        if (company != null) {
            // Retrieve company ID, supporting both "companyid" and legacy "id"
            Object companyId = company.get("companyid");
            if (companyId == null) {
                companyId = company.get("id"); // support legacy schemas
            }
            // Set session attributes for authenticated company user
            session.setAttribute("companyId", companyId);
            session.setAttribute("companyGameId", company.get("gameid"));
            session.setAttribute("username", username);
            session.setAttribute("company", company);

            // Redirect to company menu/dashboard
            return "redirect:/company/company-menu";
        }

        // Neither authentication succeeded: show login page with error indicator
        model.addAttribute("loginError", true);
        return "login";
    }




    /**
     * Handles GET requests to log out the current user.
     * 
     * Invalidates the current HTTP session, effectively logging out the user,
     * and then redirects the user to the login page.
     *
     * @param session the current HTTP session to invalidate
     * @return a redirect instruction to the login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }



    
}
