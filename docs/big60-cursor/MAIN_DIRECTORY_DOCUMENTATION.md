# BIG Application Main Directory Documentation

## Overview
The main directory of the BIG (Business Information Game) application contains essential project files including build configuration, licensing information, version tracking, and project history. These files provide the foundation for building, deploying, and understanding the application's legal and technical context.

## Main Directory Files

### **Build Configuration**

#### **`build.xml`** - Apache Ant Build File
- **Purpose**: Main build configuration for the BIG application
- **Author**: Kevin Craig
- **Created**: Tuesday, April 24, 2001, 03:16:00 PDT
- **Modified**: Friday, January 28 (year not specified)
- **Version**: BIG v4
- **Copyright**: (c)2002, Trustees of the California State University
- **Tool**: Apache Ant 1.2

##### **Build Configuration Details:**
```xml
<project name="BIG" default="dist" basedir=".">
  <property name="version" value="$Revision $" />
  <property name="src" value="src/java" />
  <property name="build" value="build" />
  <property name="dist" value="dist" />
  <property name="conf" value="conf" />
  <property name="web" value="src/web" />
  <property name="archive" value="big" />
  <property name="webapps" value="..\..\..\tomcat\webapps" />
</project>
```

##### **Key Build Targets:**
- **`prepare`**: Creates build directory structure
- **`stage`**: Copies JAR files to lib directory
- **`generateParser`**: Uses JLex/CUP to generate formula parser
- **`compile`**: Compiles Java source files with debug information
- **`dist`**: Creates deployable WAR file
- **`clean`**: Removes build artifacts
- **`javadoc`**: Generates API documentation

##### **Build Process Features:**
- **Parser Generation**: JLex and CUP integration for formula parsing
- **Debug Compilation**: Includes line numbers, variables, and source debugging
- **Deprecation Warnings**: Enables deprecation warnings during compilation
- **WAR Creation**: Packages application into deployable WAR file
- **Javadoc Generation**: Creates comprehensive API documentation

### **Version and History Tracking**

#### **`version.txt`** - Current Version Information
- **Content**: `v4.0.1.2w`
- **Purpose**: Tracks the current version of the application
- **Format**: Simple text file with version number
- **Status**: Working version (indicated by 'w' suffix)

#### **`history.txt`** - Development History and Change Log
- **Purpose**: Documents version history and significant changes
- **Format**: Chronological list of versions with descriptions

##### **Version History:**
- **v4.0.1.2w (2/11/2006)** - Current working version
  - Infrastructure improvements
  - Scaled back log4j/commons-logging usage
  - Implemented BIGLog classes for better readability
  - Logs organized by game, company, and job

- **v4.0.1.2+ (2/8/2006)** - Installed version on CMWEB
  - Bug fix for share of savings calculation
  - Fixed negative revenue issue for jobs without share of savings

- **v4.0.1.1 (1/7/2006)**
  - Bug fixes for share of savings handling

- **v4.0.1 (8/25/2005)**
  - Employee hire/fire scheduling capability
  - Foundation for office overhead calculation

### **Licensing and Legal Information**

#### **`apache_LICENSE.txt`** - Apache License 2.0
- **License**: Apache License Version 2.0, January 2004
- **URL**: http://www.apache.org/licenses/
- **Purpose**: Governs use of Apache software components
- **Scope**: Covers Apache Struts, Commons libraries, and other Apache components

##### **Key License Terms:**
- **Use**: Free use for any purpose
- **Modification**: Allowed with proper attribution
- **Distribution**: Allowed with license inclusion
- **Patent Grant**: Includes patent license grant
- **Trademark**: No trademark rights granted

#### **`cup_LICENSE.txt`** - CUP Parser Generator License
- **Copyright**: 1996-1999 by Scott Hudson, Frank Flannery, C. Scott Ananian
- **License**: Permissive license for CUP parser generator
- **Purpose**: Covers the CUP parser generator used for formula parsing

##### **License Terms:**
- **Use**: Free use for any purpose without fee
- **Modification**: Allowed with copyright notice preservation
- **Distribution**: Allowed with license inclusion
- **Warranty**: No warranties provided
- **Liability**: Authors disclaim all liability

