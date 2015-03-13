An AJAX web application designed to manage and schedule any remote batch process, with additional monitoring capabilities for Java processes via JMX.

Features:
  * An in-memory module for remote servers to register, execute, and manage batch processes.
  * Time-based and duration-based scheduling of any configured or registered batch process for execution.
  * Monitoring currently running processes, including resources used and logs.
  * Server management for configuration of servers that hold remote batch processes.
  * A simple rights-based user management module.

Major technologies or frameworks used:
  * [Spring](http://springframework.org)
  * [Quartz](http://www.opensymphony.com/quartz/)
  * [IceFaces](http://www.icefaces.org/main/home/index.jsp)


---

## High-level Design ##
The Batch Manager is a Java application that runs in the background on a remote server. It registers itself with JBPCC and listens for execution commands. It is responsible for registering and managing the batch processes available on that server.

The web application portion provides an AJAX interface for users to configure the various aspects of JBPCC, including server management, user management, scheduling, and monitoring. All internal data are backed by an embedded Apache Derby database.
![http://jbpcc.googlecode.com/svn/wiki/jbpccDesign.png](http://jbpcc.googlecode.com/svn/wiki/jbpccDesign.png)

## Roadmap ##

## FAQ ##