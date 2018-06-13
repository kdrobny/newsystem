# NEWSYSTEM

Newsystem is a demo java based web app that act as a proxy layer for the existing legacy system.

More information about legacy application can be found at the following URL:
https://hub.docker.com/r/yntelectual/legacy/

Newsystem project structure was build using maven archetype, that allow us to create a compliant Java EE 7 application using JSF 2.2, CDI 1.1, EJB 3.3, JPA 2.1 and Bean Validation 1.1.

    <groupId>org.wildfly.archetype</groupId>
    <artifactId>wildfly-javaee7-webapp-archetype</artifactId>
    <version>8.2.0.Final</version>

Newsystem can be used to :

- import project and all related data from the legacy system to the Newsystem by invoking a REST call

Test examples:

	http://localhost:8080/newsystem/rest/import/PRJ1
	http://localhost:8080/newsystem/rest/import/PRJ2
	http://localhost:8080/newsystem/rest/import/PRJ3

- list all projects in the Newsystem database by invoking REST call

Test example:

	http://localhost:8080/newsystem/rest/projects

- query details of the project along with all its tasks by invoking REST call

Test example:

	http://localhost:8080/newsystem/rest/projects/<projectId>

where projectId is project id in Newsystem.

- create new task for a given project by invoking REST service of type POST

Test example: 

	Invoke POST request at http://localhost:8080/newsystem/rest/projects/<projectId>/tasks with json data {"assignee":"test","status":"OPEN", "name":"new task"}

where projectId is project id in Newsystem.


Test examples suppose that application Newsystem run on localhost:8080


## Getting Started

### Prerequisites

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.


### Installing

Start JBoss WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Newsystem
-------------------------

NOTE: The following build command assumes you have configured your Maven user settings.

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this Newsystem.
3. Type this command to build and deploy the archive:

        mvn clean package wildfly:deploy

4. This will deploy `target/newsystem.war` to the running instance of the server.

You can also start the server and deploy the application from your favorite IDE. 

Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/newsystem/>.

Test REST calls examples should be also runnable.

Undeploy the Archive
--------------------

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this Newsystem.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


