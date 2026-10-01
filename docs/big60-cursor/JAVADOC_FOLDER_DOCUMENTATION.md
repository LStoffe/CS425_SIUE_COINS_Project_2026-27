# BIG Application Javadoc Documentation

## Overview
The `javadoc/` folder contains the complete API documentation for the BIG (Business Information Game) application, generated using the Java documentation tool. This folder provides comprehensive HTML-based documentation for all Java classes, packages, and interfaces in the application, serving as the primary reference for developers working with the codebase.

## Javadoc Generation Information

### **Generation Details:**
- **Generated**: Tuesday, November 25, 2003 at 17:33:59 PST
- **Tool**: Standard Java javadoc utility
- **Format**: HTML with frames-based navigation
- **Style**: Custom stylesheet with mauve/blue color scheme
- **Coverage**: Complete API documentation for all packages

### **Documentation Structure:**
- **Frame-based Navigation**: Multi-frame layout for easy browsing
- **Package Organization**: Hierarchical package structure
- **Class Documentation**: Detailed class, method, and field documentation
- **Cross-references**: Extensive linking between related classes
- **Deprecation Tracking**: Dedicated deprecated API documentation

## Main Documentation Files

### **Navigation and Index Files:**

#### **`index.html`** - Main Entry Point
- **Purpose**: Primary entry point with frame-based navigation
- **Layout**: Three-frame layout (package list, class list, content)
- **Features**: Frame and non-frame versions available
- **Navigation**: Links to all major documentation sections

#### **`overview-summary.html`** - Package Overview
- **Purpose**: High-level overview of all packages
- **Content**: Package descriptions and class counts
- **Navigation**: Links to individual package documentation
- **Structure**: Organized by package hierarchy

#### **`allclasses-frame.html`** - Class Index
- **Purpose**: Alphabetical listing of all classes
- **Format**: Frame-based navigation list
- **Coverage**: All classes across all packages
- **Accessibility**: Quick access to any class

#### **`index-all.html`** - Complete Index
- **Purpose**: Comprehensive alphabetical index
- **Content**: All classes, methods, and fields
- **Search**: Quick lookup functionality
- **Coverage**: Complete API reference

### **Special Documentation Files:**

#### **`deprecated-list.html`** - Deprecated API
- **Purpose**: Lists all deprecated classes and methods
- **Content**: Deprecation reasons and replacement information
- **Key Items**:
  - `CompanyX` class (deprecated July 16, 2003)
  - Various deprecated methods in `Bid` class
  - Migration guidance for deprecated functionality

#### **`help-doc.html`** - API Help
- **Purpose**: Usage instructions for the documentation
- **Content**: How to navigate and use the javadoc
- **Features**: Frame navigation help, search tips

#### **`overview-tree.html`** - Class Hierarchy
- **Purpose**: Visual representation of class inheritance
- **Content**: Tree structure showing class relationships
- **Navigation**: Expandable tree view of inheritance

#### **`serialized-form.html`** - Serialization Documentation
- **Purpose**: Documentation for serializable classes
- **Content**: Serialization details and requirements
- **Coverage**: Classes implementing Serializable interface

## Package Documentation Structure

### **Main Application Packages:**

#### **`edu.calpoly.its.mas.big.db`** - Database Layer
- **Purpose**: Database access and data management
- **Classes**: 40+ database handler classes
- **Key Classes**:
  - `BIGDatabaseFacade` - Main database interface
  - `BIGDatabaseFacadeInterface` - Database interface contract
  - `BIGDatabaseFacadeException` - Database exception handling
  - Various `*DBHandler` classes for specific data operations
  - `Default*DBHandler` implementations

#### **`edu.calpoly.its.mas.big.game`** - Game Logic
- **Purpose**: Core game mechanics and business logic
- **Classes**: 16 game-related classes
- **Key Classes**:
  - `Game` - Main game entity
  - `Job` - Construction job representation
  - `Company` - Company/contractor entity
  - `Bid` - Bidding system
  - `Method` - Construction methods
  - `Period` - Game time periods
  - `Activity` - Construction activities
  - `Loan` - Financial loan management

#### **`edu.calpoly.its.mas.big.reports`** - Reporting System
- **Purpose**: Financial and business reporting
- **Classes**: 20+ report-related classes
- **Key Classes**:
  - `FinancialReportAction` - Financial report generation
  - `ProductivityReportAction` - Productivity analysis
  - `ProgressReportAction` - Project progress tracking
  - `CompleteListofBidsReportAction` - Bid listing reports
  - Various report form classes

#### **`edu.calpoly.its.mas.big.users`** - User Management
- **Purpose**: User authentication and session management
- **Classes**: 100+ user-related classes
- **Subpackages**:
  - `admin` - Administrator functionality (70+ classes)
  - `company` - Company user functionality (30+ classes)
- **Key Classes**:
  - `User` - Base user class
  - `LoginBroker` - Authentication management
  - `LoginAuthenticationAction` - Login processing
  - `GameLoginInfo` - Game session information

#### **`edu.calpoly.its.mas.big.tags`** - Custom JSP Tags
- **Purpose**: Custom JSP tag library
- **Classes**: 7 tag classes
- **Key Classes**:
  - `CheckLoggedIn` - Login verification tag
  - `CheckAdminLoggedIn` - Admin verification tag
  - `LoginVerificationTag` - General login verification
  - `IncrementNumberOfCosLoggedInTag` - Session tracking

### **Multimedia Library Packages:**

