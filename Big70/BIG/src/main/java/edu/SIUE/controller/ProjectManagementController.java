package edu.SIUE.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import edu.SIUE.service.JobFactoryService;
import edu.SIUE.service.JobService;
import edu.SIUE.service.GameService;



/*
 * ProjectManagementController.java
 *
 * Controller responsible for managing all project management-related pages and routes
 * for company users within the BIG simulation platform.
 *
 * Responsibilities:
 *   - Routes HTTP requests to project management features such as:
 *        - Progress reporting
 *        - Overtime/extra work
 *        - Project billing and payments
 *        - Other job tracking and project status pages
 *   - Ensures only authenticated company users can access project management pages
 *   - Aggregates and prepares job/project data for frontend display
 *   - Integrates with JobFactoryService and JobService to obtain project/job details
 *
 * All endpoints in this controller are secured by a company login check.
 * 
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
@RequestMapping("/company")
public class ProjectManagementController {

    @Autowired
    private JobFactoryService jobFactoryService;

    @Autowired
    private JobService jobService;

    @Autowired
    private GameService gameService;

    private boolean isCompanyLoggedIn(HttpSession session) {
        return session.getAttribute("companyId") != null;
    }

    // Project Management Menu Page
    @GetMapping("/project-management-menu")
    public String projectManagementMenu(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/project-management/project-management-menu";
    }





    /**
     * Handles GET requests for the project progress report page.
     * 
     * This method aggregates a list of all active jobs for the currently logged-in company.
     * For each job, the job type name and the percentage progress (based on periods worked
     * versus periods estimated to complete) are calculated and attached as new properties.
     * 
     * The method ensures that only authenticated company users can access this page. 
     * If the user is not logged in as a company, they are redirected to the login page.
     * 
     * @param session the current HTTP session, used to check login status and retrieve user info
     * @param model the model to add job and user info for rendering in the view
     * @return the view name for the progress report page or redirect to login if unauthorized
     */
    @GetMapping("/project-management/progress-report")
    public String progressReport(HttpSession session, Model model) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        List<Map<String, Object>> activeJobs = jobFactoryService.getCompanyActiveJobs(gameId, companyId);

        List<Map<String, Object>> jobs = new java.util.ArrayList<>();
        for (Map<String, Object> row : activeJobs) {
            Map<String, Object> job = new java.util.HashMap<>(row);

            // Add job type name
            int typeId = ((Number) row.get("job_type_id")).intValue();
            job.put("job_type_name", jobService.getJobTypeName(typeId));

            // Add method name — MUST be inside the loop using row not job
            int currentMethod = ((Number) row.get("current_method")).intValue();
            job.put("method_name", JobFactoryService.METHOD_NAMES.get(currentMethod));

            // Calculate progress adjusted for method speed
            int estimatedWorkdays = ((Number) row.get("estimated_workdays")).intValue();
            int periodAwarded = ((Number) row.get("period_awarded")).intValue();
            int periodsToComplete = Math.max(1, estimatedWorkdays / 40);
            int periodsWorked = currentPeriod - periodAwarded;
            double speedModifier = jobFactoryService.getMethodSpeedModifier(currentMethod);
            int effectivePeriodsToComplete = Math.max(1, (int) Math.round(periodsToComplete * speedModifier));
            double progress = Math.min(100.0, (periodsWorked / (double) effectivePeriodsToComplete) * 100.0);
            job.put("progress", String.format("%.1f", progress));

            jobs.add(job);
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("username", session.getAttribute("username"));
        return "company/project-management/progress-report";
    }





    /**
     * Handles GET requests for the Reschedule Methods page.
     * This page allows the company to view their current active jobs and, for each job,
     * determine if the production method can be changed during the current period.
     * - Checks company authentication.
     * - Prepares job data with method names, job type names, and change-eligible flags.
     * - Passes job and method info to the view for display.
     *
     * @param session the current HTTP session
     * @param model   the Spring MVC model for passing attributes to the view
     * @return the view for rescheduling project methods, or redirect to login if not authenticated
     */
    @GetMapping("/project-management/reschedule-methods")
    public String rescheduleMethods(HttpSession session, Model model) {
        // Ensure user is logged in as a company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve session attributes for company/game context
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get the current period from the game
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Get list of active jobs for this company and game
        List<Map<String, Object>> activeJobs = jobFactoryService.getCompanyActiveJobs(gameId, companyId);

        // For each job, add the job type name, current method name, and whether method can be changed
        List<Map<String, Object>> jobs = new java.util.ArrayList<>();
        for (Map<String, Object> row : activeJobs) {
            Map<String, Object> job = new java.util.HashMap<>(row);

            // Lookup and add job type name
            int typeId = ((Number) row.get("job_type_id")).intValue();
            job.put("job_type_name", jobService.getJobTypeName(typeId));

            // Add current method name
            int currentMethod = ((Number) row.get("current_method")).intValue();
            job.put("method_name", JobFactoryService.METHOD_NAMES.get(currentMethod));

            // Determine if the method can be changed this period (can't if already changed this period)
            int methodChangedPeriod = ((Number) row.get("method_changed_period")).intValue();
            job.put("can_change", methodChangedPeriod != currentPeriod);

            jobs.add(job);
        }

        // Add jobs and method info to model for rendering in template
        model.addAttribute("jobs", jobs);
        model.addAttribute("methodNames", JobFactoryService.METHOD_NAMES);
        model.addAttribute("methodDescriptions", JobFactoryService.METHOD_DESCRIPTIONS);
        model.addAttribute("currentPeriod", currentPeriod);

        // Render the reschedule-methods page
        return "company/project-management/reschedule-methods";
    }

    /**
     * Handles POST requests from the Reschedule Methods form.
     * Updates a job to use a newly selected method (if eligible),
     * shows error or success feedback, and redisplays the updated page.
     * - Checks company authentication.
     * - Attempts to change the job's method.
     * - Populates model with jobs, method info, and any error/success message.
     *
     * @param jobId     The target job to update
     * @param newMethod The new production method index
     * @param session   the current HTTP session
     * @param model     the Spring MVC model for passing attributes to the view
     * @return the view for rescheduling project methods, or redirect to login if not authenticated
     */
    @PostMapping("/project-management/reschedule-methods")
    public String updateMethod(@RequestParam int jobId,
                              @RequestParam int newMethod,
                              HttpSession session,
                              Model model) {
        // Ensure user is logged in as a company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve session attributes for company/game context
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get the current period from the game
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Attempt to change the job's method; receive error message if not successful
        String error = jobFactoryService.changeJobMethod(gameId, companyId, jobId, newMethod, currentPeriod);
        if (error != null) {
            model.addAttribute("error", error);
        } else {
            model.addAttribute("success", "Method updated successfully.");
        }

        // Prepare updated list of jobs with job type and current method names, etc.
        List<Map<String, Object>> activeJobs = jobFactoryService.getCompanyActiveJobs(gameId, companyId);
        List<Map<String, Object>> jobs = new java.util.ArrayList<>();
        for (Map<String, Object> row : activeJobs) {
            Map<String, Object> job = new java.util.HashMap<>(row);
            int typeId = ((Number) row.get("job_type_id")).intValue();
            job.put("job_type_name", jobService.getJobTypeName(typeId));
            int currentMethod = ((Number) row.get("current_method")).intValue();
            job.put("method_name", JobFactoryService.METHOD_NAMES.get(currentMethod));
            int methodChangedPeriod = ((Number) row.get("method_changed_period")).intValue();
            job.put("can_change", methodChangedPeriod != currentPeriod);
            jobs.add(job);
        }

        // Add jobs and method info to model for rendering in template
        model.addAttribute("jobs", jobs);
        model.addAttribute("methodNames", JobFactoryService.METHOD_NAMES);
        model.addAttribute("methodDescriptions", JobFactoryService.METHOD_DESCRIPTIONS);
        model.addAttribute("currentPeriod", currentPeriod);

        // Render the reschedule-methods page (with feedback)
        return "company/project-management/reschedule-methods";
    }




    // Assign Overtime Page
    @GetMapping("/project-management/assign-overtime")
    public String assignOvertime(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/project-management/assign-overtime";
    }





    // Bill for Work Completed Page
    @GetMapping("/project-management/bill-for-work-completed")
    public String billForWorkCompleted(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/project-management/bill-for-work-completed";
    }





    /**
     * Handles GET requests for the "Payments Received" page as part of project management.
     * 
     * This route allows an authenticated company to view payments received from completed jobs
     * in the current game period. For each job, the payment per period is calculated based on
     * bid amount, estimated workdays, and the method speed modifier. The results, along with
     * job type names and method names, are prepared and passed to the view for display.
     *
     * Route: /project-management/payments-received
     * 
     * @param session the current HTTP session, used to check company authentication
     * @param model the Spring MVC model to hold job payment and user data for rendering
     * @return view name for Thymeleaf template, or redirects to login if unauthorized
     */
    @GetMapping("/project-management/payments-received")
    public String paymentsReceived(HttpSession session, Model model) {
        // Check if user is logged in as a company; if not, redirect to login page
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve session attributes for company and game context
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get current game info and period number
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Get completed jobs/payments received for this company in this game
        List<Map<String, Object>> rawPayments = jobFactoryService.getPaymentsReceived(gameId, companyId);

        // Prepare detailed payment info for display
        List<Map<String, Object>> payments = new java.util.ArrayList<>();
        double totalReceived = 0;

        // For each payment entry, compute extra display properties
        for (Map<String, Object> row : rawPayments) {
            Map<String, Object> payment = new java.util.HashMap<>(row);

            // Get human-readable job type name
            int typeId = ((Number) row.get("job_type_id")).intValue();
            payment.put("job_type_name", jobService.getJobTypeName(typeId));

            // Retrieve period and method details to calculate per-period payment
            int estimatedWorkdays = ((Number) row.get("estimated_workdays")).intValue();
            int currentMethod = ((Number) row.get("current_method")).intValue();
            double bidAmount = ((Number) row.get("bid_amount")).doubleValue();

            // Calculate how many periods are required for completion at the given method
            int basePeriodsToComplete = Math.max(1, estimatedWorkdays / 40);
            double speedModifier = jobFactoryService.getMethodSpeedModifier(currentMethod);
            int effectivePeriodsToComplete = Math.max(1, (int) Math.round(basePeriodsToComplete * speedModifier));
            // Determine income per period for this completed job
            double incomePerPeriod = bidAmount / effectivePeriodsToComplete;

            // Add per-period income and method name for rendering
            payment.put("income_per_period", incomePerPeriod);
            payment.put("method_name", JobFactoryService.METHOD_NAMES.get(currentMethod));
            // Track total received (sum of all job payment per periods)
            totalReceived += incomePerPeriod;

            payments.add(payment);
        }

        // Add all payment/job info and meta info to model for use in template
        model.addAttribute("payments", payments);
        model.addAttribute("totalReceived", totalReceived);
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("currentPeriod", currentPeriod);

        // Render the payments-received page
        return "company/project-management/payments-received";
    }



    
}