# Construction Industry Simulation (COINS)

## Overview
COINS, also known as BIG (Building Industry Game), is a web-based construction management simulation used in academic settings to teach students strategic decision-making, financial management, and competitive analysis within the construction industry.

This repository contains the modernized BIG70 implementation, a full rewrite of the legacy Big60 system.

## Legacy Stack (Big60)
- **Frameworks** — Apache Struts, JSP/JSTL, iBATIS
- **Database** — PostgreSQL, JDBC, Apache Commons DBCP
- **Build Tools** — Apache Ant
- **Utilities** — Log4j, Apache Commons libraries
- **UI** — Static HTML/CSS/JavaScript

## Modern Stack (BIG70)
- **Framework** — Spring Boot 3.2.4 (Java 21)
- **Templating** — Thymeleaf
- **Database** — PostgreSQL 16 with plain JDBC (JdbcTemplate)
- **Build Tool** — Maven
- **Security** — BCrypt password hashing, session-based role access control
- **UI** — Tailwind CSS
- **Deployment** — Docker + Docker Compose
- **Legacy Integration** — coins-backend.jar (original Big60 JAR, hybrid approach)

## Getting Started
See [Big70/BIG/README.md](Big70/BIG/README.md) for full setup instructions.


## Project Status
The following features are fully implemented:
- Admin game management (create, recall, delete games)
- Company registration and login
- Job generation based on game type (building, heavy, or both)
- Competitive bidding system with bid evaluation
- Period advancement (bids, income, loans, payroll, job expiry, job generation)
- Personnel management (hire/fire employees, payroll deduction)
- Loan management (request, repayment with interest)
- Job method scheduling (Standard, Accelerated, Overtime, Economy)
- Consulting services reports (weather, materials, labor, future demand)
- Financial reports (balance sheet, income statement, cash flow, job cost, ratios)
- Company scoring and ranking (4 categories)
- Docker containerized deployment

The following features are not yet implemented:
- Inbox/messaging system
- Automatic period advancement scheduling
- Activity-based work simulation
- WIP bond limit enforcement
- Custom grading formula editor
- Appraisal metrics (CAM) auto-update

## Test Reports
Legacy Big60 test reports (JUnit + JaCoCo) are available in `docs/test-reports/`.

## Useful Links
**COINS** — coinsim.org/big50