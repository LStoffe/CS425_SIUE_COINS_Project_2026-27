# COINS Codebase Structure

This document outlines the current structure of the `COINS-Project` repository and describes the main folders and files, focusing on the `Big70/BIG` Spring Boot application.

_Last reviewed: 04/2026_

## Repository Root

- `.git/` — Git metadata for version control.
- `README.md` — Project overview, tech stack, project status, and modernization notes.
- `Big70/` — Main application and supporting assets.
- `docs/` — Documentation files, ERD, test reports, and admin documentation.
- `git-log.txt` — Commit history export.

_There are currently **no** `.github/`, `.gitlab-ci.yml/`, or dedicated CI/CD configuration files. Docker Compose is used for local development, testing, and client deployment._

---

## `Big70/BIG`

This directory contains the full BIG70 Spring Boot application.

### Key Files

- `docker-compose.yml` — Orchestrates PostgreSQL 16, the Spring Boot app, and pgAdmin containers.
- `dockerfile` — Defines the Spring Boot service container build (Node for Tailwind, Maven for Java, JRE to run).
- `pom.xml` — Maven project and dependency configuration.
- `mvnw` / `mvnw.cmd` — Maven wrapper scripts for portability.
- `tailwind.config.js` / `package.json` / `package-lock.json` — Tailwind CSS build configuration.
- `README.md` — Docker setup and quick start instructions for running the app.

### Docker and Database Support

- `docker-init-db/` — SQL scripts loaded automatically by PostgreSQL on first startup, in alphabetical order.
  - `01_schema.sql` — Full database schema, all tables, sequences, constraints, default game parameters, and the default head admin account.
  - `02_seed.sql` — Creates the `postgres` superuser role if it does not already exist. Required because `01_schema.sql` was exported from a server where `postgres` is the default table owner.

### Libraries

- `libs/` — Contains `coins-backend.jar`, the original Big60 legacy JAR. Used for hybrid integration where the original database handlers are called directly where the schema matches.

---

## `Big70/BIG/src`

All source code, resources, templates, and configuration.

### `src/main/java/edu/SIUE`

- `CoinsProjectApplication.java` — Spring Boot application entry point.

#### `config/`
- `LegacyConfig.java` — Registers `BIGDatabaseFacadeInterface` as a Spring bean for legacy JAR integration.
- `PasswordConfig.java` — Defines the `BCryptPasswordEncoder` bean used for password hashing.

#### `controller/`
All controllers use session-based authentication checks. No REST API — all endpoints are server-rendered via Thymeleaf.

- `LoginController.java` — Handles login/logout for both admin and company users.
- `AdminController.java` — Admin panel: game management, company management, period advancement.
- `RecallGameController.java` — Game recall and game menu routing.
- `CompanyController.java` — Company menu, company info editing, rank and score.
- `BiddingController.java` — Job bidding, bid submission, bid reports.
- `ProjectManagementController.java` — Progress report, reschedule methods, payments received.
- `FinancialReportsController.java` — All financial report pages (balance sheet, income statement, etc.).
- `FinancialServicesController.java` — Loan management and personnel management.
- `ConsultingServicesController.java` — Consulting services reports (weather, materials, labor, future demand, appraisal).
- `PublishedInfoController.java` — Public pages (manual, home).

#### `service/`
Business logic and all database access via Spring `JdbcTemplate`. No Spring Data repositories or MyBatis — all queries are plain JDBC.

- `AuthService.java` — Company login authentication with BCrypt and lazy password upgrade.
- `AdminAuthService.java` — Admin login authentication with BCrypt and lazy password upgrade.
- `GameService.java` — Game creation, retrieval, period date string calculation.
- `JobService.java` — Job type names, available jobs, job expiry.
- `JobFactoryService.java` — Job generation, income processing, method scheduling, payment history.
- `BidService.java` — Bid submission, retraction, evaluation, bid opening report, complete list of bids.
- `LoanService.java` — Loan requests, repayment processing.
- `PersonnelService.java` — Hire/fire employees, payroll processing.
- `ConsultingService.java` — Consulting report data (weather, materials cost, labor availability, future demand, appraisal metrics).
- `ReportService.java` — Financial report data (balance sheet, cash flow, income statement, completed contracts, contracts in progress, job cost, ratios).
- `GradingService.java` — Company scoring and ranking in 4 standard categories.
- `CompanyService.java` — Company data retrieval and updates.

#### `model/`
- Domain model classes used primarily for legacy JAR integration (e.g., `Game.java`).

---

### `src/main/resources`

- `application.properties` — Spring Boot core configuration (DB connection, JPA settings).
- `application-local.properties` — Local/dev configuration overrides.
- `ibatis.properties` — Legacy iBATIS configuration used by `coins-backend.jar` for hybrid DB access.

#### `static/`
- `css/tailwind.css` — Compiled Tailwind CSS output.

#### `templates/`
All UI is server-rendered via Thymeleaf. No client-side JS frameworks.

- `login.html` — Unified login page for admin and company users.
- `fragments/` — Shared HTML fragments:
  - `navbar-admin.html` — Admin navigation bar.
  - `navbar-company.html` — Company navigation bar.
  - `footer.html` — Shared footer.
- `admin/` — All admin-facing pages:
  - `recall-game/` — Game menu, view companies, advance period, grading template, etc.
  - `create-new-game.html`, `view-games.html`, `admin-menu.html`, etc.
- `company/` — All company-facing pages:
  - `bidding/` — Bid on a job, bid reports, complete list of bids.
  - `project-management/` — Progress report, reschedule methods, payments received, bill for work.
  - `financial-reports/` — Balance sheet, income statement, cash flow, completed contracts, contracts in progress, job cost, ratios.
  - `consulting-services/` — Weather, materials cost, labor availability, future demand, appraisal metrics.
  - `financial-services/` — Loans, personnel management.
  - `company-menu.html`, `rank-and-score.html`, etc.
- `manual/` — In-game user manual pages.

---

## `docs/`

- `ADMIN_DOCUMENTATION.md` — Full admin handoff documentation covering setup, game management, period advancement, and troubleshooting.
- `erd.png` — Entity Relationship Diagram of the full database schema.
- `test-reports/` — Legacy Big60 JUnit and JaCoCo test reports:
  - `jacoco/` — Code coverage reports (open `index.html` to view).
  - `junit/` — Unit test results.
  - `README.md` — Explains how reports were generated and how to view them.

---

## How Components Fit Together

1. **Docker Compose** spins up PostgreSQL, the Spring Boot app, and pgAdmin. PostgreSQL automatically runs `docker-init-db/` scripts on first startup to initialize the schema and seed data.
2. **Spring Boot** serves all HTML via Thymeleaf and static assets (CSS) via the `static/` folder.
3. **Controllers** handle HTTP routing and session-based authentication, delegating business logic to services.
4. **Services** contain all business logic and database access via `JdbcTemplate`. There are no Spring Data repositories.
5. **Legacy JAR** (`coins-backend.jar`) is integrated via `LegacyConfig.java` for game creation and other operations where the original Big60 schema matches.
6. **BCrypt** password hashing is applied on registration. Legacy plain-text passwords are lazily upgraded on first login.

---

## Known Limitations

See `ADMIN_DOCUMENTATION.md` for a full list of features not yet implemented in this version.

---

_If new services, controllers, or file conventions are introduced, please update this document for discoverability and onboarding._