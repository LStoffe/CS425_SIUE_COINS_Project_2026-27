# BIG Application Source Code Documentation

## Overview
The `src/` folder contains the complete source code for the BIG (Business Information Game) application, including Java source files, JSP pages, SQL scripts, and Tag Library Descriptors. This folder represents the core development codebase that gets compiled and deployed to create the web application.

## Source Code Structure

### **Main Directories:**
- **`java/`** - Java source code (170+ Java files)
- **`web/`** - Web interface files (458+ files including JSPs, images, CSS, JS)
- **`sql/`** - Database schema and data scripts (11 SQL files)
- **`tld/`** - Tag Library Descriptors (16 TLD/DTD files)

## Java Source Code (`src/java/`)

### **Package Structure:**

#### **`edu.calpoly.its.mas.big`** - Main Application Package

##### **`db/` - Database Layer (40+ classes)**
- **Core Database Classes:**
  - `BIGDatabaseFacade.java` - Main database interface implementation
  - `BIGDatabaseFacadeInterface.java` - Database interface contract
  - `BIGDatabaseFacadeException.java` - Database exception handling
  - `ObjectNotFoundException.java` - Object not found exception
  - `ParametersNotFoundException.java` - Parameters not found exception

- **Database Handlers:**
  - `ActivityDBHandler.java` - Activity data operations
  - `AdminDBHandler.java` - Administrator data operations
  - `BidDBHandler.java` - Bidding system data operations
  - `CompanyDBHandler.java` - Company data operations
  - `GameDBHandler.java` - Game data operations
  - `JobDBHandler.java` - Job data operations
  - `LoanDBHandler.java` - Loan data operations
  - `PeriodDBHandler.java` - Period data operations
  - `FinancialReportDBHandler.java` - Financial report data operations

- **Default Implementations:**
  - `Default*DBHandler.java` - Default implementations for all handlers
  - Pattern: Interface + Default implementation for flexibility

##### **`game/` - Game Logic (16 classes)**
- **Core Game Classes:**
  - `Game.java` - Main game entity with state management
  - `Job.java` - Construction job representation
  - `Activity.java` - Construction activity definition
  - `Method.java` - Construction method definition
  - `Period.java` - Game time period management
  - `Loan.java` - Financial loan management

- **Game Management:**
  - `ExpandedGame.java` - Extended game functionality
  - `GameForm.java` - Game form handling
  - `JobFactory.java` - Job creation factory
  - `NetworkDependencies.java` - Project dependency management
  - `ScheduledMonth.java` - Scheduling functionality
  - `NumberConverter.java` - Number formatting utilities

##### **`reports/` - Reporting System (16 classes)**
- **Report Actions:**
  - `CompleteListofBidsReportAction.java` - Bid listing reports
  - `ConsultingReportsAction.java` - Consulting service reports
  - `EstimatedTimeandCostAction.java` - Time and cost estimation
  - `NetworkDependenciesReportAction.java` - Dependency reports
  - `ProductivityReportAction.java` - Productivity analysis
  - `ProgressReportAction.java` - Project progress tracking
  - `ReportFromBidOpeningAction.java` - Bid opening reports

- **Report Forms:**
  - `*ReportForm.java` - Form classes for each report type
  - `JobFinancialInfo.java` - Job financial information
  - `ReportForm.java` - Base report form class

##### **`tags/` - Custom JSP Tags (4 classes)**
- **Authentication Tags:**
  - `CheckLoggedIn.java` - Login verification tag
  - `CheckAdminLoggedIn.java` - Admin verification tag
  - `LoginVerificationTag.java` - General login verification
  - `IncrementNumberOfCosLoggedInTag.java` - Session tracking

##### **`users/` - User Management (100+ classes)**
- **Base User Classes:**
  - `User.java` - Base user class
  - `LoginBroker.java` - Authentication management
  - `LoginAuthenticationAction.java` - Login processing
  - `LoginForm.java` - Login form handling
  - `LoginInitializationServlet.java` - Login initialization
  - `LogoutAction.java` - Logout processing
  - `GameLoginInfo.java` - Game session information
  - `StringConstraints.java` - String validation utilities

- **`admin/` - Administrator Functionality (67+ classes)**
  - Administrative actions and forms
  - Game management functionality
  - System configuration tools
  - User management capabilities

