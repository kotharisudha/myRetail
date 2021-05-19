# myRetail
myRetail RESTful service is a REST API example application

This is a bare-bones example of a retail application providing a REST API for product management.
The application looks up for product name for a publicly available API and the price of the product is from a mocked API.

The entire application is contained within the jar file.

The minimal configuration required is a postgres DB in case one wants to add product to the DB.


Install
mvn clean install
Run the app
java -jar myRetail-1.0.0-SNAPSHOT

#Run the tests

REST API
The REST API to the myRetail app is described below.

Get a specific product
Request
GET /products/id
http://localhost:8080/products/1

Response
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Wed, 19 May 2021 09:49:15 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "id": 1,
  "name": "Small Iron Table",
  "currentPrice": {
    "value": "13.49",
    "currencyCode": "USD"
  }
}

Response code: 200; Time: 8763ms; Content length: 88 bytes

###

Create a new Product
Request
POST /products/

POST http://localhost:8080/products/
Content-Type: application/json

{"name":"Pulse Oximeter","price":40.0}

Response
HTTP/1.1 201 
Content-Length: 0
Date: Wed, 19 May 2021 09:59:14 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<Response body is empty>

Response code: 201; Time: 1047ms; Content length: 0 bytes

