package edu.SIUE.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * ManualController.java
 *
 * Controller responsible for serving all pages related to the user manual within the BIG simulation platform.
 *
 * Responsibilities:
 *   - Maps HTTP GET requests to manual section templates for user documentation.
 *   - Groups all manual and help documentation under the "/manual" URL namespace.
 *   - Allows navigation of the manual through organized route mappings for each section or topic.
 * 
 * Developed as part of the SIUE BIG simulation web application.
 */

@Controller
@RequestMapping("/manual") // Base URL mapping for all manual-related pages
public class ManualController {

     // the Manual Table of Contents page
    @GetMapping("/manual-toc")
    public String manualTOC() {
        return "manual/manual-toc"; // templates/manual/manual-toc.html
    }

    // SECTION 1 CONTROLLERS

    // Mapping for the Section 1 - Getting Started 
    @GetMapping("/getting-started")
    public String gettingStarted() {
        return "manual/section-1/getting-started";  // manual/section-1/getting-started.html
    }

    // Mapping for Section 1.1 - Introduction
    @GetMapping("/introduction")
    public String introduction() {
        return "manual/section-1/introduction"; // manual/section-1/introduction.html
    }

    // Mapping for Section 1.2 - Login-Logout
    @GetMapping("/login-logout")
    public String loginLogout() {
        return "manual/section-1/login-logout"; // manual/section-1/login-logout.html
    }

     // Mapping for Section 1.3 - Company-information
    @GetMapping("/company-information")
    public String companyInformation() {
        return "manual/section-1/company-information"; // manual/section-1/company-information.html
    }

    // Mapping for Section 1.4 - Big Basics
    @GetMapping("/big-basics")
    public String bigBasics() {
        return "manual/section-1/big-basics"; // manual/section-1/big-basics.html
    }

    // Mapping for Section 1.5 - Interacting with BIG
    @GetMapping("/interacting-with-big")
    public String interactingWithBig() {
        return "manual/section-1/interacting-with-big"; // manual/section-1/interacting-with-big.html
    }

    // Mapping for Section 1.6 - Company Inbox
    @GetMapping("/company-inbox")
    public String companyInbox() {
        return "manual/section-1/company-inbox"; // manual/section-1/company-inbox.html
    }

    // Mapping for Section 1.7 - Job Information
    @GetMapping("/job-information")
    public String jobInformation() {
        return "manual/section-1/job-information"; // manual/section-1/job-information.html
    }

    // Mapping for Section 1.8 - Productivity Report
    @GetMapping("/productivity-report")
    public String productivityReport() {
        return "manual/section-1/productivity-report"; // manual/section-1/productivity-report.html
    }

    // Mapping for Section 1.9 Available Jobs
    @GetMapping("/available-jobs")
    public String availableJobs() {
        return "manual/section-1/available-jobs"; // manual/section-1/available-jobs.html
    }

     // Mapping for Section 1.10 Estimated Time and Cost Report
    @GetMapping("/estimated-time-and-cost-report")
    public String estimatedTimeAndCostReport() {
        return "manual/section-1/estimated-time-and-cost-report"; // manual/section-1/estimated-time-and-cost-report.html
    }

    // Mapping for Section 1.11 Network Depdendencies Reports
    @GetMapping("/network-dependencies-reports")
    public String networkDependenciesReports() {
        return "manual/section-1/network-dependencies-reports"; // manual/section-1/network-dependencies-reports.html
    }

    // SECTION 2 CONTROLLERS 

    // Mapping for Section 2 - Bidding
    @GetMapping("/bidding")
    public String bidding() {
        return "manual/section-2/bidding"; // manual/section-2/network-dependencies-reports.html
    }

    // Mapping for Section 2.1 Scheduling Methods
    @GetMapping("/scheduling-methods")
    public String schedulingMethods() {
        return "manual/section-2/scheduling-methods"; // manual/section-2/scheduling-methods.html
    }

    // Mapping for Section 2.2 Bidding Form
    @GetMapping("/bidding-form")
    public String biddingForm() {
        return "manual/section-2/bidding-form"; // manual/section-2/bidding-form.html
    }

    // Mapping for Section 2.3 Submitting Bids
    @GetMapping("/submitting-bids")
    public String submittingBids() {
        return "manual/section-2/submitting-bids"; // manual/section-2/submitting-bids.html
    }

    // Mapping for Section 2.4 Bond Limits
    @GetMapping("/bond-limits")
    public String bondLimits() {
        return "manual/section-2/bond-limits"; // manual/section-2/bond-limits.html
    }

    // Mapping for Section 2.5 Negotiation Offers
    @GetMapping("/negotiation-offers")
    public String negotiationOffers() {
        return "manual/section-2/negotiation-offers"; // manual/section-2/negotiation-offers.html
    }

    // Mapping for Section 2.6 Bid Closing
    @GetMapping("/bid-closing")
    public String bidClosing() {
        return "manual/section-2/bid-closing"; // manual/section-2/bid-closing.html
    }

    // Mapping for Section 2.7 Bidding Results
    @GetMapping("/bidding-results")
    public String biddingResults() {
        return "manual/section-2/bidding-results"; // manual/section-2/bidding-results.html
    }

    // Mapping for Section 2.8 Progress Report
    @GetMapping("/progress-report")
    public String progressReport() {
        return "manual/section-2/progress-report"; // manual/section-2/progress-report.html
    }

    // SECTION 3 CONTROLLERS

    // Mapping for Section 3 - Accounting Reports
    @GetMapping("/accounting-reports")
    public String accountingReports() {
        return "manual/section-3/accounting-reports"; // manual/section-3/accounting-reports.html
    }

