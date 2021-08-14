package com.ncs.airside.model.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RT_COMPANY {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long companyId;
    private String companyName;
    private String address;
    private String contactPersonName;
    private String contactPersonNumber;
    private String department;
    private LocalDateTime timestamp;
    private String rowRecordStatus;

    public RT_COMPANY() {
    }

    public RT_COMPANY(Long companyId, String companyName, String address, String contactPersonName, String contactPersonNumber, String department, LocalDateTime timestamp, String rowRecordStatus) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.address = address;
        this.contactPersonName = contactPersonName;
        this.contactPersonNumber = contactPersonNumber;
        this.department = department;
        this.timestamp = timestamp;
        this.rowRecordStatus = rowRecordStatus;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRowRecordStatus() {
        return rowRecordStatus;
    }

    public void setRowRecordStatus(String rowRecordStatus) {
        this.rowRecordStatus = rowRecordStatus;
    }
}