- **`company/` - Company User Functionality (26+ classes)**
  - Company-specific actions and forms
  - Bidding and job management
  - Financial reporting for companies
  - Project management tools

#### **`edu.calpoly.lib.multimedia.big`** - Extended Functionality Package

##### **`db/` - Extended Database (35+ classes)**
- Additional database operations and utilities
- Extended data access patterns
- Advanced database functionality

##### **`game/` - Extended Game Logic (20+ classes)**
- **AI and Automation:**
  - `ComputerControlledContractor.java` - AI contractor implementation
  - `Contractor.java` - Contractor interface
  - `AutoUpdateServlet.java` - Automatic game updates
  - `AutoUpdateThread.java` - Background update processing
  - `CAMUpdater.java` - CAM system updates

- **Advanced Game Features:**
  - `NamedVariableDataFacade.java` - Variable data access
  - `NamedVariableDataFacadeImpl.java` - Implementation
  - `NamedVariableDataFacadeTestImpl.java` - Test implementation
  - `NegotiatedJob.java` - Negotiated job handling
  - `NegotiatedJobFactory.java` - Negotiated job creation
  - `Statistics.java` - Statistical calculations
  - `TaxLedger.java` - Tax management
  - `Utility.java` - Utility functions

##### **`interpreter/` - Formula Parser (7 files)**
- **Parser Generation:**
  - `BIGFormulaGrammar.cup` - CUP grammar definition
  - `BIGFormulaLexicon.jlex` - JLex lexicon definition
  - Generated parser and scanner classes

##### **`log/` - Custom Logging (2 classes)**
- `BIGLog.java` - Custom logging implementation
- `BIGLogFactory.java` - Logging factory

##### **`personnel/` - Personnel Management (11+ classes)**
- Employee management functionality
- Personnel tracking and reporting
- HR-related operations

##### **`reports/` - Extended Reporting (23+ classes)**
- Advanced reporting capabilities
- Extended analytics and reporting
- Additional report types and formats

##### **`users/` - Extended User Management (42+ classes)**
- **Admin Extensions (28+ classes):**
  - Extended administrative functionality
  - Advanced admin tools and features

- **Company Extensions (13+ classes):**
  - Extended company functionality
  - Advanced company tools and features

## Web Interface (`src/web/`)

### **JSP Pages (240+ files)**
- **Administrative Pages:**
  - `addAdmin.jsp`, `AdminMenu.jsp`, `viewAdmins.jsp`
  - `createNewGame.jsp`, `gameMenu.jsp`, `viewGames.jsp`
  - `addCompany.jsp`, `viewCompanies.jsp`, `companyMenu.jsp`

- **Company Management:**
  - `company_viewCompanyMembers.jsp`, `viewCompanyMembers.jsp`
  - `changeCompanyInfo.jsp`, `editCompanyInfo.jsp`
  - `changePassword.jsp`, `changeEmail.jsp`

- **Job and Bidding:**
  - `chooseJobForBid.jsp`, `jobEstimate.jsp`
  - `chooseJobForEstimates.jsp`, `chooseJobToBill.jsp`
  - `chooseJobToChangeMethods.jsp`, `chooseJobToRequestOvertime.jsp`

- **Financial Reports:**
  - `balanceSheet.jsp`, `incomeStatement.jsp`, `cashFlowReport.jsp`
  - `jobCostReport.jsp`, `ratiosReport.jsp`, `productivityReport.jsp`
  - `progressReport.jsp`, `paymentsReport.jsp`

- **System Configuration:**
  - `gameparameters.jsp`, `personnelParameters.jsp`
  - `methodParams.jsp`, `miscParameters.jsp`
  - `autoUpdatePolicy.jsp`, `autoUpdatePolicy_dates.jsp`

### **Static Resources:**

#### **CSS Styling:**
- `css/big.css` - Main application stylesheet

#### **JavaScript Libraries:**
- `js/` - Complete JavaScript library (11 files)
  - Browser detection and compatibility
  - Menu system and navigation
  - Form handling and validation

#### **Images and Graphics:**
- `images/` - Application graphics (73 GIF files)
- `logos/` - Company and organization logos (18 files)
- `logos2004/` - Additional logo variations (15 files)

