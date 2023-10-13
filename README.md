# moneytransf
Sample application for journalling money transfers

Postgresql database is used.

 - /import/merchants - Import merchants from csv file
 - /merchants/list - list all merchants
 - /transactions/forceClean?millisAgo=number - cleans all transactions except those before millisAgo milliseconds.
 - /transactions/cleanup - clean all transactions except those in the last hour
 - /transactions/pay - Execute payment task
 - /transactions/hold - Execute authorization task (hold an amount)
 - /transactions/reverse - Reverse an authorization task.

A scheduled task in (CleanupTasks.java) is running on every 5 minutes that clears all transactions that are older than 1 hour

Endpoints are accessible from the swagger interface which can be accessed by http://localhost:8080/swagger-ui.html

#Using Docker Compose
 1. Run ./gradlew build on the moneystransfapp

 2. Execute the following commands:
  - docker compose build
  - docker compose up

 3. Access application on http://localhost:8080/swagger-ui.html

Used Docker Compose version v2.17.3

