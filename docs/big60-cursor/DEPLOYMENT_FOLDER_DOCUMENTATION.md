# BIG Application Deployment Documentation

## Overview
The `deployment/` folder contains documentation and resources for deploying the BIG (Business Information Game) application to a production or development server environment. This includes server installation guides, database setup scripts, and configuration documentation.

## Deployment Folder Structure

### `/deployment/server_installation_notes/` - Installation Documentation
Contains Microsoft Word documentation files for server setup:

#### `Configure_BIG.doc`
- **Purpose**: Main configuration guide for BIG application
- **Content**: Step-by-step instructions for configuring the application
- **Format**: Microsoft Word document (binary, not readable as text)

#### `PostgreSQL_Setup.doc`
- **Purpose**: Database server installation and configuration guide
- **Content**: PostgreSQL installation, database creation, and user setup
- **Format**: Microsoft Word document (binary, not readable as text)

#### `Tomcat_v4_install.doc`
- **Purpose**: Apache Tomcat servlet container installation guide
- **Content**: Tomcat 4.x installation and configuration for BIG
- **Format**: Microsoft Word document (binary, not readable as text)

#### `vssver.scc`
- **Purpose**: Visual SourceSafe version control file
- **Content**: Version control metadata

## Database Deployment Scripts

### SQL Scripts Location: `/src/sql/`
The database deployment consists of multiple SQL scripts that must be executed in a specific order:

#### Core Database Schema Scripts

##### `big-main-tables.sql` - Main Application Tables
- **Purpose**: Creates core application tables
- **Tables Created**:
  - `ADMIN` - Administrator accounts and credentials
  - `GAME` - Game instances and configuration
  - `PERIOD` - Game periods with environmental factors
  - `COMPANY` - Company entities participating in games
  - `COMPANY_MEMBERS` - Company user accounts
  - `JOB` - Construction jobs available for bidding
  - `BID` - Company bids on jobs
  - `METHOD` - Construction methods and activities
  - `LOAN` - Company loan records
  - `OVERTIME` - Overtime requests and approvals
- **Database**: Originally designed for Oracle 8, adapted for PostgreSQL
- **Size**: 464 lines of DDL statements

##### `big-parameter-tables.sql` - Game Parameter Tables
- **Purpose**: Creates tables for game configuration parameters
- **Tables Created**:
  - `GAME_PARAMS_OVERHEAD` - Overhead cost parameters
  - `GAME_PARAMS_JOBS_PER_PERIOD` - Job availability parameters
  - `GAME_PARAMS_LABOR_AVAIL` - Labor availability parameters
  - `GAME_PARAMS_MATERIAL_COST_IDX` - Material cost index parameters
  - `GAME_PARAMS_TEMPERATURE` - Temperature parameters
  - `GAME_PARAMS_RAINFALL` - Rainfall parameters
- **Size**: 195 lines of DDL statements

##### `big-personnel-component-tables.sql` - Personnel Management
- **Purpose**: Creates tables for employee and personnel management
- **Tables Created**:
  - `AVAILABLE_PERSONNEL` - Employee types and costs
  - `COMPANY_PERSONNEL` - Company employee tracking
- **Features**: 
  - Employee hiring/firing costs
  - Period-based personnel journaling
  - Employee type management

##### `big-estimating-component-tables.sql` - Estimating System
- **Purpose**: Creates tables for job estimation functionality
- **Tables Created**:
  - `ESTIMATES_PER_PERIOD` - Estimation limits per period
  - `SCHEDULE_ESTIMATED_METHODS` - Estimated construction methods
  - `ESTIMATES_AVAILABLE` - Available estimates for companies

##### `big-report-tables.sql` - Reporting System
- **Purpose**: Creates tables for financial and business reporting
- **Tables Created**:
  - `FINANCIAL_REPORT` - Company financial data by period
  - `JOB_FINANCIAL_INFO` - Job-specific financial information
- **Features**: 
  - Period-based financial tracking
  - Interest, overhead, and expense tracking
  - Loan and cash flow management

#### Data Initialization Scripts

##### `big-insert-param-values.sql` - Default Parameter Values
- **Purpose**: Inserts default game parameters and configuration values
- **Content**: 285 lines of INSERT statements
- **Default Values**:
  - Job counts per period (mean, std dev, min, max)
  - Labor availability parameters
  - Material cost index values
  - Temperature and rainfall parameters
  - Overhead cost parameters
- **Game ID**: Uses game ID 0 for default/global parameters

#### Database Cleanup Scripts

##### Drop Scripts (for reinstallation):
- `big-drop-main-tables.sql` - Drops all main application tables
- `big-drop-parameter-tables.sql` - Drops parameter tables
- `big-drop-estimating-component-tables.sql` - Drops estimating tables
- `big-drop-report-tables.sql` - Drops reporting tables

