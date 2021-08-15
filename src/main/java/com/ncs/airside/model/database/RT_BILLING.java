package com.ncs.airside.model.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class RT_BILLING {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long billingID;

    private String companyName;
    private String department;
    private String Address;
    private String contactPerson;
    private String contactNumber;
    private LocalDate dateOfApplication;
    private String vehicleID;
    private String transponderID;
    private String typeOfAvpLicenseID;
    private String natureOfWorkInAirside;
    private LocalDateTime timeStamp;
    private String row_record_status;

    public RT_BILLING() {
    }

    public RT_BILLING(Long billingID, String companyName, String department, String address, String contactPerson, String contactNumber, LocalDate dateOfApplication, String vehicleID, String transponderID, String typeOfAvpLicenseID, String natureOfWorkInAirside, LocalDateTime timeStamp, String row_record_status) {
        this.billingID = billingID;
        this.companyName = companyName;
        this.department = department;
        Address = address;
        this.contactPerson = contactPerson;
        this.contactNumber = contactNumber;
        this.dateOfApplication = dateOfApplication;
        this.vehicleID = vehicleID;
        this.transponderID = transponderID;
        this.typeOfAvpLicenseID = typeOfAvpLicenseID;
        this.natureOfWorkInAirside = natureOfWorkInAirside;
        this.timeStamp = timeStamp;
        this.row_record_status = row_record_status;
    }

    public Long getBillingID() {
        return billingID;
    }

    public void setBillingID(Long billingID) {
        this.billingID = billingID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDate getDateOfApplication() {
        return dateOfApplication;
    }

    public void setDateOfApplication(LocalDate dateOfApplication) {
        this.dateOfApplication = dateOfApplication;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getTransponderID() {
        return transponderID;
    }

    public void setTransponderID(String transponderID) {
        this.transponderID = transponderID;
    }

    public String getTypeOfAvpLicenseID() {
        return typeOfAvpLicenseID;
    }

    public void setTypeOfAvpLicenseID(String typeOfAvpLicenseID) {
        this.typeOfAvpLicenseID = typeOfAvpLicenseID;
    }

    public String getNatureOfWorkInAirside() {
        return natureOfWorkInAirside;
    }

    public void setNatureOfWorkInAirside(String natureOfWorkInAirside) {
        this.natureOfWorkInAirside = natureOfWorkInAirside;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRow_record_status() {
        return row_record_status;
    }

    public void setRow_record_status(String row_record_status) {
        this.row_record_status = row_record_status;
    }
}
