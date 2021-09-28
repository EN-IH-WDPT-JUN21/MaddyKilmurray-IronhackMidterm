# Introduction

This is an API simulating a banking service. Users can be added, modified and removed, accounts can be created and modified and all services are secured using HTTP.BASIC protocols. 



### Installation

Clone or download the project from this git repository.

The database is set up as a H2 database and the password to access is:

**Username**: sysadmin, **Password**: 5y5@m1n

Open and run in your preferred IDE.



### Setup

In order to set up the database, it's best to inject it with some data. Once you have run the project for the first time, log back into H2 and check if the tables have data by running any of the following commands:

```
SELECT * FROM user;
SELECT * FROM role; 
SELECT * FROM account; 
SELECT * FROM third_party_account;
SELECT * FROM transaction;
```

If there is no data, please find some data samples at the bottom of this README.md



### Pathways

#### **User Pathways**

| Route Type | Route                            | Access Roles                     | Input Required                                       |
| ---------- | -------------------------------- | -------------------------------- | ---------------------------------------------------- |
| GET        | /users                           | ADMIN                            | NONE                                                 |
| GET        | /users/{username}                | ADMIN, ACCOUNTHOLDER, THIRDPARTY | String - Username                                    |
| GET        | /users/byid/{id}                 | ADMIN, ACCOUNTHOLDER, THIRDPARTY | Long - User ID                                       |
| POST       | /users/new/admin                 | ADMIN                            | UserDTO                                              |
| POST       | /users/new/accountholder         | ADMIN                            | UserDTO                                              |
| POST       | /users/new/thirdparty            | ADMIN                            | UserDTO                                              |
| PATCH      | /users/update/logindetails/{id}  | ADMIN, ACCOUNTHOLDER, THIRDPARTY | Long - User ID, String - Username, String - Password |
| PATCH      | /users/update/username/{id}      | ADMIN, ACCOUNTHOLDER, THIRDPARTY | Long - User Id, String - Username                    |
| PATCH      | /users/update/password/{id}      | ADMIN, ACCOUNTHOLDER, THIRDPARTY | Long - User Id, String - Password                    |
| PATCH      | /users/update/accountholder/{id} | ADMIN, ACCOUNTHOLDER             | Long - User ID, AccountHolderDTO                     |
| PATCH      | /users/update/thirdparty/{id}    | ADMIN, THIRDPARTY                | Long - User ID, ThirdPartyDTO                        |
| PATCH      | /users/update/admin/{id}         | ADMIN                            | Long - User ID, AdminDTO                             |
| DELETE     | /users/remove/{id}               | ADMIN                            | Long - User ID                                       |



#### **Account Pathways**

| Route Type | Route                                             | Access Roles                     | Input Required                                     |
| ---------- | ------------------------------------------------- | -------------------------------- | -------------------------------------------------- |
| GET        | /accounts                                         | ADMIN                            | NONE                                               |
| GET        | /accounts/byid/{id}                               | ADMIN, ACCOUNTHOLDER, THIRDPARTY | Long - Account ID                                  |
| POST       | /accounts/new/checking                            | ADMIN                            | CheckingAccountDTO                                 |
| POST       | /accounts/new/savings                             | ADMIN                            | SavingsAccountDTO                                  |
| POST       | /accounts/new/creditcard                          | ADMIN                            | CreditCardAccountDTO                               |
| POST       | /accounts/new/thirdparty                          | ADMIN                            | ThirdPartyAccountDTO                               |
| PATCH      | /accounts/update/status/{id}                      | ADMIN                            | Long - Account ID                                  |
| GET        | /accounts/getbalance/checking/{account_id}        | ADMIN, ACCOUNTHOLDER             | Long - Account ID, String - Account Owner Username |
| GET        | /accounts/getbalance/studentchecking/{account_id} | ADMIN, ACCOUNTHOLDER             | Long - Account ID, String - Account Owner Username |
| GET        | /accounts/getbalance/savings/{account_id}         | ADMIN, ACCOUNTHOLDER             | Long - Account ID, String - Account Owner Username |
| GET        | /accounts/getbalance/creditcard/{account_id}      | ADMIN, ACCOUNTHOLDER             | Long - Account ID, String - Account Owner Username |
| GET        | /accounts/getbalance/thirdparty/{account_id}      | ADMIN, THIRDPARTY                | Long - Account ID, String - Account Owner Username |
| PATCH      | /accounts/admin/transferfunds/                    | ADMIN                            | TransactionDTO                                     |
| PATCH      | /accounts/accountholder/transferfunds/{username}  | ADMIN, ACCOUNTHOLDER             | String - Username, TransactionDTO                  |
| PATCH      | /accounts/thirdparty/transferfunds/               | ADMIN, THIRDPARTY                | String - HashedKey, ThirdPartyTransactionDTO       |



