# BIG Application - Comprehensive Documentation Overview

## Executive Summary

The BIG (Business Information Game) is a sophisticated Java web application designed for educational purposes in construction business simulation. Developed at California Polytechnic State University, it provides a comprehensive platform for teaching construction management, business operations, and financial analysis through interactive gameplay.

### **Key Application Details:**
- **Name**: BIG (Business Information Game)
- **Version**: v4.0.1.2w (working version)
- **Type**: Java Web Application (J2EE)
- **Framework**: Apache Struts 1.x
- **Database**: PostgreSQL with iBATIS ORM
- **Build Tool**: Apache Ant
- **Target**: Educational construction business simulation
- **Copyright**: (c)2002, Trustees of the California State University

## Application Architecture Overview

### **Technology Stack:**
- **Backend**: Java/J2EE with Apache Struts MVC framework
- **Database**: PostgreSQL 8.0+ with iBATIS Object-Relational Mapping
- **Frontend**: JSP pages with custom tags and JavaScript
- **Build System**: Apache Ant with parser generation (JLex/CUP)
- **Deployment**: Apache Tomcat servlet container
- **Documentation**: Javadoc API documentation and comprehensive user manuals

### **Core Design Patterns:**
- **MVC Architecture**: Struts-based Model-View-Controller separation
- **Facade Pattern**: `BIGDatabaseFacade` for unified database access
- **Factory Pattern**: Object creation through specialized factories
- **Interface/Implementation**: Flexible, testable design with default implementations

## Project Structure Analysis

### **1. Main Directory (`/`)**
**Purpose**: Project foundation and build configuration

**Key Files:**
- **`build.xml`**: Apache Ant build file (created April 24, 2001)
  - Complete build automation from source to deployment
  - Parser generation using JLex/CUP for formula parsing
  - Debug compilation with line numbers and variables
  - WAR file creation for Tomcat deployment
  - Javadoc generation for API documentation

- **`version.txt`**: Current version tracking (`v4.0.1.2w`)
- **`history.txt`**: Development history (2001-2006)
- **License Files**: Apache 2.0, CUP, JLex licenses with proper attribution
- **`NOTICE.txt`**: Third-party software acknowledgments

**Development Timeline:**
- **v4.0.1.2w (2/11/2006)**: Current version with BIGLog infrastructure
- **v4.0.1.2+ (2/8/2006)**: Bug fixes for share of savings calculations
- **v4.0.1.1 (1/7/2006)**: Share of savings handling improvements
- **v4.0.1 (8/25/2005)**: Employee hire/fire scheduling capability

### **2. Source Code (`/src/`)**
**Purpose**: Complete development codebase

**Structure:**
- **`java/`**: 170+ Java source files in organized packages
- **`web/`**: 458+ files including JSPs, images, CSS, JavaScript
- **`sql/`**: 11 database schema and data scripts
- **`tld/`**: 16 Tag Library Descriptors for Struts framework

**Package Organization:**
- **`edu.calpoly.its.mas.big`**: Main application package
  - **`db/`**: Database layer (40+ classes) with facade pattern
  - **`game/`**: Game logic (16 classes) with business objects
  - **`reports/`**: Reporting system (16 classes) with financial analytics
  - **`users/`**: User management (100+ classes) with authentication
  - **`tags/`**: Custom JSP tags (4 classes) for session management

- **`edu.calpoly.lib.multimedia.big`**: Extended functionality package
  - **AI System**: `ComputerControlledContractor` for automated bidding
  - **Parser Generation**: JLex/CUP generated formula parser
  - **Custom Logging**: `BIGLog` system for application-specific logging
  - **Advanced Features**: Negotiation, statistics, tax management

### **3. Build Output (`/build/`)**
**Purpose**: Compiled and packaged application ready for deployment

**Contents:**
- **`dist/`**: Deployable web application (834+ files)
  - **JSP Pages**: 240+ web interface pages
  - **Compiled Classes**: 306+ Java class files
  - **Static Resources**: 180+ images, CSS, JavaScript files
  - **Documentation**: User manuals and handbooks (200+ files)

