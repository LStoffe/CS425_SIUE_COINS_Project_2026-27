# BIG Application Documentation Folder

## Overview
The `docs/` folder contains comprehensive documentation for the BIG (Business Information Game) application, including software requirements specifications, design documents, database schemas, user manuals, and example files. This folder represents the complete documentation suite for the educational construction business simulation game.

## Documentation Structure

### `/docs/` - Main Documentation Directory
Contains the primary documentation files and subdirectories:

#### **Core Documentation Files:**
- **`adminManual.doc`** - Administrator manual (Microsoft Word format)
- **`BigInstructionManualv2.1.doc`** - Main user instruction manual
- **`Big Review Questions.doc`** - Review questions for educational use
- **`Big Review Questions (answers).doc`** - Answer key for review questions
- **`Big Image Banners.doc`** - Documentation for image banners
- **`new_accouting.XLS`** - Accounting documentation (Excel format)

### `/docs/srs/` - Software Requirements Specification

#### **Primary SRS Documents:**
- **`bigreqv22.xml`** - Main SRS in DocBook XML format (2,867 lines)
- **`bigreqv22.pdf`** - Generated PDF version of SRS
- **`srs_BIGv4-0.doc`** - Latest version SRS (v4.0)
- **`srs_BIGv3-2.doc`** - Previous version SRS (v3.2)
- **`srs_BIGv3.doc`** - Earlier version SRS (v3.0)

#### **SRS Supporting Files:**
- **`build_srs.xml`** - Apache Ant build file for generating SRS PDF
- **`BIG-FAQ.doc`** - Frequently Asked Questions document
- **`srs_BIGv3-2-change_log.doc`** - Change log for v3.2
- **`srs_BIGv3-2-CHECKLIST.doc`** - SRS checklist document

#### **Example Data Files (Excel):**
- **`balanceSheetExample.xls`** - Balance sheet example data
- **`incomeStatementExample.xls`** - Income statement example
- **`cashFlowReportExample.xls`** - Cash flow report example
- **`jobCostExample.xls`** - Job cost report example
- **`completedContractsExample.xls`** - Completed contracts example
- **`contractsInProgressExample.xls`** - Contracts in progress example
- **`ratiosReportExample.xls`** - Financial ratios report example

### `/docs/designdocs/` - Design Documentation

#### **Design Documents:**
- **`BIGDesignGuide.doc`** - Design guide and standards
- **`BIGDefectReport.doc`** - Defect reporting documentation
- **`BIGDefectReport.pdf`** - PDF version of defect report
- **`BigInstructionManualv2.1.pdf`** - PDF version of instruction manual

#### **Design Templates and Examples:**
- **`bidEstimateForm.xls`** - Bid estimation form template
- **`bidEstimateFormWithValues.xls`** - Bid estimation form with example data
- **`Gantt_Chart.xls`** - Gantt chart template
- **`Gantt_Chart_example.xls`** - Gantt chart with example data
- **`Bid.gif`** - Bid-related image/graphic

### `/docs/sdd/` - Software Design Document

#### **Database Design Files:**
- **`database/`** - Database schema documentation

##### **PowerDesigner Files:**
- **`BIG-main-tables.PDM`** - Physical Data Model for main tables
- **`BIG-main-tables.CDM`** - Conceptual Data Model for main tables
- **`BIG-main-tables.PDB`** - PowerDesigner database file
- **`BIG-main-tables.jpg`** - Visual representation of main tables
- **`BIG-gameparameters.PDM`** - Physical Data Model for game parameters
- **`BIG-gameparameters.CDM`** - Conceptual Data Model for game parameters
- **`BIG-gameparameters.PDB`** - PowerDesigner database file
- **`BIG-report-tables.PDM`** - Physical Data Model for report tables
- **`BIG-report-tables.pdm`** - Alternative format report tables model
- **`BIG-report-tables.PDB`** - PowerDesigner database file

##### **Database Logs:**
- **`sqlnet.log`** - SQL network connection log

## Software Requirements Specification (SRS) Details

### **SRS Document Structure:**
The main SRS document (`bigreqv22.xml`) is written in DocBook XML format and contains:

#### **Document Information:**
- **Title**: Software Requirements Specification for BIG v2.2
- **Subtitle**: Building Industry Game
- **Authors**: Dara Manker, Kevin Craig
- **Creation**: February 2001
- **Revisions**: Multiple revisions through August 2001

#### **Key Sections:**
1. **Introduction**
   - The BIG Game overview
   - BIG Companies (student teams as contractors)
   - BIG Timeline (12 periods, 2 months each)
   - Background (DOS to web-based migration)