#### **Documentation:**
- `manual/` - User manual (143 files including JSPs, images, PDFs)
- `handbooks/` - Employee and safety handbooks (92 files)

## Database Scripts (`src/sql/`)

### **Schema Creation Scripts:**
- `big-main-tables.sql` - Core application tables
- `big-parameter-tables.sql` - Game parameter tables
- `big-personnel-component-tables.sql` - Personnel management tables
- `big-estimating-component-tables.sql` - Estimation system tables
- `big-report-tables.sql` - Reporting system tables

### **Data Initialization:**
- `big-insert-param-values.sql` - Default parameter values

### **Cleanup Scripts:**
- `big-drop-*.sql` - Table cleanup scripts for reinstallation

## Tag Library Descriptors (`src/tld/`)

### **Struts Framework TLDs:**
- `struts.tld` - Main Struts tag library
- `struts-bean.tld` - Bean manipulation tags
- `struts-html.tld` - HTML form tags
- `struts-logic.tld` - Logic control tags
- `struts-form.tld` - Form handling tags
- `struts-nested.tld` - Nested tag support
- `struts-template.tld` - Template tags
- `struts-tiles.tld` - Tiles framework tags

### **Configuration DTDs:**
- `struts-config_*.dtd` - Struts configuration schemas
- `web-app_*.dtd` - Web application deployment schemas
- `tiles-config_*.dtd` - Tiles configuration schema

### **Custom TLDs:**
- `apps.tld` - Application-specific tags
- `LoginVerificationTags_1_0.tld` - Login verification tags

## Key Architecture Patterns

### **Database Layer:**
- **Facade Pattern**: `BIGDatabaseFacade` provides unified database access
- **Interface/Implementation**: Each handler has interface + default implementation
- **iBATIS Integration**: SQL mapping for database operations
- **Connection Management**: Automatic connection handling with optional persistence

### **Web Layer:**
- **MVC Pattern**: Struts-based Model-View-Controller architecture
- **Action Classes**: Handle business logic and request processing
- **Form Classes**: Handle form data binding and validation
- **JSP Views**: Presentation layer with Struts tags

### **Game Logic:**
- **Entity Classes**: Game, Job, Company, Activity, Method represent business objects
- **Factory Pattern**: JobFactory for object creation
- **State Management**: Game state tracking and period management
- **AI Integration**: ComputerControlledContractor for automated bidding

### **Reporting System:**
- **Action-Based**: Each report type has dedicated action class
- **Form Integration**: Report forms handle data presentation
- **Financial Calculations**: Complex business logic for financial reports
- **Multi-format Support**: Various report formats and views

## File Statistics

### **Source Code Summary:**
- **Java Files**: 170+ source files
- **JSP Pages**: 240+ web pages
- **SQL Scripts**: 11 database scripts
- **TLD Files**: 16 tag library descriptors
- **Static Resources**: 200+ images, CSS, JavaScript files
- **Documentation**: 200+ manual and handbook files

### **Package Distribution:**
- **Main Application**: 100+ classes in core packages
- **Extended Functionality**: 70+ classes in multimedia package
- **Database Layer**: 40+ database-related classes
- **User Management**: 100+ user and authentication classes
- **Reporting System**: 40+ report-related classes

## Build Integration

### **Compilation Process:**
- **Java Compilation**: All `.java` files compiled to `.class` files
- **Parser Generation**: JLex/CUP generates formula parser
- **Resource Copying**: JSP, images, CSS copied to build directory
- **TLD Processing**: Tag libraries processed and deployed

### **Deployment Structure:**
- **WEB-INF/classes**: Compiled Java classes
- **WEB-INF/lib**: JAR dependencies
- **Root Directory**: JSP pages and static resources
- **WEB-INF/tld**: Tag library descriptors

## Notes
- The source code follows standard Java web application patterns
- Extensive use of Struts framework for MVC architecture
- iBATIS ORM for database operations
- Custom JSP tags for authentication and session management
- Comprehensive error handling and logging
- Well-documented code with JavaDoc comments
- Modular design with clear separation of concerns
- Support for both human and AI players
- Extensive reporting and analytics capabilities
- Educational focus with comprehensive documentation and handbooks
