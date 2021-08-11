package com.ncs.airside.model.view;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "V_VEHICLE_COMPANY")
@Subselect("select * from V_VEHICLE_COMPANY")
public class V_VEHICLE_COMPANY {

    @Id
    private Long companyID;

    private String company_name;
    private String company_address;
    private String contact_person_name;
    private String contact_person_number;
    private String department;
    private String vehicle_registration_number;

    public V_VEHICLE_COMPANY() {
    }

    public V_VEHICLE_COMPANY(Long companyID, String company_name, String company_address, String contact_person_name, String contact_person_number, String department, String vehicle_registration_number) {
        this.companyID = companyID;
        this.company_name = company_name;
        this.company_address = company_address;
        this.contact_person_name = contact_person_name;
        this.contact_person_number = contact_person_number;
        this.department = department;
        this.vehicle_registration_number = vehicle_registration_number;
    }

    public Long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Long companyID) {
        this.companyID = companyID;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getContact_person_name() {
        return contact_person_name;
    }

    public void setContact_person_name(String contact_person_name) {
        this.contact_person_name = contact_person_name;
    }

    public String getContact_person_number() {
        return contact_person_number;
    }

    public void setContact_person_number(String contact_person_number) {
        this.contact_person_number = contact_person_number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getVehicle_registration_number() {
        return vehicle_registration_number;
    }

    public void setVehicle_registration_number(String vehicle_registration_number) {
        this.vehicle_registration_number = vehicle_registration_number;
    }
}