### Development Tools

##### `devtools/createTestGame.sql` - Test Data Creation
- **Purpose**: Creates test game and company for development/testing
- **Content**: 
  - Creates a test game with ID from sequence
  - Creates a test company with default credentials
  - Sets up basic game parameters

## Deployment Process

### Build and Package Process (from `build.xml`)

#### 1. Compilation Phase
```xml
<target name="compile" depends="generateParser">
  <javac srcdir="./${src}" destdir="${build}/${dist}/WEB-INF/classes" debug="true">
    <classpath refid="compile.classpath" />
  </javac>
</target>
```

#### 2. Distribution Phase
```xml
<target name="dist" depends="compile,distclean">
  <!-- Copy configuration files -->
  <copy file="${conf}/web.xml" todir="${build}/${dist}/WEB-INF" />
  <copy file="${conf}/struts-config.xml" todir="${build}/${dist}/WEB-INF" />
  
  <!-- Copy web resources -->
  <copy todir="${build}/${dist}">
    <fileset dir="${web}" excludes="${images}" />
  </copy>
  
  <!-- Create WAR file -->
  <jar jarfile="${webapps}/${archive}.war" basedir="${build}/${dist}"/>
</target>
```

#### 3. Deployment Targets
- **`dist`**: Creates deployable WAR file
- **`clean`**: Removes build artifacts
- **`distclean`**: Removes deployed application
- **`javadoc`**: Generates API documentation

### Deployment Architecture

#### Server Requirements
- **Java Runtime**: J2SE 1.4 or higher
- **Servlet Container**: Apache Tomcat 4.x or higher
- **Database**: PostgreSQL 8.0+ (originally Oracle 8)
- **Operating System**: Windows (based on path configurations)

#### Directory Structure
```
Tomcat Installation:
├── webapps/
│   └── big.war (deployed application)
├── logs/
│   ├── BIGLog.log (application logs)
│   └── big/ (custom BIG logs)
└── common/lib/ (shared libraries)
```

#### Database Setup Process
1. **Install PostgreSQL** (following `PostgreSQL_Setup.doc`)
2. **Create Database**: `CREATE DATABASE big;`
3. **Create User**: `CREATE USER big WITH PASSWORD 'big';`
4. **Execute SQL Scripts** in order:
   - `big-main-tables.sql`
   - `big-parameter-tables.sql`
   - `big-personnel-component-tables.sql`
   - `big-estimating-component-tables.sql`
   - `big-report-tables.sql`
   - `big-insert-param-values.sql`
5. **Create Test Data** (optional): `createTestGame.sql`

#### Application Deployment
1. **Build Application**: `ant dist`
2. **Deploy WAR File**: Copy `big.war` to Tomcat webapps directory
3. **Configure Database**: Update `conf/iBATIS/ibatis.properties`
4. **Start Tomcat**: Application auto-deploys from WAR file

## Configuration for Deployment

### Database Configuration
```properties
# conf/iBATIS/ibatis.properties
driver=org.postgresql.Driver
url=jdbc:postgresql://localhost/big
username=big
password=big
```

### Logging Configuration
```properties
# conf/log4j.properties
log4j.appender.LogFile.File=c:\\tomcat\\logs\\BIGLog.log
log4j.appender.LogFile.Append=false
```

### Build Configuration
```xml
<!-- build.xml -->
<property name="webapps" value="..\..\..\tomcat\webapps" />
<property name="archive" value="big" />
```

## File Counts and Structure
- **Documentation Files**: 3 Word documents + 1 VSS file
- **SQL Scripts**: 11 database setup/teardown scripts
- **Total Deployment Files**: 15 files
- **Database Tables**: 20+ tables across 5 schema categories
- **Default Parameters**: 285+ parameter values

## Deployment Notes
- **Original Database**: Oracle 8 (migrated to PostgreSQL)
- **Servlet Container**: Tomcat 4.x (legacy version)
- **Build Tool**: Apache Ant 1.2+
- **Documentation**: Microsoft Word format (legacy)
- **Version Control**: Visual SourceSafe (legacy)
- **Path Separators**: Windows-style backslashes
- **Logging**: File-based logging to Tomcat logs directory

## Security Considerations
- Database credentials are hardcoded in configuration
- Default admin credentials not documented in accessible files
- Application uses basic authentication
- Session timeout: 30 minutes
- Directory-based access control for admin/company areas

## Troubleshooting
- Check Tomcat logs for deployment errors
- Verify database connectivity and permissions
- Ensure all SQL scripts executed successfully
- Verify WAR file deployment in Tomcat webapps
- Check BIG-specific logs in `c:\tomcat\logs\big\`
