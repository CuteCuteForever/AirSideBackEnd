package com.ncs.airside.model.database;

import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class RT_TRANSPONDER_BORROW {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long transponderBorrowerID;

    private Long transponderID;
    private String EPC;
    private Long vehicleID;
    private Long companyID;
    private LocalDateTime borrowTimestamp;
    private LocalDateTime returnTimestamp;
    private LocalDateTime timestamp;
    private String rowRecordStatus;

    public RT_TRANSPONDER_BORROW() {
    }

    public RT_TRANSPONDER_BORROW(Long transponderBorrowerID, Long transponderID, String EPC, Long vehicleID, Long companyID, LocalDateTime borrowTimestamp, LocalDateTime returnTimestamp, LocalDateTime timestamp, String rowRecordStatus) {
        this.transponderBorrowerID = transponderBorrowerID;
        this.transponderID = transponderID;
        this.EPC = EPC;
        this.vehicleID = vehicleID;
        this.companyID = companyID;
        this.borrowTimestamp = borrowTimestamp;
        this.returnTimestamp = returnTimestamp;
        this.timestamp = timestamp;
        this.rowRecordStatus = rowRecordStatus;
    }

    public String getEPC() {
        return EPC;
    }

    public void setEPC(String EPC) {
        this.EPC = EPC;
    }

    public Long getTransponderBorrowerID() {
        return transponderBorrowerID;
    }

    public void setTransponderBorrowerID(Long transponderBorrowerID) {
        this.transponderBorrowerID = transponderBorrowerID;
    }

    public LocalDateTime getBorrowTimestamp() {
        return borrowTimestamp;
    }

    public void setBorrowTimestamp(LocalDateTime borrowTimestamp) {
        this.borrowTimestamp = borrowTimestamp;
    }

    public LocalDateTime getReturnTimestamp() {
        return returnTimestamp;
    }

    public void setReturnTimestamp(LocalDateTime returnTimestamp) {
        this.returnTimestamp = returnTimestamp;
    }

    public Long getTransponderID() {
        return transponderID;
    }

    public void setTransponderID(Long transponderID) {
        this.transponderID = transponderID;
    }

    public Long getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Long vehicleID) {
        this.vehicleID = vehicleID;
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

    public Long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Long companyID) {
        this.companyID = companyID;
    }
}
