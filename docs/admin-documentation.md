# COINS (BIG70) Administrator Documentation

## Table of Contents
1. [System Overview](#1-system-overview)
2. [First Login & Admin Account Management](#2-first-login--admin-account-management)
3. [Game Management](#3-game-management)
4. [Period Advancement](#4-period-advancement)
5. [What Happens Each Period Advance](#5-what-happens-each-period-advance)
6. [Troubleshooting](#6-troubleshooting)
7. [Known Limitations](#7-known-limitations)
8. [Tech Stack Reference](#8-tech-stack-reference)

---

## 1. System Overview

COINS (BIG70) is a web-based construction business simulation. It is accessed through a web browser — no installation is required for end users.

### Accessing the Application
| Service   | URL                        | Notes                        |
|-----------|----------------------------|------------------------------|
| COINS App | http://localhost:8080       | Main application — replace `localhost` with your server address when hosted |
| pgAdmin   | http://localhost:5050       | Database management UI |

### Default Admin Credentials
| Username  | Password    |
|-----------|-------------|
| `admin`   | `admin1357` |

> **Important:** Change the default admin password after first login using the Change Password option in the Admin Menu if necessary.

### User Roles
There are two types of users in COINS:

- **Admin** — Manages games, companies, and period advancement. Accessed via the Admin Menu.
- **Company** — Participates in the simulation by bidding on jobs, managing finances, and tracking progress. Accessed via the Company Menu.

---

## 2. First Login & Admin Account Management

### Logging In
1. Navigate to the application URL
2. Click **Login** in the navigation bar
3. Enter your admin username and password
4. You will be redirected to the Admin Menu

### Registering a New Admin
1. From the Admin Menu, click **Register New Admin**
2. Fill in the required fields:
   - Username
   - Password
   - Confirm Password
   - Email
3. Click **Register**

> **Note:** There are two admin roles — **Head Admin** (the default `admin` account) and **Regular Admin**. All subsequently registered admins are regular admins.

### Changing Admin Password
1. From the Admin Menu, click **Change Password**
2. Enter your current password
3. Enter and confirm your new password
4. Click **Save Changes**

### Changing Admin Email
1. From the Admin Menu, click **Change Email**
2. Enter your new email address
3. Click **Save Changes**

---

## 3. Game Management

### Creating a New Game
1. From the Admin Menu, click **Create New Game**
2. Fill in the required fields:
   - **Game Name** — A unique name for the game session (max 20 characters)
   - **Start Year** — The in-game starting year (e.g., 2020)
   - **Game Type** — Select one or both:
     - **Building Construction** — Generates apartment, school, office, hospital, and industrial jobs
     - **Heavy Construction** — Generates highway, bridge, site development, mass excavation, and underground utility jobs
3. Click **Create Game**

> **Note:** 20 jobs are automatically generated when a game is created so companies have something to bid on immediately.

### Recalling a Game
1. From the Admin Menu, click **View Games**
2. Find the game you want to manage and click **Recall**
3. You will be taken to the Game Menu for that session

> Recalling a game sets it as the active game session for the admin. All game management actions apply to the recalled game.

### Adding Companies to a Game
1. Recall the game you want to add companies to
2. From the Game Menu, click **View Companies**
3. Click **Add New Company**
4. Fill in the required fields:
   - **Username** — Company login username
   - **Password** — Company login password
   - **Company Title** — Display name for the company
   - **Cash on Hand** — Starting cash (recommended: $10,000,000)
   - **WIP Limit** — Work in progress bond limit (recommended: $15,000,000)
   - **Per Job Limit %** — Per job limit as a percentage of WIP limit (recommended: 35%)
5. Click **Register Company**

### Removing Companies
1. Recall the game
2. From the Game Menu, click **View Companies**
3. Find the company you want to remove and click **Remove**

> **Warning:** Removing a company permanently deletes all associated data including bids, loans, and personnel records. This action cannot be undone.

### Ending a Game
1. Recall the game
2. From the Game Menu, click **End Game**
3. Confirm the deletion on the confirmation page

> **Warning:** Ending a game permanently deletes all game data including companies, jobs, bids, loans, and personnel records. This action cannot be undone.

---

## 4. Period Advancement

### How to Advance the Period
1. Recall the game you want to advance
2. From the Game Menu, click **Advance Period**
3. Confirm the action
4. The system will process all period events automatically and return you to the Game Menu

### Game Period Date System
Each period in COINS represents **two months** of in-game time. The period date is calculated from the game's start year:

- Period 0 = January & February of start year
- Period 1 = March & April of start year
- Period 2 = May & June of start year
- And so on...

> Period advancement is **manual only** in this version. The admin must click Advance Period to move the simulation forward.

---

## 5. What Happens Each Period Advance

When an admin advances the period, the following events occur **in this order**:

### 1. Bid Evaluation
- All pending bids are evaluated for active jobs
- The **lowest qualified bid** wins the contract
- Bids below 80% of the job's direct cost are automatically disqualified
- The winning company has the bid amount deducted from their cash on hand
- All companies are notified of bid outcomes on their Bid Opening Report

### 2. Job Income
- Companies with active jobs receive income each period
- Income per period = `bid amount / estimated periods to complete`
- Jobs automatically complete after their estimated duration and stop generating income
- Job completion speed is affected by the company's chosen construction method

### 3. Loan Repayments
- All active company loans are automatically repaid
- Each repayment = `(original loan amount / 7) + 2% interest on remaining balance`
- If a company has insufficient funds the balance goes negative

### 4. Payroll
- All employee salaries are automatically deducted from company cash on hand
- Costs are based on the number and type of employees the company currently employs
- Employee counts carry over to the next period automatically

### 5. Job Expiry
- Any unclaimed jobs whose completion deadline has passed are marked inactive
- Jobs with active bids are protected from expiry until bids are evaluated

### 6. New Job Generation
- New jobs are generated for the new period based on game parameters
- The number of jobs is randomized using a normal distribution
- A maximum of 40 available jobs can exist at any time
- Job types are determined by the game type (building, heavy, or both)

### 7. Period Counter Update
- The game's current period is incremented by 1

---

## 6. Troubleshooting

### Companies Report Incorrect Cash on Hand
- Cash on hand updates automatically each period advance
- Verify the period has been advanced recently
- Check the company's loan balance and payroll costs — these are deducted automatically

### Companies Cannot Log In
- Verify the company was added to the correct game
- The admin can remove and re-add the company with a new password if needed

### Jobs Not Appearing for Bidding
- Jobs are generated automatically on game creation and each period advance
- If no jobs appear, try advancing the period
- A maximum of 40 jobs can exist at one time — if the cap is reached no new jobs will generate until existing jobs are awarded or expire

### Bids Not Being Awarded
- Bids are only evaluated when the admin advances the period
- A bid may be disqualified if it is below 80% of the job's direct cost
- Companies can check their Bid Opening Report after period advancement to see outcomes

### Database or Application Issues
- Contact your system administrator or refer to the deployment documentation for server-level troubleshooting
- Application logs are available via `docker logs -f big_app` if Docker is accessible

---

## 7. Known Limitations

The following features from the original Big60 system are not yet implemented in this version:

| Feature | Status | Notes |
|---------|--------|-------|
| Inbox / Messaging | Not implemented | Companies cannot receive automated messages |
| Historical Period Reports | Not implemented | Reports only show current period data |
| Job Work Simulation | Simplified | Progress is time-based, not activity-based |
| Email Notifications | Not implemented | No automated email alerts |
| Appraisal Metrics (CAM) | Not implemented | CAM scores are not automatically updated |
| WIP Bond Limit Enforcement | Not implemented | Companies are not prevented from exceeding WIP limits when bidding |
| Automatic Period Scheduling | Not implemented | Period advancement is manual only |
| Auto-Add Jobs to Companies | Not implemented | Jobs are generated for bidding only, not auto-awarded |
| Game Component Toggles | Not implemented | Estimating and billing component toggles are disabled |
| Custom Grading Formulas | Not implemented | Fixed 4-category scoring is used instead |

---

## 8. Tech Stack Reference

| Component | Technology |
|-----------|------------|
| Backend Framework | Spring Boot 3.2.4 (Java 21) |
| Frontend Templating | Thymeleaf + Tailwind CSS |
| Database | PostgreSQL 16 |
| Legacy Integration | coins-backend.jar (Big60) |
| Containerization | Docker + Docker Compose |
| Build Tool | Maven |

---

*COINS BIG70*