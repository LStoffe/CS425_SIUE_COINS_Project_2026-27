package edu.SIUE.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;

import edu.SIUE.service.CompanyService;
import java.util.Map;

/*
 * PublishedInfoController.java
 *
 * Controller responsible for handling all routes related to the company's public (published) information
 * within the BIG simulation platform. This includes features such as:
 *   - Editing and displaying company core values
 *   - Managing the mission statement
 *   - Setting or showing company URL and other external-facing information
 *
 * All endpoints in this controller are under the /company/ route namespace
 * and require the user (company) to be authenticated.
 *
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
@RequestMapping("/company")
public class PublishedInfoController {

    @Autowired
    private CompanyService companyService;

    private boolean isCompanyLoggedIn(HttpSession session) {
        return session.getAttribute("companyId") != null;
    }

    // Published Info Menu Page
    @GetMapping("/published-info-menu")
    public String publishedInfoMenu(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/published-info/published-info-menu";
    }



    

    /**
     * Handles GET requests for the Core Values edit page.
     * Checks if the company is logged in; if not, redirects to the login page.
     * If authenticated, retrieves the company's core values from the database,
     * adds them to the model, and returns the core values edit view.
     *
     * @param session the current HTTP session containing company/game context
     * @param model the Spring Model used to add attributes for Thymeleaf templates
     * @return the core values edit page, or redirects to login if unauthorized
     */
    @GetMapping("/published-info/core-values")
    public String coreValues(HttpSession session, Model model) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");
        Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
        model.addAttribute("coreValues", company.get("corevalues"));
        return "company/published-info/core-values";
    }

    /**
     * Handles POST requests to update the company's core values.
     * Checks if the company is logged in; if not, redirects to the login page.
     * If authenticated, updates the core values in the database and
     * redirects to the company menu page.
     *
     * @param coreValues the new/updated core values submitted by the user
     * @param session the current HTTP session containing company/game context
     * @return a redirect string to the company menu, or to login if unauthorized
     */
    @PostMapping("/published-info/core-values")
    public String updateCoreValues(@RequestParam String coreValues,
                                   HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");
        companyService.updateCoreValues(companyId, gameId, coreValues);
        return "redirect:/company/company-menu";
    }





    /**
     * Handles GET request for the Mission Statement edit page.
     * Checks if the company is logged in. If not, redirect to login page.
     * If authenticated, retrieves the company's mission statement from the database,
     * adds it to the model, and returns the mission statement edit view.
     *
     * @param session the current HTTP session to retrieve company/game context
     * @param model the Spring Model to add attributes for Thymeleaf
     * @return the mission statement edit page, or redirects to login if unauthorized
     */
    @GetMapping("/published-info/mission-statement")
    public String missionStatement(HttpSession session, Model model) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");
        Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
        model.addAttribute("missionStatement", company.get("missionstatement"));
        return "company/published-info/mission-statement";
    }

    /**
     * Handles POST request to update the company's mission statement.
     * Checks if the company is logged in. If not, redirect to login page.
     * If authenticated, updates the mission statement in the database and
     * redirects to the company menu page.
     *
     * @param missionStatement the updated mission statement submitted by the user
     * @param session the current HTTP session to retrieve company/game context
     * @return a redirect string to the company menu, or to login if unauthorized
     */
    @PostMapping("/published-info/mission-statement")
    public String updateMissionStatement(@RequestParam String missionStatement,
                                         HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");
        companyService.updateMissionStatement(companyId, gameId, missionStatement);
        return "redirect:/company/company-menu";
    }



    

    /**
     * Handles GET request for the Company URL edit page.
     * Checks if the company is logged in; if not, redirects to login page.
     * Fetches the company's URL from the database and adds it to the model for display.
     *
     * @param session the current HTTP session to retrieve company/game context
     * @param model the Spring Model to add attributes for the Thymeleaf view
     * @return the view name for the company URL edit page, or redirect to login if not authenticated
     */
    @GetMapping("/published-info/company-url")
    public String companyUrl(HttpSession session, Model model) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");
        Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
        model.addAttribute("companyUrl", company.get("companyurl"));
        return "company/published-info/company-url";
    }

    /**
     * Handles POST request to update the Company URL.
     * Checks if the company is logged in; if not, redirects to login page.
     * If authenticated, updates the company's URL in the database.
     * Redirects to the main company menu upon success.
     *
     * @param companyUrl the updated company URL submitted by the user
     * @param session the current HTTP session to retrieve company/game context
     * @return a redirect string to the company menu, or to login if not authenticated
     */
    @PostMapping("/published-info/company-url")
    public String updateCompanyUrl(@RequestParam String companyUrl,
                                   HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");
        companyService.updateCompanyUrl(companyId, gameId, companyUrl);
        return "redirect:/company/company-menu";
    }





    // Productivity Report Page
    @GetMapping("/published-info/productivity-report")
    public String productivityReport(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/published-info/productivity-report";
    }



    

    // Network Dependencies Report Page
    @GetMapping("/published-info/network-dependencies-report")
    public String networkDependenciesReport(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/published-info/network-dependencies-report";
    }
}