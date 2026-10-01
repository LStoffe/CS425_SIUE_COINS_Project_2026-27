package edu.SIUE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * LoanService.java
 *
 * Service class responsible for managing all business logic related to company loans
 * within the BIG simulation platform. This includes:
 *   - Processing new loan requests with proper validation (amount limits, max active loans, one-per-period)
 *   - Managing loan repayments and payoff (scheduled payments and early repayment)
 *   - Retrieving loan details and enforcing related game rules
 *
 * Developed as part of the SIUE BIG simulation web application.
 */


@Service
public class LoanService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Requests a new loan for a company.
     * Validates max loan amount, max active loans, and one loan per period.
     *
     * @return null if successful, or an error message string if validation fails
     */
    public String requestLoan(int gameId, int companyId, int currentPeriod, double loanAmount) {
        try {
            // Validate max loan amount
            if (loanAmount <= 0 || loanAmount > 10000000) {
                return "Loan amount must be between $1 and $10,000,000.";
            }

            // Check active loan count — max 3
            int activeLoanCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.loan WHERE gameid = ? AND companyid = ? AND payments_remaining > 0",
                Integer.class, gameId, companyId);

            if (activeLoanCount >= 3) {
                return "You already have 3 active loans. You cannot request another loan at this time.";
            }

            // Check if already requested a loan this period
            int thisPeriodCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM public.loan WHERE gameid = ? AND companyid = ? AND periodid = ?",
                Integer.class, gameId, companyId, currentPeriod);

            if (thisPeriodCount > 0) {
                // Get the existing loan amount for this period
                double existingAmount = jdbcTemplate.queryForObject(
                    "SELECT amount FROM public.loan WHERE gameid = ? AND companyid = ? AND periodid = ?",
                    Double.class, gameId, companyId, currentPeriod);

                // Update the loan record
                jdbcTemplate.update(
                    "UPDATE public.loan SET amount = ?, balance = ?, payment_amount = ? WHERE gameid = ? AND companyid = ? AND periodid = ?",
                    loanAmount, loanAmount, loanAmount / 7.0, gameId, companyId, currentPeriod);

                // Only add the DIFFERENCE to cash on hand
                double difference = loanAmount - existingAmount;
                if (difference != 0) {
                    jdbcTemplate.update(
                        "UPDATE public.company SET cash_on_hand = cash_on_hand + ? WHERE companyid = ? AND gameid = ?",
                        difference, companyId, gameId);
                }

            } else {
                // Get next loan ID
                int loanId = jdbcTemplate.queryForObject(
                    "SELECT COALESCE(MAX(loanid), 0) + 1 FROM public.loan", Integer.class);

                // Insert new loan
                jdbcTemplate.update("""
                    INSERT INTO public.loan 
                    (loanid, gameid, companyid, periodid, amount, balance, 
                    payment_amount, interest_paid, payments_remaining, is_approved, repay_immediately)
                    VALUES (?, ?, ?, ?, ?, ?, ?, 0, 7, true, false)
                    """,
                    loanId, gameId, companyId, currentPeriod,
                    loanAmount, loanAmount, loanAmount / 7.0);

                // Add full loan amount to cash on hand immediately
                jdbcTemplate.update(
                    "UPDATE public.company SET cash_on_hand = cash_on_hand + ? WHERE companyid = ? AND gameid = ?",
                    loanAmount, companyId, gameId);
            }

            return null; // null means success

        } catch (Exception e) {
            System.err.println("requestLoan error: " + e.getMessage());
            return "An error occurred while processing your loan request.";
        }
    }




    /**
     * Processes loan repayments for all active loans in a game when period advances.
     * Deducts 1/7 principal + 2% interest on remaining balance from cash on hand.
     */
    public void processLoanRepayments(int gameId) {
        try {
            // Get all active loans for this game
            String sql = """
                SELECT loanid, companyid, balance, payment_amount, payments_remaining
                FROM public.loan
                WHERE gameid = ? AND payments_remaining > 0
                """;

            List<Map<String, Object>> loans = jdbcTemplate.queryForList(sql, gameId);

            for (Map<String, Object> loan : loans) {
                int loanId = ((Number) loan.get("loanid")).intValue();
                int companyId = ((Number) loan.get("companyid")).intValue();
                double balance = ((Number) loan.get("balance")).doubleValue();
                double paymentAmount = ((Number) loan.get("payment_amount")).doubleValue();
                int paymentsRemaining = ((Number) loan.get("payments_remaining")).intValue();

                // Calculate interest on remaining balance (2% per period)
                double interest = Math.round(balance * 0.02 * 100.0) / 100.0;
                double totalPayment = paymentAmount + interest;

                // Deduct payment from company cash on hand
                jdbcTemplate.update(
                    "UPDATE public.company SET cash_on_hand = cash_on_hand - ? WHERE companyid = ? AND gameid = ?",
                    totalPayment, companyId, gameId);

                // Update loan balance and payments remaining
                double newBalance = Math.round((balance - paymentAmount) * 100.0) / 100.0;
                int newPaymentsRemaining = paymentsRemaining - 1;

                jdbcTemplate.update("""
                    UPDATE public.loan 
                    SET balance = ?, payments_remaining = ?, interest_paid = interest_paid + ?
                    WHERE loanid = ?
                    """,
                    newBalance, newPaymentsRemaining, interest, loanId);
            }

        } catch (Exception e) {
            System.err.println("processLoanRepayments error: " + e.getMessage());
            e.printStackTrace();
        }
    }




    /**
     * Processes immediate full repayment of all loans for a company.
     */
    public String repayAllLoans(int gameId, int companyId) {
        try {
            // Get total remaining balance
            Double totalBalance = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(balance), 0) FROM public.loan WHERE gameid = ? AND companyid = ? AND payments_remaining > 0",
                Double.class, gameId, companyId);

            if (totalBalance == null || totalBalance <= 0) {
                return "No active loans to repay.";
            }

            // Check company has enough cash
            Double cashOnHand = jdbcTemplate.queryForObject(
                "SELECT cash_on_hand FROM public.company WHERE companyid = ? AND gameid = ?",
                Double.class, companyId, gameId);

            if (cashOnHand < totalBalance) {
                return "Insufficient cash on hand to repay all loans. Balance: $" + totalBalance;
            }

            // Deduct total balance from cash on hand
            jdbcTemplate.update(
                "UPDATE public.company SET cash_on_hand = cash_on_hand - ? WHERE companyid = ? AND gameid = ?",
                totalBalance, companyId, gameId);

            // Mark all loans as paid off
            jdbcTemplate.update(
                "UPDATE public.loan SET balance = 0, payments_remaining = 0 WHERE gameid = ? AND companyid = ? AND payments_remaining > 0",
                gameId, companyId);

            return null; // null means success

        } catch (Exception e) {
            System.err.println("repayAllLoans error: " + e.getMessage());
            return "An error occurred while processing your repayment.";
        }
    }


    

    /**
     * Gets all active loans for a company.
     */
    public List<Map<String, Object>> getActiveLoans(int gameId, int companyId) {
        try {
            String sql = """
                SELECT loanid, amount, balance, payment_amount, 
                       interest_paid, payments_remaining, periodid
                FROM public.loan
                WHERE gameid = ? AND companyid = ? AND payments_remaining > 0
                ORDER BY loanid ASC
                """;
            return jdbcTemplate.queryForList(sql, gameId, companyId);
        } catch (Exception e) {
            System.err.println("getActiveLoans error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}