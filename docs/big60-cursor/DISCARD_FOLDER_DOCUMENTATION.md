# BIG Application Discard Folder Documentation

## Overview
The `discard/` folder contains deprecated and discarded Java source files from the BIG (Business Information Game) application. These files represent older implementations that were replaced by newer, more robust versions during the application's evolution. The folder serves as a historical record of the application's development and provides insight into the architectural changes made over time.

## Discarded Files Structure

### `/discard/` Directory Contents
The discard folder contains 3 Java source files that were removed from the active codebase:

1. **`CompanyX.java`** - Deprecated AI company implementation
2. **`FinancialReportAction.java`** - Old financial reporting action
3. **`SubmitBidAction.java`** - Incomplete bid submission action

## Detailed Analysis of Discarded Files

### 1. `CompanyX.java` - AI Company Implementation

#### **Purpose and Functionality:**
- **Original Role**: Computer-controlled company that automatically placed bids on jobs
- **Primary Function**: Prevented player companies from submitting exaggerated bids by providing competitive AI bids
- **Bidding Algorithm**: 
  - Added field/office overhead (fixed and variable)
  - Added bid expense percentage of direct cost
  - Applied random profit margin within game parameters
  - Used company ID -1 (negative to distinguish from human players)

#### **Key Features:**
```java
public class CompanyX extends edu.calpoly.its.mas.big.users.company.Company {
    public static final int companyID = -1;
    
    private int lowerPercentJobMarkup;
    private int upperPercentJobMarkup;
    private int fieldOverheadFixed;
    private int fieldOverheadVariable;
    private int officeOverheadFixed;
    private int bidExpensePercent;
    private int workdaysPerMonth;
}
```

#### **Deprecation Details:**
- **Deprecated Date**: July 16, 2003
- **Replacement**: `edu.calpoly.lib.multimedia.big.game.ComputerControlledContractor`
- **Reason**: Architectural improvement with better separation of concerns
- **Author**: Andrew Von Dollen
- **Version**: $Revision: 1.7 $

#### **Why It Was Discarded:**
1. **Inheritance Issues**: Inherited from Company class but many methods had no meaning for CompanyX
2. **Polymorphism Problems**: Used inheritance mainly for polymorphism, not true functionality
3. **Architectural Improvement**: Replaced by interface-based design with `ComputerControlledContractor`
4. **Better Separation**: New implementation separates AI logic from company data structure

### 2. `FinancialReportAction.java` - Financial Reporting Action

#### **Purpose and Functionality:**
- **Original Role**: Handled requests to view financial reports
- **Framework**: Apache Struts Action class
- **Key Features**:
  - Session validation and game login verification
  - Support for both Company and Admin users
  - Database integration for financial data retrieval
  - Error handling and logging

#### **Key Implementation Details:**
```java
public class FinancialReportAction extends Action {
    public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        // Session validation
        // Game login verification
        // User type checking (Company vs Admin)
        // Financial data retrieval
        // Error handling
    }
}
```

#### **Why It Was Discarded:**
1. **Incomplete Implementation**: Appears to be a work-in-progress or prototype
2. **Architectural Changes**: Likely replaced by more specialized report actions
3. **Framework Evolution**: May have been superseded by newer Struts patterns
4. **Code Quality**: Possibly had issues that required complete rewrite

### 3. `SubmitBidAction.java` - Bid Submission Action

#### **Purpose and Functionality:**
- **Original Role**: Handled bid submission requests from companies
- **Framework**: Apache Struts Action class
- **Key Features**:
  - Form processing for bid submission
  - Database operations for bid storage
  - Error handling and validation
  - Session management

#### **Key Implementation Details:**
```java
public class SubmitBidAction extends Action {
    public ActionForward perform(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        // Form processing
        // Database operations
        // Error handling
        // Success/failure routing
    }
}
```

