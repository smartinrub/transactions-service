# transactions-service

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Run Tests](#run-tests)
* [API Usage](#api-usage)

## General info
This application is to store transaction and get transaction status
	
## Technologies
Project is created with:
* Java: 14
* Spring Boot: 2.3.1.RELEASE
* H2 embedded MySQL DB
* Cucumber: 6.2.2
	
## Setup
To run this project use Maven:

```
./mvnw clean install
./mvnw spring-boot:run
```

Once it's built the service can be also ran like this:

```
java -jar target/transactions-service-1.0.0.jar
```

The application is available at `localhost:8080`

An account with iban number `ES9820385778983000760236` is already loaded.

## Run Tests

```
./mvnw clean test
```

## API Usage

### Create Transaction

- Request:
```
POST http://localhost:8080/transactions
Content-Type: application/json

{
  "reference": "f8145c28-4730-4afc-8cf5-11934d94b06f",
  "accountIban": "ES9820385778983000760236",
  "date": "2019-07-16T16:55:42.000Z",
  "amount": 193.38,
  "fee": 3.18,
  "description": "Restaurant payment"
}
```

- Response

```
HTTP/1.1 201
```

if account not found:
```
HTTP/1.1 404
```

if invalid transaction:
```
HTTP/1.1 422
```

### Search Transactions

- Request

```
GET http://localhost:8080/transactions/ES9820385778983000760236
Accept: application/json
```

- Response

```
HTTP/1.1 200
[{
  "reference": "f8145c28-4730-4afc-8cf5-11934d94b06f",
  "accountIban": "ES9820385778983000760236",
  "date": "2019-07-16T16:55:42.000Z",
  "amount": 193.38,
  "fee": 3.18,
  "description": "Restaurant payment"
},
{
  "reference": "5531f334-70f7-4275-9f19-d5130af816bf",
  "accountIban": "ES9820385778983000760236",
  "date": "2020-07-16T16:55:42.000Z",
  "amount": -50.38,
  "fee": 3.18,
  "description": "Groceries"
}]
```

### Get Transaction Status

- Request

```
POST http://localhost:8080/status
Content-Type: application/json

{
  "reference": "f8145c28-4730-4afc-8cf5-11934d94b06f",
  "channel": "CLIENT"
}
```

- Response

```
HTTP/1.1 200
{
    "reference":"12345A",
    "status":"PENDING"
    "amount":193.38,
    "fee":3.18
}
```
