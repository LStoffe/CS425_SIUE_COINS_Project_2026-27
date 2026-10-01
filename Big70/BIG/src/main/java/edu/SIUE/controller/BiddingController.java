package edu.SIUE.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.servlet.http.HttpSession;

import edu.SIUE.service.JobService;
import edu.SIUE.service.GameService;
import edu.SIUE.service.BidService;
import edu.SIUE.service.CompanyService;


import java.util.List;
import java.util.Map;

/*
 * BiddingController.java
 *
 * Controller for company bidding operations within the BIG simulation platform.
 *
 * Responsible for:
 *   - Routing all bidding-related company pages (base path: /company)
 *   - Serving views for bidding menu, bid placement, bid reports, and schedule-related estimates
 *   - Interfacing with JobService, GameService, BidService, and CompanyService to provide
 *     business logic and database interactions for job bidding functionality
 *   - Enforcing company authentication and session validation across all bidding operations
 *
 * Typical features managed here include:
 *   - Presenting jobs available for bidding and displaying job type information
 *   - Handling new bid submissions and listing existing company bids
 *   - Providing reports/summaries on scheduled work, awarded jobs, and bid outcomes
 *
 * Developed as part of the SIUE BIG competition web application.
 */

@Controller
@RequestMapping("/company")
public class BiddingController {

    @Autowired
    private JdbcTemplate jdbcTemplate;  

    @Autowired
    private JobService jobService;

    @Autowired
    private GameService gameService;

    @Autowired
    private BidService bidService;

    @Autowired
    private CompanyService companyService;

    private boolean isCompanyLoggedIn(HttpSession session) {
        return session.getAttribute("companyId") != null;
    }