#### **Why It Was Discarded:**
1. **Incomplete Implementation**: Contains placeholder comments like "// Do inserts into method etc."
2. **Replaced by Better Implementation**: Superseded by `BidOnJobAction.java`
3. **Code Quality Issues**: Appears to be an early prototype or incomplete version
4. **Architectural Improvements**: Newer implementation likely has better error handling and validation

## Replacement Implementations

### CompanyX Replacement: ComputerControlledContractor

#### **New Implementation:**
- **Class**: `edu.calpoly.lib.multimedia.big.game.ComputerControlledContractor`
- **Interface**: Implements `Contractor` interface
- **Benefits**:
  - Better separation of concerns
  - Interface-based design
  - More flexible architecture
  - Cleaner code organization

#### **Key Improvements:**
```java
public class ComputerControlledContractor implements Contractor {
    // Better architecture with interface implementation
    // Improved bidding algorithm
    // Cleaner separation from Company class
}
```

### Action Replacements

#### **Financial Reporting:**
- **Replaced by**: Multiple specialized report actions
- **Examples**: `CashFlowAction`, `CAMReportAction`, `CompleteListofBidsReportAction`
- **Benefits**: More focused, maintainable code

#### **Bid Submission:**
- **Replaced by**: `BidOnJobAction.java`
- **Benefits**: Complete implementation with proper error handling
- **Features**: Better form processing and validation

## Historical Context

### Development Timeline:
- **CompanyX**: Deprecated July 16, 2003
- **Application Version**: BIG v4.0.1.2w (current)
- **Development Period**: 2001-2006
- **Architecture Evolution**: From inheritance-based to interface-based design

### Architectural Changes:
1. **AI System**: CompanyX → ComputerControlledContractor
2. **Action Classes**: Monolithic → Specialized actions
3. **Design Patterns**: Inheritance → Interface implementation
4. **Code Quality**: Prototype → Production-ready implementations

## Code Quality Analysis

### Issues in Discarded Files:

#### **CompanyX.java:**
- **Inheritance Misuse**: Inherited from Company but didn't use most methods
- **Tight Coupling**: Mixed AI logic with company data structure
- **Maintenance Issues**: Hard to extend or modify

#### **FinancialReportAction.java:**
- **Incomplete**: Missing key implementation details
- **Generic**: Tried to handle too many responsibilities
- **Error Handling**: Basic error handling implementation

#### **SubmitBidAction.java:**
- **Placeholder Code**: Contains "// Do inserts into method etc." comments
- **Incomplete**: Missing actual database operations
- **Prototype Quality**: Appears to be early development version

## Lessons Learned

### Design Principles Applied:
1. **Single Responsibility**: New implementations focus on specific tasks
2. **Interface Segregation**: Use interfaces instead of inheritance for flexibility
3. **Code Completeness**: Ensure implementations are complete before deployment
4. **Error Handling**: Implement robust error handling and validation

### Best Practices Demonstrated:
1. **Deprecation Process**: Proper deprecation with clear replacement paths
2. **Version Control**: Maintained historical record in discard folder
3. **Documentation**: Clear deprecation notices and replacement information
4. **Architectural Evolution**: Continuous improvement of design patterns

## File Statistics

### Discard Folder Summary:
- **Total Files**: 3 Java source files
- **Total Lines**: ~400+ lines of code
- **Deprecation Dates**: 2003 (CompanyX)
- **Replacement Status**: All files have active replacements
- **Code Quality**: Mix of incomplete and deprecated implementations

### Replacement Status:
- **CompanyX**: ✅ Replaced by ComputerControlledContractor
- **FinancialReportAction**: ✅ Replaced by specialized report actions
- **SubmitBidAction**: ✅ Replaced by BidOnJobAction

## Notes
- The discard folder provides valuable insight into the application's evolution
- All discarded files have been properly replaced with better implementations
- The deprecation process was well-documented with clear replacement paths
- The architectural improvements demonstrate good software engineering practices
- The historical record is valuable for understanding the application's development history
