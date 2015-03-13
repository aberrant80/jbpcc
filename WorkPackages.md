List of work packages for beta release, temporary scheduled at 31st-May-2008

### Batch Manager ###
Owner: Chin Fei, Requirements:
  * Support configuration of Java or Non Java base batch process via XML configuration.
  * Support of configuration of run once process and long running process.
  * Support execution process with parameters
  * Communicate with Java process wrap by JMX agent, and obtain JVM runtime statistic to Web Admin Console
  * Support remote logging management for Java process that utilize apache log4j API
  * Provide constant heart beat monitoring on configure process, and notify WAC on process run time status status, e.g process started or terminated outside WAC.
  * Communicate with WAC Batch, Schedule and Monitoring module for batch execution (either schedule or ad hoc), provide runtime statistic, and log management if available.

### WAC domain model Framework ###
Owner: aberrant80
  * Deliver framework to support CRUD of WAC Domain Model, including Users, Scheduling, and ServerVO.


### WAC Database Setup ###
Owner: James Khoo
  * Study Derby, and create SQL install script
  * Embedding derby into WAC
  * Create WAC Startup listener to initialize Apache Derby

### WAC Security ###
Owner: James Khoo
  * Deliver Authentication module base on User Table on Apache Derby


### WAC Operation ###
Owner : Ahfei78
  * Support batch process execution
  * Receive notification from Batch Manager, and render process status accordingly
  * Render process JVM statistics if available
  * Provide remote log management of running process if available.


### WAC Scheduling ###
Owner : Ahfei78
  * Allows schedule of processes across multiple servers.



### JBPCC Presentation ###
Owner: Lyn Yen
  * Create banner, logo for WAC
  * Page Layout, and CSS design for WAC
  * Touch up on JBPCC project page, and project's blog.


