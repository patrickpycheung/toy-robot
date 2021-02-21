# The Toy Robot Application

  Table of contents

  * [**About the application**](#about-the-application)
  * [**Version history**](#version-history)
  * [**Assumptions**](#assumptions)
  * [**Operation manual**](#operation-manual)
    + [**Pre-requsites**](#pre-requsites)
    + [**Steps**](#steps)
  * [**Architecture**](#architecture)
    + [**Data storage of Robot**](#data-storage-of-robot)
    + [**Validation**](#validation)
    + [**Logging**](#logging)
  * [**Development approach**](#development-approach)
  * [**Technology stack**](#technology-stack)

## **About the application**

The Toy Robot application is a simulation of a toy robot moving on a square tabletop of dimensions 5 units x 5 units.
<br/>
<br/>
<img src="https://bn1301files.storage.live.com/y4mVkXFCti071xnd6Hsd8f5XTsyDF27CXHdGetLq5LCxYeJkjer9zZFEKsDPYPRMFMitRwizM1GVoiP80-DXDwAO5Yc4riVDqk1Sh9k6vunnW54umW0bHUZICPoSlMUFtQwH4oejAe8fkeKy-E0MmImeUHAaruYikCutTkM65iSjnum1QsSTkl3XlK93-ceBDmO?width=2880&height=792&cropmode=none" alt="Welcome_Page">

## **Version history**

  | Editor | Version | Date |Description|
  | --- | --- | --- | --- |
  | N/A | 1.0.0| 21 Feb 2021 |Initial release|

## **Assumptions**

The following assumptions are being made for the application:
* Only 1 user will use the application at a time
* There is only 1 robot on the table
* The state of the robot will not persist, and will be lost when system shuts down
* Input is from standard input. The system handles 1 command at a time
* Input is case-insensitve

## **Operation manual**

  ### Pre-requsites

  * Maven
  * JDK8

  ### **Steps**

  The applcation can be started directly by running the following command at the project root folder

    mvn spring-boot:run

  If you would like to create a JAR file from the source, run the following command

    mvn clean package

  A JAR file will be created in the "target" folder and can be executed by the following command

    java -jar toy-robot-<artifact version>.jar

  Sample executions:
  
  * Example 1
  <img src="https://bn1301files.storage.live.com/y4m35iLXvgWjyafgJWA3QvIOz8lFyx7HnT-Q_I1eqHqlbn8-4LNHzC_RUvIae6y-31mCE4TtkHmIXfU7HkfpBVl-tJYxjul8b5RU-oSK9lEyCDxfNWLZh4MEvlMtnOrISqkzsud391oea6rLELM3AKT2GquYHjVe5_RdfMAwRqhGaWhOeExBqpX6jyEZ17aJzgJ?width=2880&height=370&cropmode=none" alt="Sample_Execution_01">
  
  * Example 2
  <img src="https://bn1301files.storage.live.com/y4m3p8jhbcKe5sp1Lz0PzYmccCREIt8I6r0TlOP_JmHQdt1OOLK-tixUtsfkwvGhquVS4D9Fvjpb6msnygXQYiuQ4wvBK34JnmxSkkaruQ3yfixDbPKfgADDmHjz7okS66AAmjdEhQodIbXC72GUjbgbEJ3SaEkVlFtmxNLLIHY-gCYshfDKIQw9G_9PlziB3KM?width=2880&height=370&cropmode=none" alt="Sample_Execution_02">

  * Example 3
  <img src="https://bn1301files.storage.live.com/y4mA-qFK193Nkk-bNYqZbJfEgN3mSPlDl4nDlX-dDMo3A4nkzs9EGi4lF9peZk7AAWOzcA-8cihMr7AuPdcFpQaJDwGy0jtQnmoa5VsP9SIpitEJ2ASObCKSMcc78mRB5bE3PI7leVqzANllHEYFcfrX65beqgdRVDkWBfokA3s_9GrOtRm4uG_kSPAPYdcmMR6?width=2880&height=662&cropmode=none" alt="Sample_Execution_03">

## **Architecture**

There are 2 main parts that constitutes the application, the frontend console and the backend services.

The frontend console receives input from the user, delegates the actual handling to the services, and displays messages to the user.

The backend services provide functions that corresponds to the commands ("PLACE/MOVE/LEFT/RIGHT/REPORT"). Any display (e.g. from the "REPORT" command) in the process will be provided back to the console and shown to the user.

### Data storage

A robot bean is created on system start up to keep track of the robot's status.

### Validation

A backend validation service is created to perform actions related to vefiying the user's inputs. Any problematic commands will result in an IllegalArgumentException. The error message will be displayed in the console notifying user of the casue of the issue.

### Logging

Logs will be grouped by INFO (currently at [JAR FOLDER]/logs/info.log) and ERROR logs (currently at [JAR FOLDER]/logs/error.log).

The info log contains all logs of the system, including system error logs if any.

The error log contains errors related to the program run.

All log files will be housekept daily or when the file size exceed the limit.

Log settings can be configured by editing logback-spring.xml.

## Development approach

The development phase had been progressed with Test Driven Developement (TDD)-like style.

Each of the backend services correspoinding to the commands ("PLACE/MOVE/LEFT/RIGHT/REPORT") had been implemented one by one. The desired outcomes (e.g. "MOVE" will advance 1 unit in the direction facing) were thought of before carrying out the implementation. After unit testing and confirming the functionablity of one service, another service would then be developed.

After the backend services were created, the frontend console application was connected to the backend services, and integration test was performed to verify the end-to-end functionability.

### Testing

Unit tests of the backend services will be run in the test phase of the maven build.

Integration test cases and results can be found in "Integration_test.docx".

## **Technology stack**

* Java 8
* Spring Boot v2.4.2
  * Dependencies:
    * lombok (Provides getters/setters to entity/model class properties, and provides slf4j support for logging)
    * spring-boot-starter-test (Provides JUnit support)
    * spring-boot-maven-plugin (Provides Maven support)
    * spring-boot-starter-actuator (For development)
    * spring-boot-devtools (For development)
