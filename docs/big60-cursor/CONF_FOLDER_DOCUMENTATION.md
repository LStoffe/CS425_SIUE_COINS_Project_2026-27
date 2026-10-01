# BIG Application Configuration Folder Documentation

## Overview
The `conf/` folder contains all configuration files for the BIG (Business Information Game) application. These files define the application's behavior, database connections, logging, validation rules, and internationalization resources.

## Configuration Files Structure

### Core Application Configuration

#### `web.xml` - Web Application Deployment Descriptor
- **Purpose**: Defines the web application configuration for J2EE servlet container
- **Key Components**:
  - **Login Initialization Servlet**: `LoginInitializationServlet` - Manages company login sessions
  - **Auto Update Servlet**: `AutoUpdateServlet` - Handles scheduled game updates (1-minute intervals)
  - **Struts Action Servlet**: Main controller with debugging enabled (level 10)
  - **Session Configuration**: 30-minute session timeout
  - **Error Pages**: Custom error handling for 404 and 500 errors
  - **Security Constraints**: Protects admin and company directories
  - **Welcome Files**: `index.html` as default entry point

#### `ApplicationResources.properties` - Internationalization Resources
- **Purpose**: Contains all text strings, labels, and messages for the application
- **Content**: 882 lines of key-value pairs including:
  - **Activity Names**: Construction activities (Excavation, Foundation, Framing, etc.)
  - **UI Labels**: Form labels, button text, headers
  - **Messages**: Success/error messages, confirmations
  - **Report Labels**: Financial report headers and descriptions
  - **Validation Messages**: Form validation error text

### Database Configuration

#### `iBATIS/` Directory - Database Mapping Configuration
Contains iBATIS ORM configuration files for database operations:

##### `ibatis.properties` - Database Connection
```properties
driver=org.postgresql.Driver
url=jdbc:postgresql://localhost/big
username=big
password=big
```

##### `sqlMaps.xml` - Main iBATIS Configuration
- **Database**: PostgreSQL with DBCP connection pooling
- **Settings**: 
  - Cache models enabled
  - Lazy loading enabled
  - Max 32 requests, 10 sessions, 5 transactions
- **SQL Maps**: References all individual SQL mapping files

##### Individual SQL Mapping Files:
1. **`job-sqlMap.xml`** - Job-related database operations
   - Job counting queries
   - Company-specific job queries
   - Job type filtering

2. **`bid-sqlMap.xml`** - Bidding system operations
   - Total company bids counting
   - Dismissed bids tracking
   - Bid outcome management

3. **`companyPersonnel-sqlMap.xml`** - Personnel management
   - Employee mapping operations
   - Company personnel tracking
   - Employee type management

4. **`gradingTemplate-sqlMap.xml`** - Grading system
   - Template management for grading

5. **`automaticPeriodUpdates-sqlMap.xml`** - Game automation
   - Scheduled update operations

6. **`personnelParameters-sqlMap.xml`** - Personnel configuration
   - Parameter management for personnel

### Logging Configuration

#### `log4j.properties` - Standard Logging
```properties
log4j.rootLogger=INFO, LogFile
log4j.logger.edu.calpoly=DEBUG
log4j.logger.edu.calpoly.its.mas.big.game.NumberConverter=ERROR
```
- **Console Appender**: Debug output to console
- **File Appender**: Logs to `c:\tomcat\logs\BIGLog.log`
- **Pattern**: Timestamp, level, class, message format

#### `BIGLog.properties` - Custom BIG Logging
```properties
log.level=TRACE
log.directory=c:\tomcat\logs\big
```
- **Purpose**: Custom logging system for BIG-specific logs
- **Level**: TRACE (most verbose)
- **Directory**: Separate log directory for BIG logs

### Form Validation Configuration

#### `validation.xml` - Form Validation Rules
- **Purpose**: Defines validation rules for HTML forms
- **Status**: Currently empty (no custom validation rules defined)
- **Framework**: Apache Commons Validator

#### `validator-rules.xml` - Validation Rule Definitions
- **Purpose**: Standard Struts validation rules
- **Content**: 288 lines of predefined validation rules including:
  - Required field validation
  - Length validation (min/max)
  - Format validation (email, credit card, etc.)
  - Range validation
  - Date validation
  - Custom validators

## Configuration Architecture

### Database Layer
- **ORM Framework**: iBATIS 2.0
- **Database**: PostgreSQL
- **Connection Pooling**: Apache Commons DBCP
- **Connection Management**: JDBC transaction manager

### Web Framework
- **MVC Framework**: Apache Struts 1.x
- **Servlet Container**: J2EE 2.2 compliant
- **Session Management**: 30-minute timeout
- **Security**: Directory-based access control

### Logging Strategy
- **Dual Logging**: Standard log4j + custom BIG logging
- **Log Levels**: 
  - Application: DEBUG level
  - NumberConverter: ERROR level only
  - Custom BIG logs: TRACE level
- **Output**: Both console and file logging

### Internationalization
- **Resource Bundle**: `ApplicationResources.properties`
- **Encoding**: ISO-8859-1
- **Scope**: Complete application text localization

## Key Configuration Features

### Game Automation
- **Auto Update Servlet**: Runs every minute to check for scheduled updates
- **Period Management**: Automatic game period progression
- **Background Processing**: Non-blocking game state updates

### Security Configuration
- **Protected Directories**: `/admin/` and `/company/` require authentication
- **Session Security**: Automatic session timeout
- **Error Handling**: Custom error pages for security

### Performance Settings
- **Connection Pooling**: DBCP with configurable limits
- **Caching**: iBATIS cache models enabled
- **Lazy Loading**: Optimized data loading
- **Debug Mode**: High-level debugging enabled for development

## File Counts and Structure
- **Total Configuration Files**: 12 files
- **iBATIS SQL Maps**: 7 XML files
- **Properties Files**: 3 files
- **XML Configuration**: 2 files
- **Validation Files**: 2 files

## Dependencies Referenced
- **Database**: PostgreSQL JDBC driver
- **ORM**: iBATIS 2.0
- **Web Framework**: Apache Struts
- **Logging**: Log4j 1.2.9
- **Validation**: Apache Commons Validator
- **Connection Pooling**: Apache Commons DBCP

## Notes
- Configuration is designed for development/testing environment
- Database credentials are hardcoded (should be externalized for production)
- Logging is configured for Windows Tomcat installation
- All configuration files use ISO-8859-1 encoding
- iBATIS configuration follows version 2.0 DTD specifications