    // Mapping for Section 3.1 Job Cost Report
    @GetMapping("/job-cost-report")
    public String jobCostReport() {
        return "manual/section-3/job-cost-report"; // manual/section-3/job-cost-report.html
    }

    // Mapping for Section 3.2 Cash Flow Report
    @GetMapping("/cash-flow-report")
    public String cashFlowReport() {
        return "manual/section-3/cash-flow-report"; // manual/section-3/cash-flow-report.html
    }

    // Mapping for Section 3.3 Contracts in Progress Report
    @GetMapping("/contracts-in-progress")
    public String contractsInProgress() {
        return "manual/section-3/contracts-in-progress"; // manual/section-3/contracts-in-progress.html
    }

    // Mapping for Section 3.4 Completed Contracts
    @GetMapping("/completed-contracts")
    public String completedContracts() {
        return "manual/section-3/completed-contracts"; // manual/section-3/completed-contracts.html
    }

    // Mapping for Section 3.5 Income Statement
    @GetMapping("/statement-of-earnings")
    public String statementOfEarnings() {
        return "manual/section-3/statement-of-earnings"; // manual/section-3/statement-of-earnings.html
    }

    // Mapping for Section 3.6 Balance Sheet
    @GetMapping("/balance-sheet")
    public String balanceSheet() {
        return "manual/section-3/balance-sheet"; // manual/section-3/balance-sheet.html
    }

    // SECTION 4 CONTROLLERS

    // Mapping for Section 4 - Consulting Services
    @GetMapping("/consulting-services")
    public String consultingServices() {
        return "manual/section-4/consulting-services"; // manual/section-4/consulting-services.html
    }

    // Mapping for Section 4.1 Labor Availability Index
    @GetMapping("/labor-availability-index")
    public String laborAvailabilityIndex() {
        return "manual/section-4/labor-availability-index"; // manual/section-4/labor-availability-index.html
    }

    // Mapping for Section 4.2 Materials Cost Index
    @GetMapping("/materials-cost-index")
    public String materialsCostIndex() {
        return "manual/section-4/materials-cost-index"; // manual/section-4/materials-cost-index.html
    }

    // Mapping for Section 4.3 Weather Forecast
    @GetMapping("/weather-forecast")
    public String weatherForecast() {
        return "manual/section-4/weather-forecast"; // manual/section-4/weather-forecast.html
    }

    // Mapping for Section 4.4 Market Analysis
    @GetMapping("/market-analysis")
    public String marketAnalysis() {
        return "manual/section-4/market-analysis"; // manual/section-4/market-analysis.html
    }

    // SECTION 5 CONTROLLERS

    // Mapping for Section 5 - Project Administration
    @GetMapping("/project-administration")
    public String projectAdministration() {
        return "manual/section-5/project-administration"; // manual/section-5/project-administration.html
    }

    // Mapping for Section 5.1 Liquidated Damages
    @GetMapping("/liquidated-damages")
    public String liquidatedDamages() {
        return "manual/section-5/liquidated-damages"; // manual/section-5/liquidated-damages.html
    }

    // Mapping for Section 5.2 Change Orders
    @GetMapping("/change-orders")
    public String changeOrders() {
        return "manual/section-5/change-orders"; // manual/section-5/change-orders.html
    }

    // Mapping for Section 5.3 Retention
    @GetMapping("/retention")
    public String retention() {
        return "manual/section-5/retention"; // manual/section-5/retention.html
    }

    // Mapping for Section 5.4 Subcontracting
    @GetMapping("/subcontracting")
    public String subcontracting() {
        return "manual/section-5/subcontracting"; // manual/section-5/subcontracting.html
    }

    // Mapping for Section 5.5 Method Rescheduling
    @GetMapping("/method-rescheduling")
    public String methodRescheduling() {
        return "manual/section-5/method-rescheduling"; // manual/section-5/method-rescheduling.html
    }

    // Mapping for Section 5.6 Overtime
    @GetMapping("/overtime")
    public String overtime() {
        return "manual/section-5/overtime"; // manual/section-5/overtime.html
    }

    // Mapping for Section 5.7 Loans
    @GetMapping("/loans")
    public String loans() {
        return "manual/section-5/loans"; // manual/section-5/loans.html
    }

    // Mapping for Section 5.8 Billing
    @GetMapping("/billing")
    public String billing() {
        return "manual/section-5/billing"; // manual/section-5/billing.html
    }

    // Mapping for Section 5.9 Overbilling
    @GetMapping("/overbilling")
    public String overbilling() {
        return "manual/section-5/overbilling"; // manual/section-5/overbilling.html
    }

    // Mapping for Section 5.10 Income Tax
    @GetMapping("/income-tax")
    public String incomeTax() {
        return "manual/section-5/income-tax"; // manual/section-5/income-tax.html
    }

    // Mapping for Section 5.11 Company Appraisal Metrics
    @GetMapping("/company-appraisal-metrics")
    public String companyAppraisalMetrics() {
        return "manual/section-5/company-appraisal-metrics"; // manual/section-5/company-appraisal-metrics.html
    }

    // Mapping for Section 5.12 Main Office Personnel
    @GetMapping("/main-office-personnel")
    public String mainOfficePersonnel() {
        return "manual/section-5/main-office-personnel"; // manual/section-5/main-office-personnel.html
    }

    // Mapping for Section 5.13 Completing BIG
    @GetMapping("/completing-big")
    public String completingBIG() {
        return "manual/section-5/completing-big"; // manual/section-5/completing-big.html
    }

    // Add additional sections here as needed
}