    // Bidding Menu Page
    @GetMapping("/bidding-menu")
    public String biddingMenu(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/bidding/bidding-menu";
    }




    
   /**
    * Displays the Bid-on-a-Job page listing all jobs available for bidding to the company.
    * Handles mapping of job type names and attaches any existing bids placed by the company.
    *
    * @param session the current HTTP session (company authentication and game context)
    * @param model   Spring model to pass data to the view template
    * @return the Thymeleaf view for bid-on-a-job, or redirect to login if not authenticated
    */
   @GetMapping("/bidding/bid-on-a-job")
   public String bidOnAJob(HttpSession session, Model model) {
       // Redirect to login if company is not authenticated
       if (!isCompanyLoggedIn(session)) return "redirect:/login";

       Integer gameId = (Integer) session.getAttribute("companyGameId");
       Integer companyId = (Integer) session.getAttribute("companyId");
       if (gameId == null) return "redirect:/login";

       // Add the current period date string to the view model
       model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));

       // Retrieve the list of available jobs for this game
       List<Map<String, Object>> rawJobs = jobService.getAvailableJobs(gameId);

       // Prepare a jobs list with display-friendly job type names and any existing company bids
       List<Map<String, Object>> jobs = new java.util.ArrayList<>();
       for (Map<String, Object> row : rawJobs) {
           Map<String, Object> job = new java.util.HashMap<>(row);
           int typeId = ((Number) row.get("job_type_id")).intValue();
           // Attach the display name for the job type
           job.put("job_type_name", jobService.getJobTypeName(typeId));

           // Attach current bid by this company, if one exists
           int jobId = ((Number) row.get("jobid")).intValue();
           Map<String, Object> existingBid = bidService.getCompanyBidForJob(gameId, companyId, jobId);
           job.put("currentBid", existingBid != null ? existingBid.get("bid_amount") : null);

           jobs.add(job);
       }

       model.addAttribute("jobs", jobs);

       // Add error message to the model, if present
       String error = (String) model.asMap().get("error");
       if (error != null) model.addAttribute("error", error);

       return "company/bidding/bid-on-a-job";
   }

   /**
    * Handles bid submission for a job.
    * Validates user authentication, bid positivity, and cash on hand.
    *
    * @param jobId     the ID of the job being bid on
    * @param bidAmount the amount offered on the bid
    * @param session   the current HTTP session (company authentication and game context)
    * @return redirect to the bid-on-a-job page with error message if invalid, or success
    */
   @PostMapping("/bidding/submit-bid")
   public String submitBid(@RequestParam int jobId,
                           @RequestParam double bidAmount,
                           HttpSession session) {
       // Redirect to login if company is not authenticated
       if (!isCompanyLoggedIn(session)) return "redirect:/login";

       Integer gameId = (Integer) session.getAttribute("companyGameId");
       Integer companyId = (Integer) session.getAttribute("companyId");

       // Validate the bid amount is positive (> 0)
       if (bidAmount <= 0) {
           return "redirect:/company/bidding/bid-on-a-job?error=invalidAmount";
       }

       // Check if company has sufficient cash to cover the bid
       Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
       double cashOnHand = ((Number) company.get("cash_on_hand")).doubleValue();
       if (bidAmount > cashOnHand) {
           return "redirect:/company/bidding/bid-on-a-job?error=exceedsCash";
       }

       // Record/update the bid in the database
       bidService.submitBid(gameId, companyId, jobId, bidAmount);
       return "redirect:/company/bidding/bid-on-a-job";
   }

   /**
    * Handles retraction of a previously submitted bid for a given job.
    *
    * @param jobId   the ID of the job whose bid is to be retracted
    * @param session the current HTTP session (company authentication and game context)
    * @return redirect to the bid-on-a-job page
    */
   @PostMapping("/bidding/retract-bid")
   public String retractBid(@RequestParam int jobId,
                            HttpSession session) {
       // Redirect to login if company is not authenticated
       if (!isCompanyLoggedIn(session)) return "redirect:/login";

       Integer gameId = (Integer) session.getAttribute("companyGameId");
       Integer companyId = (Integer) session.getAttribute("companyId");

       // Remove bid from the database
       bidService.retractBid(gameId, companyId, jobId);
       return "redirect:/company/bidding/bid-on-a-job";
   }







    /**
     * Handles GET requests for the bid opening report page.
     * <p>
     * This method retrieves and prepares data for rendering the "Report from Bid Opening"
     * page for the currently logged in company. It determines the report period 
     * (previous period), fetches the bid opening results for that period, 
     * augments each result with the job type name, and populates the model with this data 
     * along with period date, username, and current period.
     * 
     * @param session HttpSession object for authentication and contextual info (company/game)
     * @param model   Spring Model object to add attributes required by the view
     * @return The name of the Thymeleaf template to render the bid opening report page
     */
    @GetMapping("/bidding/report-from-bid-opening")
    public String reportFromBidOpening(HttpSession session, Model model) {
        // Redirect to login if the company is not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Retrieve gameId from session
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Get game details from the service
        Map<String, Object> game = gameService.getGameById(gameId);

        // Extract the current period from the game data
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();

        // Determine the report period (previous period, minimum 0)
        int reportPeriod = Math.max(0, currentPeriod - 1);

        // Query for bid opening report data
        List<Map<String, Object>> bids = bidService.getBidOpeningReport(gameId, reportPeriod);

        // Compose a list of bid results with the job type name for display
        List<Map<String, Object>> results = new java.util.ArrayList<>();
        for (Map<String, Object> row : bids) {
            Map<String, Object> result = new java.util.HashMap<>(row);
            int typeId = ((Number) row.get("job_type_id")).intValue();
            result.put("job_type_name", jobService.getJobTypeName(typeId));
            results.add(result);
        }

        // Add attributes needed by the Thymeleaf template
        model.addAttribute("bids", results);
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("currentPeriod", currentPeriod);
        return "company/bidding/report-from-bid-opening";
    }


    

    

    /**
     * Handles GET requests to the Complete List of Bids Report page.
     * If the company is not logged in, redirect to login page. Otherwise, renders the
     * purchase confirmation/report prompt page.
     *
     * @param session the HTTP session containing company authentication info
     * @return the view name for the complete-list-of-bids-report page
     */
    @GetMapping("/bidding/complete-list-of-bids-report")
    public String generatedListOfBidsReport(HttpSession session) {
        // Redirect to login if user is not authenticated as a company
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        // Render the report purchase prompt
        return "company/bidding/complete-list-of-bids-report";
    }

    /**
     * Handles POST requests to purchase the Complete List of Bids Report.
     * Deducts $1000 from the company's account if sufficient funds exist, fetches bid data
     * grouped by job, and prepares model for the report page.
     * If insufficient funds, redisplays the purchase prompt with error.
     *
     * @param session Session holding company and game authentication info
     * @param model   Spring Model for passing attributes to the Thymeleaf template
     * @return the view name for the generated-list-of-bids-report or error state
     */
    @PostMapping("/bidding/purchase-complete-list-of-bids")
    public String purchaseCompleteListOfBids(HttpSession session, Model model) {
        // Redirect to login if not authenticated
        if (!isCompanyLoggedIn(session)) return "redirect:/login";

        // Get company and game identifiers from session
        Integer companyId = (Integer) session.getAttribute("companyId");
        Integer gameId = (Integer) session.getAttribute("companyGameId");

        // Retrieve company account details to check available funds
        Map<String, Object> company = companyService.getCompanyById(companyId, gameId);
        double cashOnHand = ((Number) company.get("cash_on_hand")).doubleValue();

        // If insufficient funds, present an error on the report purchase page
        if (cashOnHand < 1000) {
            model.addAttribute("error", "Insufficient funds to purchase this report.");
            return "company/bidding/complete-list-of-bids-report";
        }

        // Deduct $1000 from company's cash on hand
        jdbcTemplate.update(
            "UPDATE public.company SET cash_on_hand = cash_on_hand - 1000 WHERE companyid = ? AND gameid = ?",
            companyId, gameId);

        // Determine the target period: previous period or 0 if not available
        Map<String, Object> game = gameService.getGameById(gameId);
        int currentPeriod = ((Number) game.get("currentPeriod")).intValue();
        int reportPeriod = Math.max(0, currentPeriod - 1);

        // Retrieve all bids for the report period
        List<Map<String, Object>> rawBids = bidService.getCompleteListOfBids(gameId, reportPeriod);

        // Group and map bids by job for frontend consumption
        java.util.LinkedHashMap<Integer, Map<String, Object>> jobMap = new java.util.LinkedHashMap<>();
        for (Map<String, Object> row : rawBids) {
            int jobId = ((Number) row.get("jobid")).intValue();
            // If this is the first bid for the job, create an entry for this job
            if (!jobMap.containsKey(jobId)) {
                Map<String, Object> jobEntry = new java.util.HashMap<>();
                jobEntry.put("jobid", jobId);
                int typeId = ((Number) row.get("job_type_id")).intValue();
                jobEntry.put("job_type_name", jobService.getJobTypeName(typeId));
                jobEntry.put("bids", new java.util.ArrayList<Map<String, Object>>());
                jobMap.put(jobId, jobEntry);
            }
            // Add the bid information to the correct job entry
            Map<String, Object> bidEntry = new java.util.HashMap<>();
            bidEntry.put("company_title", row.get("company_title"));
            bidEntry.put("bid_amount", row.get("bid_amount"));
            bidEntry.put("outcome_code", row.get("outcome_code"));
            ((java.util.List<Map<String, Object>>) jobMap.get(jobId).get("bids")).add(bidEntry);
        }

        // Populate the model with grouped jobs, period, and user information
        model.addAttribute("jobs", new java.util.ArrayList<>(jobMap.values()));
        model.addAttribute("periodDate", gameService.getPeriodDateString(gameId));
        model.addAttribute("username", session.getAttribute("username"));
        return "company/bidding/generated-list-of-bids-report";
    }




    // Schedule Estimates Page
    @GetMapping("/bidding/schedule-estimates")
    public String scheduleEstimates(HttpSession session) {
        if (!isCompanyLoggedIn(session)) return "redirect:/login";
        return "company/bidding/schedule-estimates";
    }
}