**Key Components:**
- **`WEB-INF/classes/`**: Compiled Java classes in package structure
- **`WEB-INF/lib/`**: JAR dependencies and libraries
- **`WEB-INF/web.xml`**: Web application deployment descriptor
- **`WEB-INF/struts-config.xml`**: Struts framework configuration

### **4. Configuration (`/conf/`)**
**Purpose**: Application configuration and behavior definition

**Key Files:**
- **`web.xml`**: Web application deployment descriptor
  - Login initialization servlet for company sessions
  - Auto-update servlet for scheduled game updates (1-minute intervals)
  - Struts action servlet with debugging enabled
  - 30-minute session timeout configuration
  - Security constraints for admin and company directories

- **`ApplicationResources.properties`**: Internationalization resources (882 lines)
  - Activity names for construction activities
  - UI labels, form text, and button labels
  - Success/error messages and confirmations
  - Report labels and validation messages

- **`iBATIS/`**: Database mapping configuration
  - **`ibatis.properties`**: PostgreSQL connection settings
  - **`sqlMaps.xml`**: Main iBATIS configuration with connection pooling
  - **SQL Mapping Files**: 7 specialized mapping files for different data operations

- **Logging Configuration**:
  - **`log4j.properties`**: Standard logging to console and file
  - **`BIGLog.properties`**: Custom BIG-specific logging with TRACE level

### **5. Libraries (`/lib/`)**
**Purpose**: Third-party dependencies and JSP tag libraries

**Library Groups:**
- **Web Framework**: Struts 1.x, JSTL implementation
- **ORM/Database**: iBATIS SQL Maps, PostgreSQL JDBC, DBCP connection pooling
- **Apache Commons**: Bean utilities, collections, validation, file upload
- **XML/XSLT**: Xerces parser, Xalan processor, FOP for PDF generation
- **Logging**: Log4j backend, Commons logging facade
- **Parser Generation**: JLex, CUP, ANTLR for formula parsing

**Tag Libraries**: 16 TLD files for JSTL core, formatting, SQL, XML, and custom tags

### **6. Documentation (`/docs/`)**
**Purpose**: Comprehensive documentation suite

**Structure:**
- **Software Requirements Specification (SRS)**:
  - **`bigreqv22.xml`**: Main SRS in DocBook XML (2,867 lines)
  - **`build_srs.xml`**: Ant build file for PDF generation
  - Multiple version iterations (v3.0, v3.2, v4.0)

- **Design Documentation**:
  - **`BIGDesignGuide.doc`**: Design standards and guidelines
  - **`BIGDefectReport.doc`**: Defect reporting documentation
  - PowerDesigner database models (PDM, CDM files)

- **User Documentation**:
  - **`BigInstructionManualv2.1.doc`**: Main user instruction manual
  - **`adminManual.doc`**: Administrator manual
  - **`BIG-FAQ.doc`**: Frequently asked questions

- **Example Data**: Excel files with sample financial reports, bid forms, Gantt charts

### **7. API Documentation (`/javadoc/`)**
**Purpose**: Complete Java API reference

**Generated**: November 25, 2003 at 17:33:59 PST
**Coverage**: All packages with frame-based navigation
**Features**: Class documentation, method signatures, cross-references, deprecation tracking

### **8. Deployment (`/deployment/`)**
**Purpose**: Server installation and deployment resources

**Contents:**
- **Installation Guides**: Microsoft Word documents for BIG configuration, PostgreSQL setup, Tomcat installation
- **Database Scripts**: 11 SQL files for complete database setup
  - **Schema Creation**: Main tables, parameters, personnel, reporting, estimation
  - **Data Initialization**: Default parameter values (285+ entries)
  - **Cleanup Scripts**: Table cleanup for reinstallation

### **9. Development Tools (`/devtools/`)**
**Purpose**: Development utilities and test environment setup

**Key Files:**
- **`createTestGame.sql`**: Creates test game and company for development
- **Test Implementations**: Dummy data facades for testing without database
- **Cost Estimation**: Job cost estimator with overtime calculations

### **10. Discarded Code (`/discard/`)**
**Purpose**: Historical record of deprecated implementations

**Key Files:**
- **`CompanyX.java`**: Deprecated AI company (replaced by `ComputerControlledContractor`)
- **`FinancialReportAction.java`**: Old financial reporting action
- **`SubmitBidAction.java`**: Incomplete bid submission action