#### **`jlex_LICENSE.txt`** - JLex Lexical Analyzer License
- **Copyright**: 1996-2003 by Elliot Joel Berk and C. Scott Ananian
- **License**: Permissive license for JLex lexical analyzer
- **Purpose**: Covers the JLex lexical analyzer used for formula parsing

##### **License Terms:**
- **Use**: Free use for any purpose without fee
- **Modification**: Allowed with copyright notice preservation
- **Distribution**: Allowed with license inclusion
- **Warranty**: No warranties provided
- **Liability**: Authors disclaim all liability

#### **`NOTICE.txt`** - Third-Party Software Notice
- **Content**: Acknowledges Apache Software Foundation components
- **Purpose**: Required attribution for Apache software usage
- **Components**: Apache Struts, Commons libraries, and other Apache projects

### **Project Documentation**

#### **`scheduling.ppt`** - Project Scheduling Presentation
- **Format**: Microsoft PowerPoint presentation
- **Purpose**: Project scheduling and planning documentation
- **Content**: Likely contains project timelines, milestones, and scheduling information

### **Version Control Files**

#### **`vssver.scc`** - Visual SourceSafe Version Control
- **Purpose**: Visual SourceSafe version control metadata
- **Content**: Version control information for the main directory
- **Tool**: Microsoft Visual SourceSafe (legacy version control system)

## Build System Architecture

### **Apache Ant Integration:**
- **Build Tool**: Apache Ant 1.2
- **Java Version**: Compatible with J2SE 1.4+
- **Target Platform**: Windows (based on path separators)
- **Deployment**: Tomcat servlet container

### **Build Process Flow:**
1. **Preparation**: Create directory structure
2. **Staging**: Copy JAR dependencies
3. **Parser Generation**: Generate formula parser using JLex/CUP
4. **Compilation**: Compile Java source with debug information
5. **Resource Copying**: Copy JSP, images, CSS, and configuration files
6. **Packaging**: Create WAR file for deployment
7. **Documentation**: Generate Javadoc API documentation

### **Dependencies Management:**
- **Classpath**: Includes Tomcat libraries and project JARs
- **JAR Files**: All JARs from `lib/` directory included
- **Tomcat Integration**: References Tomcat installation paths
- **Library Versions**: Legacy versions compatible with Struts 1.x

## Legal Compliance

### **Open Source Compliance:**
- **Apache License**: Properly included for Apache components
- **CUP License**: Included for parser generator
- **JLex License**: Included for lexical analyzer
- **Attribution**: NOTICE.txt provides required attributions

### **Copyright Information:**
- **Main Application**: Copyright (c)2002, Trustees of the California State University
- **Third-Party Components**: Properly attributed with individual licenses
- **Educational Use**: Designed for educational purposes at Cal Poly

## Development Environment

### **Target Environment:**
- **Operating System**: Windows (based on path configurations)
- **Java Version**: J2SE 1.4 or higher
- **Build Tool**: Apache Ant 1.2
- **Servlet Container**: Apache Tomcat 4.x
- **Database**: PostgreSQL 8.0+
- **Version Control**: Visual SourceSafe (legacy)

### **Development Timeline:**
- **Initial Development**: 2001 (build.xml creation)
- **Active Development**: 2001-2006
- **Current Version**: v4.0.1.2w (February 2006)
- **Last Update**: February 11, 2006

## File Statistics

### **Main Directory Summary:**
- **Build Files**: 1 (build.xml)
- **License Files**: 4 (Apache, CUP, JLex, NOTICE)
- **Version Files**: 2 (version.txt, history.txt)
- **Documentation**: 1 (scheduling.ppt)
- **Version Control**: 1 (vssver.scc)
- **Total Files**: 9 files

### **File Types:**
- **XML**: 1 file (build.xml)
- **Text**: 5 files (licenses, version, history, notice)
- **PowerPoint**: 1 file (scheduling.ppt)
- **Version Control**: 1 file (vssver.scc)

## Notes
- The main directory provides essential project foundation files
- Build system uses Apache Ant with comprehensive build targets
- Proper legal compliance with open source licenses
- Version tracking shows active development from 2001-2006
- Educational focus with Cal Poly copyright and development
- Legacy technology stack but well-structured and documented
- Professional development practices with proper version control
- Complete build automation from source to deployment
- Integration with Tomcat servlet container for deployment
- Support for both development and production environments
