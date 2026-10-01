# BIG Application Development Tools Documentation

## Overview
The `devtools/` folder contains development utilities and tools for the BIG (Business Information Game) application. This folder provides resources for developers to set up test environments, create sample data, and work with the application during development.

## Development Tools Structure

### `/devtools/` Directory Contents

#### `createTestGame.sql` - Test Game Creation Script
- **Purpose**: Creates a test game and company for development and testing purposes
- **File Size**: 2 lines of SQL statements
- **Usage**: Executed after database setup to create initial test data

##### Script Content:
```sql
insert into game values (nextval ('big_gameid_sequence'), 'Test Game', 4, 0, '1/1/2005', 't', 0);
insert into company values (nextval ('big_companyid_sequence'), curval ('big_gameid_sequence'), 'Company 1', 'company1', 'company1', 1000000);
```

##### What It Creates:
1. **Test Game**:
   - **Game ID**: Auto-generated using `big_gameid_sequence`
   - **Game Name**: "Test Game"
   - **Admin ID**: 4 (references existing admin)
   - **Current Period**: 0 (starting period)
   - **Start Date**: January 1, 2005
   - **Active Status**: True ('t')
   - **Current Job Number**: 0

2. **Test Company**:
   - **Company ID**: Auto-generated using `big_companyid_sequence`
   - **Game ID**: References the test game created above
   - **Company Name**: "Company 1"
   - **Username**: "company1"
   - **Password**: "company1"
   - **Initial Cash**: $1,000,000

## Development Utilities in Source Code

### Test Implementation Classes

#### `NamedVariableDataFacadeTestImpl.java` - Test Data Facade
- **Location**: `src/java/edu/calpoly/lib/multimedia/big/game/`
- **Purpose**: Dummy implementation of `NamedVariableDataFacade` interface for testing
- **Features**:
  - Returns predefined values for named variables
  - Provides default values (1 for integers, 1.0 for floats)
  - Allows testing without database dependencies
  - Supports all data types: short, int, long, float, double

##### Key Methods:
```java
public Object getNamedVariable(String variableName)
public short getNamedShort(String variableName)
public int getNamedInt(String variableName)
public long getNamedLong(String variableName)
public float getNamedFloat(String variableName)
public double getNamedDouble(String variableName)
```

#### `JobCostEstimator.java` - Cost Estimation Utility
- **Location**: `src/java/edu/calpoly/lib/multimedia/big/reports/`
- **Purpose**: Estimates job completion costs based on various factors
- **Features**:
  - Calculates overtime costs
  - Considers construction units (CUs) remaining
  - Applies overtime modifiers to labor costs
  - Handles materials, subcontractor, and labor cost calculations

##### Estimation Process:
1. **Overtime Calculation**: Determines CUs completed during overtime
2. **Cost Per CU**: Calculates cost including overtime modifiers
3. **Total Estimation**: Multiplies CUs by cost per CU
4. **Activity Processing**: Processes each activity separately

## Development Workflow

### Database Setup for Development
1. **Install PostgreSQL** (following deployment documentation)
2. **Create Database Schema**:
   ```sql
   -- Execute in order:
   -- big-main-tables.sql
   -- big-parameter-tables.sql
   -- big-personnel-component-tables.sql
   -- big-estimating-component-tables.sql
   -- big-report-tables.sql
   -- big-insert-param-values.sql
   ```

3. **Create Test Data**:
   ```sql
   -- Execute devtools/createTestGame.sql
   ```

4. **Verify Setup**:
   - Check that test game exists
   - Verify test company can log in
   - Confirm database sequences are working

### Development Environment Configuration

#### Database Configuration
```properties
# conf/iBATIS/ibatis.properties
driver=org.postgresql.Driver
url=jdbc:postgresql://localhost/big
username=big
password=big
```

#### Logging Configuration for Development
```properties
# conf/log4j.properties
log4j.rootLogger=DEBUG, Console, LogFile
log4j.logger.edu.calpoly=DEBUG
```

#### Build Configuration
```xml
<!-- build.xml - Development targets -->
<target name="compile" debug="true" debuglevel="lines,vars,source">
  <compilerarg value="-Xlint:deprecation" />
</target>
```

## Testing Utilities

### Test Data Management
- **Test Game**: Provides a consistent starting point for testing
- **Test Company**: Pre-configured company with known credentials
- **Initial Cash**: $1,000,000 starting capital for testing financial features
- **Admin Reference**: Links to admin ID 4 for game management

### Development Testing Features
- **Mock Data Facade**: `NamedVariableDataFacadeTestImpl` for unit testing
- **Cost Estimation**: `JobCostEstimator` for testing financial calculations
- **Database Sequences**: Auto-incrementing IDs for test data creation

## Build and Development Tools

### Apache Ant Build System
The `build.xml` file provides several development-friendly targets:

#### Development Build Targets:
- **`compile`**: Compiles with debug information and deprecation warnings
- **`javadoc`**: Generates API documentation
- **`clean`**: Removes build artifacts
- **`distclean`**: Removes deployed application

#### Parser Generation:
- **JLex**: Generates lexical analyzer for formula parsing
- **CUP**: Generates parser for formula grammar
- **Output**: `BIGFormulaScanner.java` and `BIGFormulaParser.java`

### Development Dependencies
- **Java Development Kit**: J2SE 1.4 or higher
- **Apache Ant**: 1.2 or higher
- **PostgreSQL**: 8.0 or higher
- **Apache Tomcat**: 4.x or higher

## File Structure Summary

### `/devtools/` Directory:
- **Total Files**: 1 file
- **SQL Scripts**: 1 test data creation script
- **Purpose**: Development and testing utilities

### Related Development Files:
- **Test Classes**: 2 Java test implementation classes
- **Build Scripts**: 1 Apache Ant build file
- **Documentation**: Javadoc generation capability

## Development Best Practices

### Database Development:
1. **Always use sequences** for ID generation
2. **Create test data** using provided scripts
3. **Verify foreign key relationships** before testing
4. **Use transaction boundaries** for data integrity

### Code Development:
1. **Use test implementations** for unit testing
2. **Enable debug logging** during development
3. **Generate javadoc** for API documentation
4. **Test with provided test game** before creating new games

### Testing Workflow:
1. **Set up test database** with provided scripts
2. **Create test game** using `createTestGame.sql`
3. **Log in as test company** (company1/company1)
4. **Test application features** with known data
5. **Verify calculations** using `JobCostEstimator`

## Troubleshooting Development Issues

### Common Development Problems:
1. **Sequence Issues**: Ensure database sequences exist and are properly configured
2. **Foreign Key Violations**: Verify admin ID 4 exists before creating test game
3. **Connection Issues**: Check PostgreSQL service and connection parameters
4. **Build Issues**: Verify all dependencies are in classpath

### Development Logging:
- **Application Logs**: `c:\tomcat\logs\BIGLog.log`
- **Custom BIG Logs**: `c:\tomcat\logs\big\`
- **Console Output**: Debug level logging enabled
- **Error Tracking**: Deprecation warnings enabled in build

## Notes
- The devtools folder is minimal but provides essential testing capabilities
- Test data creation is straightforward with provided SQL script
- Development environment setup follows standard Java web application patterns
- All development tools are integrated with the main build system
- Test implementations allow for unit testing without database dependencies
