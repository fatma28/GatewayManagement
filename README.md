# Gateway Management System

## Description

The Gateway Management System allows master devices to control multiple peripheral devices.

### Requirements

A REST service provides the below operations:

- An operation to store information about gateways and their associated devices.
- An operation to display the information of all stored gateways and their devices.
- An operation to display details for a single gateway.
- An operation to add Device to gateway.
- An operation to remove device from gateway.

### Constraint

- Each gateway must have a value in its "IPV4" property and if the value is invalid, an Error returned with "Invalid
  IPV4!" message and HTTPStatus "Bad Request".
- Each gateway can no more than 10 peripheral devices and if it has 10 peripheral devices, we only add the gateway to db and return "Gateway name is added successfully!"

### In-memory Database - H2

This information must be stored in the database. Our database is H2 (in-memory DB).
The Database has "GATEWAY" and "DEVICE" tables.

The below is the used Queries to create tables:

```
create table GATEWAY
(
   UID VARCHAR(250) not null primary key,
   NAME VARCHAR(250) not null,
   ADDRESS VARCHAR(250) not null
);
```

```
create table DEVICE
   (
      UID INT not null primary key,
      VENDOR VARCHAR(250) not null,
      DATE_CREATED DATE not null,
      STATUS VARCHAR(10) not null,
      GATEWAY_ID VARCHAR(250) not null
);
```
#### Operations
- "/gateway/add", POST API 
- "/gateway/get/all", GET API
- "/gateway/get/{id}", GET API
- "/device/add", POST API
- "/device/delete/{id}", GET API 

#### Curls

1- Add Gateway

Adding gateway which has two peripheral devices:

```
curl --request POST \
--url http://localhost:8080/gateway/add \
--header 'Content-Type: application/json' \
--data '{
"serial_number" : "MustafaUID",
"name" : "MustafaFathy",
"ipv4Address" : "0.0.0.0"
}'
```

Adding gateway with no peripheral device:

```
curl --request POST \
--url http://localhost:8080/gateway/add \
--header 'Content-Type: application/json' \
--data '{
"serial_number" : "1234",
"name" : "First gateway",
"ipv4Address" : "0.0.0.0",
"peripharelDeviceList":[
{
"uid":4321,
"status":"Offline",
"vendor":"2",
"date_created":"2020-10-19"
}, {
"uid":90,
"status":"Online" }
]
}' 
```

2- GET All Gateways and Devices

```
curl --request GET \
  --url http://localhost:8080/gateway/get/all
```
3- Get Single Gateway details
```
curl --request GET \
  --url http://localhost:8080/gateway/get/{serial-numer} \
  --header 'Content-Type: multipart/form-data; boundary=---011000010111000001101001'
```
4- Add Device
```
curl --request POST \
  --url http://localhost:8080/device/add \
  --header 'Content-Type: application/json' \
  --data '{
	"uid":222,
	"vendor" : "vendorName",
	"date_created" : "2021-11-02",
	"status" : 1,
	"gateway_id":"gatewayIdRef"
}'
```
5- Delete Device 
```
curl --request GET \
  --url http://localhost:8080/device/delete/8
```