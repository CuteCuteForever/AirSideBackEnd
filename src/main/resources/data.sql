CREATE DATABASE airsideDB;

--AUTO_INCREMENT PRIMARY KEY
CREATE TABLE airsidedb.rt_role ( id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
CREATE TABLE airsidedb.rt_user (  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) DEFAULT NULL,    password VARCHAR(255) DEFAULT NULL,    email VARCHAR(255) DEFAULT NULL,    UNIQUE KEY UKhxbpt45qr9a3v5ri44bn3hq1i (username),    UNIQUE KEY UKjxfs9iaul7pna13uix06undw0 (email))  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
CREATE TABLE airsidedb.rt_user_role ( user_id bigint NOT NULL,  role_id int NOT NULL, PRIMARY KEY (user_id,role_id),  KEY FK1iojqvlk4tuvissmwoudpfutr (role_id),  CONSTRAINT FK1iojqvlk4tuvissmwoudpfutr FOREIGN KEY (role_id) REFERENCES rt_role (id),  CONSTRAINT FK9bo10079c9wqncrifnw0mygo9 FOREIGN KEY (user_id) REFERENCES rt_user (id)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE airsidedb.rt_vehicle (vehicle_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, company_id BIGINT DEFAULT NULL,    registration_number VARCHAR(255) DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
CREATE TABLE airsidedb.rt_company ( company_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, company_name VARCHAR(255) DEFAULT NULL,    address VARCHAR(255) DEFAULT NULL,    contact_person_name VARCHAR(255) DEFAULT NULL,    contact_person_number VARCHAR(255) DEFAULT NULL,    department VARCHAR(255) DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
CREATE TABLE airsidedb.rt_transponder_status (    transponder_status_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,    epc VARCHAR(255) DEFAULT NULL,    company_id BIGINT DEFAULT NULL,    out_timestamp DATETIME(6) DEFAULT NULL,    in_timestamp DATETIME(6) DEFAULT NULL,	rental_duration VARCHAR(255) DEFAULT NULL,    transponder_id BIGINT DEFAULT NULL,    transponder_status VARCHAR(255) DEFAULT NULL,    vehicle_id BIGINT DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;
CREATE TABLE airsidedb.rt_transponder (  transponder_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,    call_sign VARCHAR(255) DEFAULT NULL,    serial_number VARCHAR(255) DEFAULT NULL,    service_availability VARCHAR(255) DEFAULT NULL,    description VARCHAR(255) DEFAULT NULL,    warranty_from_date DATETIME(6) DEFAULT NULL,    warranty_to_date DATETIME(6) DEFAULT NULL,    epc VARCHAR(255) DEFAULT NULL,    row_record_status VARCHAR(255) DEFAULT NULL,    timestamp DATETIME(6) DEFAULT NULL)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COLLATE = UTF8MB4_0900_AI_CI;


-- Insert roles
INSERT INTO airsidedb.rt_role(id,name) VALUES(1,'ROLE_USER');
INSERT INTO airsidedb.rt_role(id,name) VALUES(2,'ROLE_ADMIN');


create or replace view airsidedb.v_vehicle_company as
select
b.companyid,
b.company_name as company_name,
b.address as company_address,
b.contact_person_name as contact_person_name,
b.contact_person_number as contact_person_number,
b.department,
a.registration_number as vehicle_registration_number
from airsidedb.rt_vehicle a inner join airsidedb.rt_company b on a.companyid = b.companyid
where a.row_record_status = 'valid'
and b.row_record_status = 'valid';


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
b.registration_number,
c.company_name, c.address , c.contact_person_name, c.contact_person_number, c.department,
d.call_sign, d.serial_number, d.service_availability, d.description, d.warranty_from_date, d.warranty_to_date
from airsidedb.rt_transponder_status a
left join airsidedb.rt_vehicle b on a.vehicle_id = b.vehicle_id and b.row_record_status = "valid"
left join airsidedb.rt_company c on a.company_id = c.company_id and c.row_record_status = "valid"
left join airsidedb.rt_transponder d on a.transponder_id = d.transponder_id and d.row_record_status = "valid"
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
) as a

