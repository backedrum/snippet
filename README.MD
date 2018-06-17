## Snippet web application

### What it can be used for?
It can be used for management of your code snippets, diagrams and other small stuff that is not a really complete application.

### Prerequisites
Java 1.9+,
Maven 3.2.3+,
Docker.

### How to run?
Simply execute <b>./run.sh</b>. 

It will create and initialize a docker container for Postgres DB if executed for the first time.
It will also ask you to create an admin user to be used for Snippet. 

Point your browser to <i>localhost://8080</i> if not done automatically.

### Stop application
Execute <b>./stop-server.sh</b> to stop java server or simply kill a process.

### Cleanup
Run <b>./destroy.sh</b> to stop the application and remove Postgres Docker container.

### License
Apache License 2.0
