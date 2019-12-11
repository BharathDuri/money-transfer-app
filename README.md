# Revolut-challenge
A Java Restful api to transfer money between two accounts


### Service Run Instructions

Service runs on jetty server which starts and sets up a H2 in memory database with sample data.
* Run App.java located under package com.moneytranser.app as a Java Application.

### HTTP Service methods

| HTTP METHOD | PATH	| USAGE
--- | --- | ---
PUT	|	`http://localhost:8040/revolut/transfer`	|	Money transfer between two accounts.
GET	|	`http://localhost:8040/revolut/accountDetails/{accountNumber}`	|	Fetch account details for an account number
POST	|	`http://localhost:8040/revolut/createNewUser`	|	Create a new Account
DELETE	|	`http://localhost:8040/revolut/deleteUser/{accountNumber}`	|	Delete an account using account number

### HTTP Requests and Sample JSON for Transfer and creation of account


#### Money Transfer Between Two Accounts:
`http://localhost:8040/revolut/transfer`
```json
{
    "fromAccountNumber" : 101,
    "toAccountNumber" : 123,
    "amount" : 10
}
```
#### Create New User:


`http://localhost:8040/revolut/createNewUser`
```json
{
    "accountNumber" : 111,
    "firstName" : "George",
    "lastName"  : "Lincoln",
    "balance" : 10000
}
```

### HTTP Requests for Get Account Details and Delete an account

#### Get Account Details
`http://localhost:8040/revolut/accountDetails/101`

#### Delete an account
`http://localhost:8040/revolut/deleteUser/101`


### Preloaded data into H2 DB upon starting Server:
```
INSERT INTO Account(ACCOUNT_NUMBER, FIRST_NAME, LAST_NAME, LOCATION, BALANCE)
VALUES (101, 'Bharath', 'Duri', 'Germany',  10000);


INSERT INTO Account(ACCOUNT_NUMBER, FIRST_NAME, LAST_NAME, LOCATION, BALANCE)
VALUES (102, 'Mouni', 'Duri', 'Germany',  10000);

INSERT INTO Account(ACCOUNT_NUMBER, FIRST_NAME, LAST_NAME, LOCATION, BALANCE)
VALUES (103, 'Jon', 'Doe', 'Germany',  10000);

INSERT INTO Account(ACCOUNT_NUMBER, FIRST_NAME, LAST_NAME, LOCATION, BALANCE)
VALUES (104, 'Larry', 'Allen', 'Germany',  10000);
```
