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
    private Long vehicleRowId;

    private Long companyId;
    private Long vehicleId;
    private String companyName;
    private String companyAddress;
    private String contactPersonName;
    private String contactPersonNumber;
    private String department;
    private String vehicleRegistrationNumber;

    public V_VEHICLE_COMPANY() {
    }

    public V_VEHICLE_COMPANY(Long vehicleRowId, Long companyId, Long vehicleId, String companyName, String companyAddress, String contactPersonName, String contactPersonNumber, String department, String vehicleRegistrationNumber) {
        this.vehicleRowId = vehicleRowId;
        this.companyId = companyId;
        this.vehicleId = vehicleId;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.contactPersonName = contactPersonName;
        this.contactPersonNumber = contactPersonNumber;
        this.department = department;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public Long getVehicleRowId() {
        return vehicleRowId;
    }

    public void setVehicleRowId(Long vehicleRowId) {
        this.vehicleRowId = vehicleRowId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonNumber() {
        return contactPersonNumber;
    }

    public void setContactPersonNumber(String contactPersonNumber) {
        this.contactPersonNumber = contactPersonNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }
}
