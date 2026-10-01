# BIG Application Lib Folder Documentation

## Overview
The `lib/` directory contains all third‑party libraries (JARs) and JSP Tag Library Descriptors (TLDs) required by BIG at build and runtime. During the Ant `dist` target, these JARs are copied into `build/dist/WEB-INF/lib` for deployment.

## Library Groups (JARs)
- **Web framework**
  - `struts.jar`: Apache Struts 1.x MVC framework
  - `standard.jar`, `jstl.jar`: JSTL implementation for JSPs
- **ORM and database**
  - `ibatis-common-2.jar`, `ibatis-sqlmap-2.jar`: iBATIS SQL Maps 2.x
  - `postgresql-8.0.309.jdbc3.jar`: PostgreSQL JDBC driver
  - `commons-dbcp-1.2.1.jar`, `commons-pool-1.2.jar`: JDBC connection pooling
  - `jdbc2_0-stdext.jar`: JDBC 2.0 standard extensions
- **Apache Commons utilities**
  - `commons-beanutils.jar`: Bean property access/conversion
  - `commons-collections-3.1.jar`: Collection utilities
  - `commons-digester.jar`: XML→object mapping (used by Struts configs)
  - `commons-fileupload.jar`: Multipart/form-data upload support
  - `commons-logging.jar`: Logging façade used by Struts and others
  - `commons-validator.jar`: Form validation (Struts Validator)
- **XML/XSLT and document generation**
  - `xerces.jar`: XML parser
  - `xalan.jar`: XSLT processor
  - `w3c.jar`: W3C DOM APIs
  - `fop.jar`: Apache FOP for PDF rendering (used by docs/SRS build)
- **Logging**
  - `log4j-1.2.9.jar`: Application logging backend
- **Parsers and regex**
  - `java_cup.jar`, `jlex.jar`, `antlr.jar`: Parser/lexer generators (used to build formula parser)
  - `jakarta-oro.jar`: Legacy regex library (used transitively by Commons)

## JSP Tag Libraries (TLDs)
- **JSTL Core/Functions**: `c.tld`, `c-1_0.tld`, `c-1_0-rt.tld`, `fn.tld`
- **Formatting**: `fmt.tld`, `fmt-1_0.tld`, `fmt-1_0-rt.tld`
- **SQL (JSTL)**: `sql.tld`, `sql-1_0.tld`, `sql-1_0-rt.tld`
- **XML (JSTL)**: `x.tld`, `x-1_0.tld`, `x-1_0-rt.tld`
- **Misc/Project**: `permittedTaglibs.tld`, `scriptfree.tld`

## How it’s used
- Ant `build.xml` copies all `lib/*.jar` into the WAR at `WEB-INF/lib`.
- JSPs declare and use the TLDs above for JSTL and project taglibs.
- iBATIS, JDBC driver, and DBCP provide database access and pooling.
- Struts + Commons Validator power the MVC actions and form validation.
- Xalan/Xerces/FOP support documentation and XML processing tasks.

## Notes
- Library versions are legacy but consistent with Struts 1.x era.
- If upgrading, keep transitive compatibility with Struts/iBATIS and JSTL.