2. **Game Description**
   - Construction business simulation
   - Competitive bidding environment
   - Financial management requirements
   - Strategic decision-making components

3. **Use Cases**
   - Administrator functions
   - Company operations
   - Game management
   - Reporting capabilities

4. **Non-Functional Requirements**
   - Performance requirements
   - Usability standards
   - Reliability specifications
   - Maintainability requirements

### **SRS Build Process:**
The `build_srs.xml` file provides automated document generation:
- **Input**: DocBook XML source (`bigreqv22.xml`)
- **Process**: XSLT transformation using modified DocBook stylesheets
- **Output**: PDF document (`bigreqv22.pdf`)
- **Tools**: Apache Xalan, Apache FOP, custom stylesheets

## Database Design Documentation

### **PowerDesigner Models:**
The database design is documented using PowerDesigner, a professional database modeling tool:

#### **Main Tables Model:**
- **Purpose**: Core application tables (ADMIN, GAME, COMPANY, JOB, BID, etc.)
- **Format**: Physical Data Model (PDM) and Conceptual Data Model (CDM)
- **Visual**: JPG representation of table relationships

#### **Game Parameters Model:**
- **Purpose**: Game configuration and parameter tables
- **Content**: Overhead costs, job availability, labor parameters
- **Format**: PDM and CDM files

#### **Report Tables Model:**
- **Purpose**: Financial and business reporting tables
- **Content**: Financial reports, job cost tracking, performance metrics
- **Format**: PDM files

## Example Data Files

### **Financial Report Examples:**
- **Balance Sheet**: Assets, liabilities, equity examples
- **Income Statement**: Revenue, expenses, profit/loss examples
- **Cash Flow**: Operating, investing, financing activities
- **Job Cost**: Direct costs, overhead, profit margins
- **Ratios Report**: Financial ratios and performance metrics

### **Contract Management Examples:**
- **Completed Contracts**: Finished project examples
- **Contracts in Progress**: Active project examples
- **Bid Estimates**: Bidding form examples with sample data

### **Project Management Examples:**
- **Gantt Charts**: Project scheduling examples
- **Bid Forms**: Estimation and bidding templates

## Documentation Standards

### **Format Standards:**
- **Primary Format**: Microsoft Word (.doc) for most documents
- **Technical Format**: DocBook XML for SRS
- **Data Format**: Microsoft Excel (.xls) for examples
- **Database Format**: PowerDesigner files (.PDM, .CDM, .PDB)
- **Graphics**: GIF and JPG for images

### **Version Control:**
- **SRS Versions**: v2.2, v3.0, v3.2, v4.0
- **Manual Versions**: v2.1
- **Change Tracking**: Dedicated change log documents
- **Checklists**: Quality assurance checklists

## Educational Documentation

### **Learning Materials:**
- **Instruction Manual**: Complete user guide
- **Review Questions**: Educational assessment tools
- **Answer Keys**: Instructor resources
- **FAQ**: Common questions and answers
- **Examples**: Real-world scenario examples

### **Administrative Documentation:**
- **Admin Manual**: System administration guide
- **Defect Reports**: Issue tracking and resolution
- **Design Guide**: Development standards and guidelines

## File Counts and Statistics

### **Documentation Summary:**
- **Total Files**: 50+ documentation files
- **SRS Documents**: 8 files (XML, PDF, DOC formats)
- **Design Documents**: 10 files (DOC, PDF, XLS formats)
- **Database Models**: 10 PowerDesigner files
- **Example Files**: 11 Excel files with sample data
- **Supporting Files**: 11 additional documentation files

### **File Types:**
- **Word Documents**: 15+ .doc files
- **Excel Files**: 11 .xls files
- **PDF Files**: 3 .pdf files
- **PowerDesigner Files**: 10 .PDM/.CDM/.PDB files
- **XML Files**: 2 .xml files
- **Image Files**: 2 .gif/.jpg files

## Build and Generation Process

### **SRS Generation:**
```xml
<!-- build_srs.xml -->
<target name="pdf" depends="fo">
  <java classname="org.apache.fop.apps.Fop" fork="yes">
    <arg line="${document}.fo -pdf ${document}.pdf"/>
  </java>
</target>
```

### **Dependencies:**
- **Apache Xalan**: XSLT processing
- **Apache FOP**: PDF generation
- **DocBook XSL**: Document formatting
- **Custom Stylesheets**: Modified DocBook stylesheets

## Notes
- The documentation represents a complete software engineering process
- SRS follows IEEE standards for software requirements
- Database design uses professional modeling tools
- Examples provide realistic business scenarios
- Documentation supports both educational and technical users
- Version control and change tracking are well-maintained
- Multiple formats ensure accessibility across different tools and platforms
