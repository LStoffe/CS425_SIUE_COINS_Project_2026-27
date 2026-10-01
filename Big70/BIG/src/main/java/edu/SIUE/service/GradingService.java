package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/*
 * GradingService.java
 *
 * Handles company scoring and ranking logic for the COINS simulation.
 * Calculates scores in 4 standard categories and ranks companies within a game.
 */
@Service
public class GradingService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Calculates scores and ranks for a company in all 4 categories.
     */
    public List<Map<String, Object>> getCompanyScores(int gameId, int companyId) {
        List<Map<String, Object>> scores = new java.util.ArrayList<>();

        // Category 1 — Financial Performance (cash on hand)
        scores.add(calculateCategory(
            gameId, companyId,
            "Financial Performance",
            "Current cash on hand",
            """
                SELECT companyid, cash_on_hand as score
                FROM public.company
                WHERE gameid = ?
                ORDER BY cash_on_hand DESC
            """
        ));

        // Category 2 — Bidding Activity (total bids - dismissed bids)
        scores.add(calculateCategory(
            gameId, companyId,
            "Bidding Activity",
            "Total bids placed minus disqualified bids",
            """
                SELECT b.companyid,
                    COUNT(*) - SUM(CASE WHEN b.outcome_code = 2 THEN 1 ELSE 0 END) as score
                FROM public.bid b
                WHERE b.gameid = ?
                GROUP BY b.companyid
                ORDER BY score DESC
            """
        ));

        // Category 3 — Job Success (jobs won)
        scores.add(calculateCategory(
            gameId, companyId,
            "Job Success",
            "Total number of jobs awarded",
            """
                SELECT companyid, COUNT(*) as score
                FROM public.job
                WHERE gameid = ? AND companyid IS NOT NULL
                GROUP BY companyid
                ORDER BY score DESC
            """
        ));

        // Category 4 — Project Completion (jobs completed)
        scores.add(calculateCategory(
            gameId, companyId,
            "Project Completion",
            "Total number of jobs completed",
            """
                SELECT companyid, COUNT(*) as score
                FROM public.job
                WHERE gameid = ? AND companyid IS NOT NULL
                AND is_active = false AND period_completed > 0
                GROUP BY companyid
                ORDER BY score DESC
            """
        ));

        return scores;
    }

    /**
     * Calculates score and rank for a single category.
     */
    private Map<String, Object> calculateCategory(int gameId, int companyId,
                                                    String label, String formula,
                                                    String sql) {
        try {
            List<Map<String, Object>> allScores = jdbcTemplate.queryForList(sql, gameId);

            int totalCompanies = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.company WHERE gameid = ?",
                Integer.class, gameId);

            // Find this company's score and rank
            double companyScore = 0;
            int rank = totalCompanies; // default to last if not found

            for (int i = 0; i < allScores.size(); i++) {
                Map<String, Object> row = allScores.get(i);
                int rowCompanyId = ((Number) row.get("companyid")).intValue();
                if (rowCompanyId == companyId) {
                    companyScore = ((Number) row.get("score")).doubleValue();
                    rank = i + 1;
                    break;
                }
            }

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("label", label);
            result.put("formula", formula);
            result.put("score", companyScore);
            result.put("rank", rank);
            result.put("totalCompanies", totalCompanies);
            return result;

        } catch (Exception e) {
            System.err.println("calculateCategory error for " + label + ": " + e.getMessage());
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("label", label);
            result.put("formula", formula);
            result.put("score", 0);
            result.put("rank", 0);
            result.put("totalCompanies", 0);
            return result;
        }
    }
}