#### **`edu.calpoly.lib.multimedia.big.db`** - Extended Database
- **Purpose**: Extended database functionality
- **Classes**: 15+ additional database classes
- **Features**: Advanced database operations and utilities

#### **`edu.calpoly.lib.multimedia.big.game`** - Extended Game Logic
- **Purpose**: Extended game functionality
- **Classes**: 8+ additional game classes
- **Features**: Advanced game mechanics and AI

#### **`edu.calpoly.lib.multimedia.big.reports`** - Extended Reporting
- **Purpose**: Extended reporting capabilities
- **Classes**: 24+ additional report classes
- **Features**: Advanced reporting and analytics

#### **`edu.calpoly.lib.multimedia.big.users`** - Extended User Management
- **Purpose**: Extended user functionality
- **Classes**: 12+ additional user classes
- **Features**: Advanced user management and authentication

## Documentation Features

### **Navigation System:**
- **Frame-based Layout**: Three-pane navigation (packages, classes, content)
- **Breadcrumb Navigation**: Clear hierarchy indication
- **Cross-references**: Extensive linking between related classes
- **Search Functionality**: Quick lookup of classes and methods

### **Content Organization:**
- **Package Summaries**: Overview of each package's purpose
- **Class Documentation**: Complete class descriptions
- **Method Documentation**: Detailed method signatures and descriptions
- **Field Documentation**: Field descriptions and types
- **Constructor Documentation**: Constructor parameters and usage

### **Visual Design:**
- **Custom Stylesheet**: `stylesheet.css` with mauve/blue theme
- **Consistent Formatting**: Standard javadoc appearance
- **Color Coding**: Different colors for different element types
- **Responsive Layout**: Frame-based design for easy navigation

## Key Classes Documentation

### **Database Layer Classes:**

#### **`BIGDatabaseFacade`**
- **Purpose**: Main database access facade
- **Methods**: 50+ database operation methods
- **Features**: Connection management, transaction handling
- **Documentation**: Complete method signatures and descriptions

#### **`BIGDatabaseFacadeInterface`**
- **Purpose**: Database interface contract
- **Methods**: Interface method definitions
- **Implementation**: Implemented by `BIGDatabaseFacade`
- **Documentation**: Interface contract specifications

### **Game Logic Classes:**

#### **`Game`**
- **Purpose**: Core game entity management
- **Methods**: Game lifecycle management
- **Features**: Game state, period management, company tracking
- **Documentation**: Complete game management API

#### **`Job`**
- **Purpose**: Construction job representation
- **Methods**: Job management and bidding
- **Features**: Job details, bidding, completion tracking
- **Documentation**: Job lifecycle and management

### **User Management Classes:**

#### **`LoginBroker`**
- **Purpose**: Authentication and session management
- **Methods**: Login verification, session handling
- **Features**: Multi-user authentication, game access control
- **Documentation**: Authentication workflow and security

## Deprecated API Documentation

### **Deprecated Classes:**
- **`CompanyX`**: Deprecated July 16, 2003, replaced by `ComputerControlledContractor`
- **Reason**: Architectural improvement with better separation of concerns

### **Deprecated Methods:**
- **`Bid.getCompany()`**: Replaced by direct company ID access
- **`Bid.setCompany(ExpandedCompany)`**: Replaced by company ID setting
- **Reason**: Elimination of `ExpandedGame` and `ExpandedCompany` classes

## File Statistics

### **Documentation Coverage:**
- **Total HTML Files**: 200+ documentation files
- **Package Documentation**: 8 main packages documented
- **Class Documentation**: 150+ classes documented
- **Method Documentation**: 1000+ methods documented
- **Field Documentation**: 500+ fields documented

### **File Types:**
- **HTML Files**: 200+ .html files
- **CSS Files**: 1 stylesheet.css
- **Package Files**: 8 package-summary.html files
- **Class Files**: 150+ class documentation files
- **Index Files**: 5 index and navigation files

## Build Integration

### **Generation Process:**
The javadoc is generated as part of the build process using the `build.xml` target:

```xml
<target name="javadoc">
  <mkdir dir="javadoc" />
  <javadoc packagenames="edu.calpoly.*"
           sourcepath="src/java"
           destdir="javadoc"
           author="true"
           version="true"
           classpath="${classpath}">
    <bottom>Cal Poly Library Multimedia 2003</bottom>
  </javadoc>
</target>
```

### **Build Configuration:**
- **Source Path**: `src/java`
- **Output Directory**: `javadoc`
- **Package Coverage**: `edu.calpoly.*`
- **Author Information**: Included
- **Version Information**: Included
- **Footer**: "Cal Poly Library Multimedia 2003"

## Usage and Navigation

### **Accessing Documentation:**
1. **Open `index.html`** in a web browser
2. **Use frame navigation** to browse packages and classes
3. **Search functionality** for quick class lookup
4. **Cross-references** for related class navigation

### **Navigation Tips:**
- **Package View**: Start with package summaries for overview
- **Class View**: Use class documentation for detailed API information
- **Method View**: Check method signatures and parameters
- **Deprecated View**: Review deprecated list for migration guidance

## Notes
- The javadoc represents a complete API reference for the BIG application
- All classes, methods, and fields are documented with JavaDoc comments
- The documentation follows standard Java documentation conventions
- Deprecated APIs are clearly marked with replacement information
- The frame-based layout provides efficient navigation for large codebases
- The documentation serves as both developer reference and educational resource
- Generated in 2003, it represents the state of the codebase at that time
- The custom stylesheet provides a professional appearance consistent with the application's branding
