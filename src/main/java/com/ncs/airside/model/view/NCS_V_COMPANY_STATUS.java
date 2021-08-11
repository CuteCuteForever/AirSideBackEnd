package com.ncs.airside.model.view;


import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "NCS_V_COMPANY_STATUS")
@Subselect("select * from NCS_V_COMPANY_STATUS")
public class NCS_V_COMPANY_STATUS {

    @Id
    private int customerID;

    private String cust_address;
    private String cust_company_name;
    private String cust_contact_number;
    private String cust_contact_person;
    private String cust_department;
    private String bill_address;
    private String bill_department;
    private String bill_contact_person;
    private String date_of_application;
    private String vehicle_registration_no;
    private String type_of_avp_license;
    private String nature_of_work_in_airside;

    public NCS_V_COMPANY_STATUS() {
    }

    public NCS_V_COMPANY_STATUS(int customerID, String cust_address, String cust_company_name, String cust_contact_number, String cust_contact_person, String cust_department, String bill_address, String bill_department, String bill_contact_person, String date_of_application, String vehicle_registration_no, String type_of_avp_license, String nature_of_work_in_airside) {
        this.customerID = customerID;
        this.cust_address = cust_address;
        this.cust_company_name = cust_company_name;
        this.cust_contact_number = cust_contact_number;
        this.cust_contact_person = cust_contact_person;
        this.cust_department = cust_department;
        this.bill_address = bill_address;
        this.bill_department = bill_department;
        this.bill_contact_person = bill_contact_person;
        this.date_of_application = date_of_application;
        this.vehicle_registration_no = vehicle_registration_no;
        this.type_of_avp_license = type_of_avp_license;
        this.nature_of_work_in_airside = nature_of_work_in_airside;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    public String getCust_company_name() {
        return cust_company_name;
    }

    public void setCust_company_name(String cust_company_name) {
        this.cust_company_name = cust_company_name;
    }

    public String getCust_contact_number() {
        return cust_contact_number;
    }

    public void setCust_contact_number(String cust_contact_number) {
        this.cust_contact_number = cust_contact_number;
    }

    public String getCust_contact_person() {
        return cust_contact_person;
    }

    public void setCust_contact_person(String cust_contact_person) {
        this.cust_contact_person = cust_contact_person;
    }

    public String getCust_department() {
        return cust_department;
    }

    public void setCust_department(String cust_department) {
        this.cust_department = cust_department;
    }

    public String getBill_address() {
        return bill_address;
    }

    public void setBill_address(String bill_address) {
        this.bill_address = bill_address;
    }

    public String getBill_department() {
        return bill_department;
    }

    public void setBill_department(String bill_department) {
        this.bill_department = bill_department;
    }

    public String getBill_contact_person() {
        return bill_contact_person;
    }

    public void setBill_contact_person(String bill_contact_person) {
        this.bill_contact_person = bill_contact_person;
    }

    public String getDate_of_application() {
        return date_of_application;
    }

    public void setDate_of_application(String date_of_application) {
        this.date_of_application = date_of_application;
    }

    public String getVehicle_registration_no() {
        return vehicle_registration_no;
    }

    public void setVehicle_registration_no(String vehicle_registration_no) {
        this.vehicle_registration_no = vehicle_registration_no;
    }

    public String getType_of_avp_license() {
        return type_of_avp_license;
    }

    public void setType_of_avp_license(String type_of_avp_license) {
        this.type_of_avp_license = type_of_avp_license;
    }

    public String getNature_of_work_in_airside() {
        return nature_of_work_in_airside;
    }

    public void setNature_of_work_in_airside(String nature_of_work_in_airside) {
        this.nature_of_work_in_airside = nature_of_work_in_airside;
    }
}
