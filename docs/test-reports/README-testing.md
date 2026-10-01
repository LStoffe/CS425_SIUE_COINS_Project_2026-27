# BIG60 Test Evidence

These reports were generated from the original Big60 legacy codebase as part of the baseline testing documentation for the COINS BIG70 modernization project.

Open build/jacoco-report/index.html in a browser to view code coverage report.

## Contents
- build/test-reports/ — JUnit per-class text reports
- build/test-logs/ — BIG application logs produced during tests
- build/jacoco-report/ — JaCoCo HTML coverage site (open index.html)
- build/jacoco.exec — Raw JaCoCo execution data (optional if HTML is included)

## How these were generated
From the BIG60 project root:

- Run all tests:
  ant -Dtomcat="C:\apache-tomcat-9.0.115\apache-tomcat-9.0.115" test

- Generate coverage HTML:
  ant -Dtomcat="C:\apache-tomcat-9.0.115\apache-tomcat-9.0.115" coverage-report

## Environment
- JDK: 21 (JAVA_HOME set)
- Ant: 1.10.x on Windows
- Tomcat libs path: C:\apache-tomcat-9.0.115\apache-tomcat-9.0.115


## How to view
- Test reports: open files under build/test-reports/
- Logs: open files under build/test-logs/
- Coverage: open build/jacoco-report/index.html in a browser

## Notes
- Logging configured by build/test/classes/BIGLog.properties (from src/test/resources/BIGLog.properties).
- Subset runs possible with:
  ant -Dtomcat="..." -D"test.includes"="**/path/*Test.class" test