# BIG Application Build Folder Documentation

## Overview
The `build/` folder contains the compiled and packaged version of the BIG (Business Information Game) application. This is a Java web application built using Apache Struts framework and designed for educational purposes in construction business simulation.

## Application Information
- **Name**: BIG (Business Information Game)
- **Version**: v4.0.1.2w (working)
- **Type**: Java Web Application (J2EE)
- **Framework**: Apache Struts 1.x
- **Database**: PostgreSQL with iBATIS ORM
- **Build Tool**: Apache Ant

## Build Folder Structure

### `/build/dist/` - Main Application Directory
This is the deployable web application directory that contains all the necessary files for deployment to a servlet container like Tomcat.

#### Key Components:

**JSP Pages (240+ files)**
- Admin management pages (`addAdmin.jsp`, `AdminMenu.jsp`, etc.)
- Company management (`addCompany.jsp`, `companyMenu.jsp`, etc.)
- Game management (`createNewGame.jsp`, `gameMenu.jsp`, etc.)
- Financial reports (`balanceSheet.jsp`, `incomeStatement.jsp`, `cashFlowReport.jsp`)
- Job management (`jobEstimate.jsp`, `chooseJobForBid.jsp`, etc.)
- User interface pages (`login.jsp`, `mainMenu.jsp`, etc.)

**Static Resources:**
- `/css/` - Stylesheets (`big.css`)
- `/images/` - Application images (73 GIF files, 1 DB file)
- `/js/` - JavaScript libraries for menu functionality and UI
- `/logos/` - Company logos for different construction companies
- `/manual/` - User manual and documentation (141 files)

**Handbooks:**
- `/handbooks/employeehandbook/` - Employee handbook pages (74 JSP files)
- `/handbooks/safetypolicies/` - Safety policy documentation (36 files)

### `/build/dist/WEB-INF/` - Web Application Configuration

#### `/WEB-INF/classes/` - Compiled Java Classes
Contains all compiled `.class` files organized in package structure:

**Main Application Classes:**
- `edu.calpoly.its.mas.big.db.*` - Database handlers and facade
- `edu.calpoly.its.mas.big.game.*` - Game logic and business objects
- `edu.calpoly.its.mas.big.users.*` - User management and authentication
- `edu.calpoly.its.mas.big.reports.*` - Financial and business reports
- `edu.calpoly.its.mas.big.tags.*` - Custom JSP tags

**Database Layer:**
- `BIGDatabaseFacade.class` - Main database interface
- Various `*DBHandler.class` files for different data operations
- iBATIS SQL mapping files (`*-sqlMap.xml`)

**Configuration Files:**
- `log4j.properties` - Logging configuration
- `BIGLog.properties` - Custom BIG logging configuration
- `ApplicationResources.properties` - Application resources
- `ibatis.properties` - iBATIS database configuration

#### `/WEB-INF/lib/` - Application Libraries (25 JAR files)
Key dependencies include:
- **Struts Framework**: `struts.jar`
- **Database**: `postgresql-8.0.309.jdbc3.jar`, `ibatis-common-2.jar`, `ibatis-sqlmap-2.jar`
- **Utilities**: `commons-*` libraries (beanutils, collections, dbcp, etc.)
- **Logging**: `log4j-1.2.9.jar`, `commons-logging.jar`
- **XML Processing**: `xalan.jar`, `xerces.jar`
- **Parser Generation**: `java_cup.jar`, `jlex.jar`

#### Configuration Files:
- `web.xml` - Web application deployment descriptor
- `struts-config.xml` - Struts framework configuration
- `validation.xml` & `validator-rules.xml` - Form validation rules
- Various `.tld` files - Tag library descriptors

## Build Process

The application is built using Apache Ant with the `build.xml` file. The build process includes:

1. **Parser Generation**: Uses JLex and CUP to generate formula parser
2. **Compilation**: Compiles Java source files to class files
3. **Resource Copying**: Copies JSP files, images, CSS, and configuration files
4. **Packaging**: Creates a WAR file for deployment

## Application Features

Based on the JSP files and class structure, the application provides:

### Administrative Functions:
- Game creation and management
- Company management
- User administration
- System configuration

### Business Simulation:
- Job bidding and estimation
- Financial reporting (balance sheet, income statement, cash flow)
- Personnel management
- Contract management
- Loan management

### Reporting:
- Various financial reports
- Job cost reports
- Productivity reports
- Personnel reports

## Deployment

The build process creates a `big.war` file that can be deployed to any J2EE-compliant servlet container. The application expects:
- PostgreSQL database
- Tomcat or similar servlet container
- Java runtime environment

## File Counts
- **Total Files**: 834+ files
- **Java Classes**: 306 compiled classes
- **JSP Pages**: 240+ pages
- **Images**: 180+ GIF files
- **Libraries**: 25 JAR files

## Notes
- This appears to be an educational simulation game for construction business management
- The application uses older Java technologies (Struts 1.x, J2EE 2.2)
- Built for educational purposes at California Polytechnic State University
- Last updated: February 2006 (v4.0.1.2w)

## Dependencies
The application requires:
- Java 1.4+ runtime
- PostgreSQL database
- Servlet container (Tomcat 4+)
- Various Apache Commons libraries
- iBATIS ORM framework
