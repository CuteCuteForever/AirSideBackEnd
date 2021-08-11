swaggerUI: To store spring boot documentation 
http://localhost:8080/swagger-ui.html

actuator: To check health status metrics of your spring boot application 
http://localhost:8080/actuator

netstat -a -n -o | find "8080
tasklist | findstr <PID>
taskkill /F /PID 6060

== Antenna Test ==
http://localhost:8080/AntennaInit/3
http://localhost:8080/AntennaStartScan
http://localhost:8080/receiveAntennaResult

== Antenna Test ==
http://localhost:8080/rfidopen
http://localhost:8080/rfidsetrfpower/30
http://localhost:8080/rfidstartsensor
http://localhost:8080/rfidgettag
http://localhost:8080/rfidclosesensor

== RFID Multiple Test ==
http://localhost:8080/rfidopen
http://localhost:8080/AsyncRfidScanTagMultiple/{numberOfTimes}
http://localhost:8080/receiveRFIDMultipleResult


How to create fresh start DB
======================================
CREATE DATABASE airsideDB;
start springboot to create the tables first.
INSERT INTO airsidedb.rt_role(id,name) VALUES(1,'ROLE_USER');
INSERT INTO airsidedb.rt_role(id,name) VALUES(2,'ROLE_ADMIN');
Go to front end and create a user and login.

How to start testing
===============================
1) Ensure that the following tables are empty by executing following SQL :
- truncate table airsidedb.rt_transponder_BORROW;
- truncate table airsidedb.rt_epc_alert;
2) Check from front end the filename - "mock-test.service.ts" that the comport for the antenna


CREATE TABLE airsidedb.rt_transponder_borrow (  transponder_borrow_id bigint NOT NULL,  epc varchar(255) DEFAULT NULL,    companyid bigint DEFAULT NULL,  borrow_timestamp datetime(6) DEFAULT NULL,  return_timestamp datetime(6) DEFAULT NULL,   transponder_id bigint DEFAULT NULL,  vehicle_id bigint DEFAULT NULL,  row_record_status varchar(255) DEFAULT NULL,  timestamp datetime(6) DEFAULT NULL,  PRIMARY KEY (transponder_borrow_id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

