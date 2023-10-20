# moneytransf
Sample application for journalling money transfers

Postgresql database is used.

## Implemented endpoints:
 - POST /import/merchants - Import merchants from csv file. The format of each line is as follows: merchant,merchant@mail.com,merchant@mail.com,ENABLED
 - GET /merchants/ - list all merchants
 - UPDATE /merchants/{merchantId} - update merchant. All fields can be updated except totalTransactionSum. 
 - DELETE /transactions/forceClean?millisAgo=number - cleans all transactions except those before millisAgo milliseconds.
 - DELETE /transactions/ - Forces transaction deletion task.
 - POST /transactions/ - Performs a transaction

#### POST transaction body is looking like this:
```JSON
{
"transactionType": "CHARGE",
"amount": 150,
"customerEmail": "string@abv.bg",
"customerPhone": "string@abv.bg",
"referencedTransactionId": "649a7aaf-e334-4a74-a3cf-73783f1f9e13"
}
```
#### transaction type can be four types: 
 - AUTHORIZE - Hold customer's amount
 - CHARGE - Charge amount from customer
 - REVERSAL - Invalidate AUTHORIZE transaction
 - REFUND - Reverse specific amount from CHARGE transaction.

amount field is used by AUTHORIZE, CHARGE amd REFUND transactions.

referencedTransactionId field is used by REVERSAL and REFUND transactions.

A scheduled task in (CleanupTasks.java) is running on every 5 minutes that clears all transactions that are older than 1 hour

Endpoints are accessible from the swagger interface which can be accessed by http://localhost:8080/swagger-ui.html

# Using Docker Compose
 1. Run ./gradlew build on the moneystransfapp

 2. Execute the following commands:
  - docker compose build
  - docker compose up

 3. Access application on http://localhost:8080/swagger-ui.html

Used Docker Compose version v2.17.3

# Running the application

Current application has one hardcoded admin and two hardcoded merchants
 - admin:password
 - johnwill@merchant.com
 - petersecada@merchant.com

For this implementation, merchant's username is his mail and is used to register into the merchants table.
