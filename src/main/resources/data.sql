CREATE DATABASE airsideDB;


CREATE TABLE airsidedb.rt_role ( id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

CREATE TABLE airsidedb.rt_user (  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) DEFAULT NULL,    password VARCHAR(255) DEFAULT NULL,    email VARCHAR(255) DEFAULT NULL,    UNIQUE KEY UKhxbpt45qr9a3v5ri44bn3hq1i (username),    UNIQUE KEY UKjxfs9iaul7pna13uix06undw0 (email))  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

CREATE TABLE airsidedb.rt_user_role ( user_id bigint NOT NULL,  role_id int NOT NULL, PRIMARY KEY (user_id,role_id),  KEY FK1iojqvlk4tuvissmwoudpfutr (role_id),  CONSTRAINT FK1iojqvlk4tuvissmwoudpfutr FOREIGN KEY (role_id) REFERENCES rt_role (id),  CONSTRAINT FK9bo10079c9wqncrifnw0mygo9 FOREIGN KEY (user_id) REFERENCES rt_user (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE airsidedb.rt_vehicle (	vehicle_row_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,    vehicle_id INT NOT NULL,    company_id BIGINT DEFAULT NULL,    registration_number VARCHAR(255) DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

CREATE TABLE airsidedb.rt_company (    company_row_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,    company_id BIGINT NOT NULL,    company_name VARCHAR(255) DEFAULT NULL,    address VARCHAR(255) DEFAULT NULL,    contact_person_name VARCHAR(255) DEFAULT NULL,    contact_person_number VARCHAR(255) DEFAULT NULL,    department VARCHAR(255) DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

CREATE TABLE airsidedb.rt_transponder_status (transponder_status_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,    epc VARCHAR(255) DEFAULT NULL,    company_id BIGINT DEFAULT NULL,    out_timestamp DATETIME(6) DEFAULT NULL,    in_timestamp DATETIME(6) DEFAULT NULL,	rental_duration VARCHAR(255) DEFAULT NULL,    transponder_id BIGINT DEFAULT NULL,    transponder_status VARCHAR(255) DEFAULT NULL,    vehicle_id BIGINT DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

CREATE TABLE airsidedb.rt_transponder ( transponder_row_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,    transponder_id BIGINT DEFAULT NULL,    call_sign VARCHAR(255) DEFAULT NULL,    serial_number VARCHAR(255) DEFAULT NULL,    service_availability VARCHAR(255) DEFAULT NULL,    description VARCHAR(255) DEFAULT NULL,    warranty_from_date DATETIME(6) DEFAULT NULL,    warranty_to_date DATETIME(6) DEFAULT NULL,    epc VARCHAR(255) DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

CREATE TABLE airsidedb.rt_epc_passive ( epc_passive_row_id BIGINT NOT NULL,    epc VARCHAR(255) DEFAULT NULL,    antenna_number INT DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL,    PRIMARY KEY (epc_passive_row_id))  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

CREATE TABLE airsidedb.rt_epc_active ( epc_active_row_id BIGINT NOT NULL,    epc VARCHAR(255) DEFAULT NULL,    antenna_number INT DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL,    PRIMARY KEY (epc_active_row_id))  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;

CREATE TABLE airsidedb.hibernate_sequence (  next_val bigint NOT NULL,  PRIMARY KEY (next_val)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO airsidedb.rt_role(id,name) VALUES(1,'ROLE_USER');
INSERT INTO airsidedb.rt_role(id,name) VALUES(2,'ROLE_ADMIN');

INSERT INTO airsidedb.hibernate_sequence(next_val) VALUES(1);


CREATE OR REPLACE VIEW airsidedb.v_vehicle_company AS
    SELECT
        a.vehicle_row_id,
        b.company_id,
        a.vehicle_id,
        b.company_name AS company_name,
        b.address AS company_address,
        b.contact_person_name AS contact_person_name,
        b.contact_person_number AS contact_person_number,
        b.department,
        a.registration_number AS vehicle_registration_number
    FROM
        airsidedb.rt_company b
            LEFT JOIN
        airsidedb.rt_vehicle a ON a.company_id = b.company_id
    WHERE
        a.row_record_status = 'valid'
            AND b.row_record_status = 'valid';




create or replace view airsidedb.v_transponder_status as
Select *  from
(
WITH difference_in_seconds AS (
  SELECT
    aa.*,
    TIMESTAMPDIFF(SECOND, out_timestamp ,in_timestamp) AS seconds
  FROM
  (
  select a.* ,
  CASE
     WHEN rental_duration = 'Weekly' and DATE_ADD(DATE(out_timestamp), INTERVAL 5 DAY) <= curdate()  THEN 'Due Soon'
     WHEN rental_duration = 'Monthly' and DATE_SUB(DATE_ADD(DATE(out_timestamp), INTERVAL 1 MONTH) , INTERVAL 1 WEEK) <= curdate()  THEN 'Due Soon'
     WHEN rental_duration = 'Yearly' and DATE_SUB(DATE_ADD(DATE(out_timestamp), INTERVAL 1 YEAR) , INTERVAL 1 WEEK) <= curdate()  THEN 'Due Soon'
     WHEN rental_duration = 'Weekly' and DATE_ADD(DATE(out_timestamp), INTERVAL 1 WEEK) <= curdate()  THEN 'Overdue'
     WHEN rental_duration = 'Monthly'and DATE_ADD(DATE(out_timestamp), INTERVAL 1 MONTH) <= curdate()  THEN 'Overdue'
     WHEN rental_duration = 'Yearly' and DATE_ADD(DATE(out_timestamp), INTERVAL 1 YEAR) <= curdate()  THEN 'Overdue'
     ELSE ""
END AS due_notice  ,
b.registration_number,
c.company_name, c.address , c.contact_person_name, c.contact_person_number, c.department,
d.call_sign, d.serial_number, d.service_availability, d.description, d.warranty_from_date, d.warranty_to_date
from airsidedb.rt_transponder_status a
inner join airsidedb.rt_vehicle b on a.vehicle_id = b.vehicle_id and b.row_record_status = "valid"
inner join airsidedb.rt_company c on a.company_id = c.company_id and c.row_record_status = "valid"
inner join airsidedb.rt_transponder d on a.transponder_id = d.transponder_id and d.row_record_status = "valid"
  ) as aa
),
differences AS (
  SELECT
    *,
    MOD(seconds, 60) AS seconds_part,
    MOD(seconds, 3600) AS minutes_part,
    MOD(seconds, 3600 * 24) AS hours_part
  FROM difference_in_seconds
)
SELECT
  *,
  CONCAT(
    FLOOR(seconds / 3600 / 24), ' days ',
    FLOOR(hours_part / 3600), ' hours ',
    FLOOR(minutes_part / 60), ' minutes ',
    seconds_part, ' seconds'
  ) AS duration
FROM differences
) as resultTable;





CREATE OR REPLACE VIEW airsidedb.v_epc_active AS
    SELECT
        a.epc_active_row_id,
        a.epc,
        a.antenna_number,
        b.transponder_id,
        b.call_sign,
        b.serial_number,
        b.service_availability,
        b.description,
        b.warranty_from_date,
        b.warranty_to_date
    FROM
        airsidedb.rt_epc_active a
            LEFT JOIN
        airsidedb.rt_transponder b ON a.epc = b.epc
    WHERE
        a.row_record_status = 'valid'
            AND b.row_record_status = 'valid';



CREATE OR REPLACE VIEW airsidedb.v_epc_passive AS
    SELECT
        a.epc_passive_row_id,
        a.epc,
        a.antenna_number,
        b.transponder_id,
        b.call_sign,
        b.serial_number,
        b.service_availability,
        b.description,
        b.warranty_from_date,
        b.warranty_to_date
    FROM
        airsidedb.rt_epc_passive a
            LEFT JOIN
        airsidedb.rt_transponder b ON a.epc = b.epc
    WHERE
        a.row_record_status = 'valid'
            AND b.row_record_status = 'valid';