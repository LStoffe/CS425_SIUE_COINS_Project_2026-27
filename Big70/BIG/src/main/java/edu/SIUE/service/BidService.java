package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * BidService.java
 *
 * Service layer for managing company bids on jobs within the SIUE BIG simulation platform.
 *
 * Responsibilities:
 *   - Handles all business logic for bid-related operations, including:
 *       - Submitting new bids and updating existing bids
 *       - Retracting bids (removal from the system)
 *       - Retrieving bid information for jobs, companies, and games
 *       - Evaluating and processing bid outcomes as required
 *
 * This service interacts directly with the 'bid' table in the database to perform CRUD operations.
 *
 * Typical Usage Context:
 *   - Invoked by controllers to process HTTP requests concerning company bidding actions.
 *   - Used within the platform's game flow and admin interfaces for overview and evaluation purposes.
 *
 * Developed for the SIUE BIG simulation web application.
 */

@Service
public class BidService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    

    /**
     * Submits or updates a company's bid on a job.
     * If a bid already exists for this company/job/game, it will be updated.
     * Otherwise a new bid is inserted.
     *
     * @param gameId    The game session ID
     * @param companyId The company placing the bid
     * @param jobId     The job being bid on
     * @param bidAmount The bid amount in dollars
     * @return true if successful, false otherwise
     */
    public boolean submitBid(int gameId, int companyId, int jobId, double bidAmount) {
        try {
            int count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.bid WHERE gameid = ? AND companyid = ? AND jobid = ?",
                Integer.class, gameId, companyId, jobId);

            if (count > 0) {
                jdbcTemplate.update(
                    "UPDATE public.bid SET bid_amount = ? WHERE gameid = ? AND companyid = ? AND jobid = ?",
                    bidAmount, gameId, companyId, jobId);
            } else {
                jdbcTemplate.update(
                    "INSERT INTO public.bid (jobid, companyid, gameid, bid_amount, outcome_code) VALUES (?, ?, ?, ?, 0)",
                    jobId, companyId, gameId, bidAmount);
            }
            return true;
        } catch (Exception e) {
            System.err.println("submitBid error: " + e.getMessage());
            return false;
        }
    }





    /**
     * Retracts a company's bid on a job by deleting it from the database.
     *
     * @param gameId    The game session ID
     * @param companyId The company retracting the bid
     * @param jobId     The job to retract the bid from
     * @return true if successful, false otherwise
     */
    public boolean retractBid(int gameId, int companyId, int jobId) {
        try {
            jdbcTemplate.update(
                "DELETE FROM public.bid WHERE gameid = ? AND companyid = ? AND jobid = ?",
                gameId, companyId, jobId);
            return true;
        } catch (Exception e) {
            System.err.println("retractBid error: " + e.getMessage());
            return false;
        }
    }





    /**
     * Retrieves a company's existing bid for a specific job.
     *
     * @param gameId    The game session ID
     * @param companyId The company to check
     * @param jobId     The job to check
     * @return Map containing bid_amount, or null if no bid exists
     */
    public Map<String, Object> getCompanyBidForJob(int gameId, int companyId, int jobId) {
        try {
            String sql = """
                SELECT bid_amount FROM public.bid 
                WHERE gameid = ? AND companyid = ? AND jobid = ?
                """;
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, gameId, companyId, jobId);
            return rows.isEmpty() ? null : rows.get(0);
        } catch (Exception e) {
            System.err.println("getCompanyBidForJob error: " + e.getMessage());
            return null;
        }
    }





    /**
     * Evaluates all bids for all jobs in a game when the period advances.
     * Awards each job to the lowest qualified bid.
     * Disqualifies bids below 80% of direct cost.
     * Sets outcome codes: 1 = winner, 2 = bid too low, 6 = lost to lower bid.
     *
     * @param gameId The game session ID
     */
    public void evaluateBids(int gameId) {
        try {
            String jobsSql = """
                SELECT DISTINCT j.jobid, j.direct_cost
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid AND j.gameid = b.gameid
                WHERE j.gameid = ? AND j.companyid IS NULL AND j.is_active = true
                """;

            List<Map<String, Object>> jobsWithBids = jdbcTemplate.queryForList(jobsSql, gameId);


            for (Map<String, Object> job : jobsWithBids) {
                int jobId = ((Number) job.get("jobid")).intValue();
                long directCost = ((Number) job.get("direct_cost")).longValue();
                long minimumBid = Math.round(directCost * 0.80);

            

                String bidsSql = """
                    SELECT companyid, bid_amount 
                    FROM public.bid 
                    WHERE gameid = ? AND jobid = ?
                    ORDER BY bid_amount ASC
                    """;

                List<Map<String, Object>> bids = jdbcTemplate.queryForList(bidsSql, gameId, jobId);
              

                if (bids.isEmpty()) continue;

                int winnerId = -1;
                double winningBid = Double.MAX_VALUE;

                for (Map<String, Object> bid : bids) {
                    int companyId = ((Number) bid.get("companyid")).intValue();
                    double bidAmount = ((Number) bid.get("bid_amount")).doubleValue();

                   

                    if (bidAmount < minimumBid) {
                       
                        jdbcTemplate.update(
                            "UPDATE public.bid SET outcome_code = 2 WHERE gameid = ? AND jobid = ? AND companyid = ?",
                            gameId, jobId, companyId);
                    } else if (bidAmount < winningBid) {
                       
                        winningBid = bidAmount;
                        winnerId = companyId;
                    }
                }

                if (winnerId != -1) {
                    // Get current period for period_awarded
                    int currentPeriod = jdbcTemplate.queryForObject(
                        "SELECT current_period FROM public.game WHERE gameid = ?",
                        Integer.class, gameId);

                    // Award job to winner with period_awarded
                    jdbcTemplate.update(
                        "UPDATE public.job SET companyid = ?, period_awarded = ? WHERE jobid = ? AND gameid = ?",
                        winnerId, currentPeriod, jobId, gameId);

                    jdbcTemplate.update(
                        "UPDATE public.bid SET outcome_code = 1 WHERE gameid = ? AND jobid = ? AND companyid = ?",
                        gameId, jobId, winnerId);

                    jdbcTemplate.update(
                        "UPDATE public.bid SET outcome_code = 6 WHERE gameid = ? AND jobid = ? AND companyid != ? AND outcome_code = 0",
                        gameId, jobId, winnerId);

                    jdbcTemplate.update(
                        "UPDATE public.company SET cash_on_hand = cash_on_hand - ? WHERE companyid = ? AND gameid = ?",
                        winningBid, winnerId, gameId);
                }
            }

        } catch (Exception e) {
            System.err.println("evaluateBids error: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * Retrieves a list of all awarded bids for a specific game and period.
     *
     * This method queries the database for jobs that have been awarded (outcome_code = 1)
     * in the specified game and period. It returns a list of maps where each map contains:
     *   - jobid:           The ID of the job
     *   - job_type_id:     The type/category of the job
     *   - bid_amount:      The amount of the winning bid
     *   - company_title:   The name/title of the winning company
     *
     * @param gameId         The unique identifier for the game session
     * @param periodAwarded  The period in which the bid was awarded
     * @return List<Map<String, Object>> containing awarded job/bid/company info; empty list on error
     */
    public List<Map<String, Object>> getBidOpeningReport(int gameId, int periodAwarded) {
        try {
            // SQL query to fetch job info, winning bid amount, and winning company name
            String sql = """
                SELECT j.jobid, j.job_type_id, b.bid_amount, c.title as company_title
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid AND j.gameid = b.gameid
                JOIN public.company c ON b.companyid = c.companyid AND b.gameid = c.gameid
                WHERE j.gameid = ? AND b.outcome_code = 1 AND j.period_awarded = ?
                ORDER BY j.jobid ASC
                """;
            // Execute the query and return the result as a list of maps
            return jdbcTemplate.queryForList(sql, gameId, periodAwarded);
        } catch (Exception e) {
            // Log error message and return an empty list in case of exception
            System.err.println("getBidOpeningReport error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }



    /**
     * Retrieves all bids for every awarded job in a game during a specific period.
     *
     * This method fetches from the database all bids placed by companies for jobs that have been
     * awarded (i.e., where period_awarded matches the requested period) in the specified game.
     * For each bid, the result contains:
     *   - jobid:         The job's unique identifier.
     *   - job_type_id:   The type/category ID of the job.
     *   - bid_amount:    The amount of the bid.
     *   - outcome_code:  The outcome code for the bid (e.g., 1 for awarded/winner, 0 for outbid).
     *   - company_title: The name/title of the company that placed the bid.
     *
     * The returned list is ordered first by jobid (ascending) and then by bid_amount (ascending).
     *
     * @param gameId        The unique game session identifier.
     * @param periodAwarded The period for which jobs were awarded.
     * @return List of maps, each containing bid and company details for awarded jobs. Empty list on error.
     */
    public List<Map<String, Object>> getCompleteListOfBids(int gameId, int periodAwarded) {
        try {
            String sql = """
                SELECT j.jobid, j.job_type_id, b.bid_amount, b.outcome_code, c.title as company_title
                FROM public.job j
                JOIN public.bid b ON j.jobid = b.jobid AND j.gameid = b.gameid
                JOIN public.company c ON b.companyid = c.companyid AND b.gameid = c.gameid
                WHERE j.gameid = ? AND j.period_awarded = ?
                ORDER BY j.jobid ASC, b.bid_amount ASC
                """;
            // Return the list of bids for the specified game and period
            return jdbcTemplate.queryForList(sql, gameId, periodAwarded);
        } catch (Exception e) {
            // Log the error and return an empty list in case of exception
            System.err.println("getCompleteListOfBids error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }




    
}