**Architectural Evolution**: Shows transition from inheritance-based to interface-based design

### **11. New Graphics (`/new_graphics/`)**
**Purpose**: Updated user interface design

**Contents:**
- **HTML Prototypes**: 8 HTML files for new interface design
- **Styling**: `big.css` with modern design elements
- **Images**: 78 GIF files for navigation, buttons, and UI elements
- **JavaScript**: 11 files for menu functionality and browser compatibility

## Functional Capabilities

### **Core Game Features:**
- **Multi-User Support**: Admin and company user types with role-based access
- **Game Management**: Create, configure, and manage multiple game instances
- **Company Management**: User accounts, authentication, and session management
- **Job Bidding**: Competitive bidding system with AI opponents
- **Financial Management**: Loans, cash flow, and financial reporting
- **Project Management**: Job scheduling, method selection, and progress tracking

### **Advanced Features:**
- **AI Integration**: Computer-controlled contractors for automated bidding
- **Formula Parser**: JLex/CUP generated parser for business formula evaluation
- **Comprehensive Reporting**: Financial, productivity, progress, and analytical reports
- **Educational Content**: Complete user manuals, handbooks, and safety policies
- **Multi-format Support**: Various report formats and data export capabilities

### **Technical Features:**
- **Database Integration**: PostgreSQL with iBATIS ORM and connection pooling
- **Session Management**: Custom authentication and session tracking
- **Error Handling**: Comprehensive exception handling and logging
- **Internationalization**: Resource bundle support for multiple languages
- **Validation**: Form validation with custom rules and error messages

## Educational Value

### **Learning Objectives:**
- **Construction Management**: Project planning, scheduling, and execution
- **Business Operations**: Financial management, bidding, and cost estimation
- **Decision Making**: Strategic planning and risk assessment
- **Financial Analysis**: Understanding of financial statements and ratios
- **Team Collaboration**: Multi-user environment with role-based responsibilities

### **Documentation Quality:**
- **Comprehensive Manuals**: User guides, admin guides, and FAQs
- **API Documentation**: Complete Javadoc with examples and cross-references
- **Design Documentation**: Software requirements, design guides, and database schemas
- **Example Data**: Sample reports, forms, and financial statements
- **Educational Materials**: Review questions, answers, and assessment tools

## Technical Excellence

### **Code Quality:**
- **Professional Architecture**: Well-structured, modular design with clear separation of concerns
- **Design Patterns**: Proper use of MVC, Facade, Factory, and Interface patterns
- **Error Handling**: Comprehensive exception handling and logging
- **Documentation**: Extensive JavaDoc comments and inline documentation
- **Testing Support**: Test implementations and development utilities

### **Build and Deployment:**
- **Automated Build**: Complete Ant-based build system with parser generation
- **Deployment Ready**: WAR file creation for easy Tomcat deployment
- **Configuration Management**: Centralized configuration with environment-specific settings
- **Version Control**: Proper version tracking and change history
- **Legal Compliance**: Proper licensing and third-party attribution

### **Maintainability:**
- **Modular Design**: Clear package structure with logical separation
- **Interface-Based**: Flexible design with interface/implementation pattern
- **Extensible**: Support for new features and custom implementations
- **Well-Documented**: Comprehensive documentation at all levels
- **Professional Standards**: Follows Java and web development best practices

## Conclusion

The BIG application represents a sophisticated, well-architected educational software system that demonstrates professional software development practices. With its comprehensive feature set, extensive documentation, and robust technical foundation, it serves as an excellent example of enterprise-grade Java web application development for educational purposes.

The application successfully combines complex business simulation with user-friendly interfaces, providing both educational value and technical excellence. Its modular architecture, comprehensive documentation, and professional development practices make it a valuable resource for understanding both construction business management and software engineering principles.

**Key Strengths:**
- Professional software architecture and design patterns
- Comprehensive documentation and educational materials
- Robust technical foundation with proper error handling
- Extensive feature set covering all aspects of construction business
- Well-maintained codebase with clear version history
- Proper legal compliance and third-party attribution
- Educational focus with practical business applications

This application serves as an excellent example of how complex business processes can be effectively modeled and taught through interactive software systems.
