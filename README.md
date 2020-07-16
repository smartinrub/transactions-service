# transactions-service

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)

## General info
This project is simple Lorem ipsum dolor generator.
	
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

java -jar target/transactions-service-1.0.0.jar

## Run Tests

```
./mvnw clean test
```

## API Usage

### Create Transaction

- Request:
```
curl -X POST -H "Content-Type: application/json" http://localhost:8080/transactions -d 
'{
  "reference": "12345A",
  "accountIban": "ES9820385778983000760236",
  "date": "2019-07-16T16:55:42.000Z",
  "amount": 193.38,
  "fee": 3.18,
  "description": "Restaurant payment"
}'
```

- Response

```
HTTP/1.1 201
```

### Search Transaction

- Request

```
curl -X GET http://localhost:8080/transactions/ibans/ES9820385778983000760236
```

- Response

```
HTTP/1.1 200
{
  "reference": "12345A",
  "accountIban": "ES9820385778983000760236",
  "date": "2019-07-16T16:55:42.000Z",
  "amount": 193.38,
  "fee": 3.18,
  "description": "Restaurant payment"
}
```

### Get Transaction Status

- Request

```
curl -X GET http://localhost:8080/transactions/12345A/status?channel=CLIENT
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