### **DTO Templates**

*Please note: In all DTOs, any reference to secondary owner is optional, and can be removed if not required*

#### Accounts:

##### AccountDTO:

`{`
`"balance": {"amount":"NUMBER",`
					`"currency":"GBP"},`
`"primaryOwnerId":"USERID",`
`"secondaryOwnerId":"USERID"`
`}`

##### CheckingAccountDTO:

`{`
`"balance": {"amount":"NUMBER",`
					`"currency":"GBP"},`
`"primaryOwnerId":"USERID",`
`"secondaryOwnerId":"USERID"`
`"secretKey":"STRING"`
`}`

##### CreditCardAccountDTO

`{`
`"balance": {"amount":"NUMBER",`
					`"currency":"GBP"},`
`"primaryOwnerId":"USERID",`
`"secondaryOwnerId":"USERID"`
`"creditLimit": {"amount":"NUMBER",`
							`"currency":"GBP"},`
`"interestRate": {"amount":"NUMBER",`
							`"currency":"GBP"}`
`}`

##### SavingsAccountDTO

`{`
`"balance": {"amount":"NUMBER",`
					`"currency":"GBP"},`
`"primaryOwnerId":"USERID",`
`"secondaryOwnerId":"USERID"`
`"secretKey":"STRING"`,
`"interestRate": {"amount":"NUMBER",`
							`"currency":"GBP"}``
``}`

##### ThirdPartyAccountDTO

`{`
`"balance": {"amount":"NUMBER",`
					`"currency":"GBP"},`
`"primaryOwnerId":"USERID",`
`"secondaryOwnerId":"USERID"`
`"hashedKey":"STRING"`,
`"name":"STRING"`
``}`



#### Transactions

##### TransactionDTO

`{`
`"transactionAmount":"NUMBER",`
`"transferAccountId":"ACCOUNTID",`
`"receivingAccountId":"ACCOUNTID"`
`}`

##### ThirdPartyTransactionDTO

`{`
`"transactionAmount":"NUMBER",`
`"transferAccountId":"ACCOUNTID",`
`"receivingAccountId":"ACCOUNTID"`
`"receivingSecretKey":"STRING"`
`}`



#### Users:

##### AccountHolderDTO

`{`
`"name":"STRING",`
`"username":"STRING",`
`"password":"STRING",`
`"dateOfBirth":"YYYY-MM-DD",`
`"primaryAddress": {"houseNumber":"NUMBER",`
									`"streetName":"STRING",`
									`"city":"STRING",`
									`"postcode":"STRING",`
									`"country":"STRING"},`

`"mailingAddress": {"houseNumber":"NUMBER",`
									`"streetName":"STRING",`
									`"city":"STRING",`
									`"postcode":"STRING",`
									`"country":"STRING"},`
`"accounts": {"id":"NUMBER",`
					`"balance": {"amount":"NUMBER",`
					`"currency":"GBP"},`
					`"primaryOwnerId":"USERID",`
					`"secondaryOwnerId":"USERID"}`
`}`

##### AdminDTO

`{`
`"name":"STRING",`
`"username":"STRING",`
`"password":"STRING",`
`}`

##### ThirdPartyDTO

`{`
`"name":"STRING",`
`"username":"STRING",`
`"password":"STRING",`
`"secretKey":"STRING"`
`}`


