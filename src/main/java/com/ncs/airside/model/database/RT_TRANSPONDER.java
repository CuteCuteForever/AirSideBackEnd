package com.ncs.airside.model.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class RT_TRANSPONDER {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long transponderId;

    private String serialNumber;
    private String callSign;
    private String description;
    private LocalDate warrantyFromDate;
    private LocalDate warrantyToDate;
    private String EPC ;
    private String serviceAvailability;
    private LocalDateTime timestamp ;
    private String rowRecordStatus;

    public RT_TRANSPONDER() {
    }

    public RT_TRANSPONDER(Long transponderId, String serialNumber, String callSign, String description, LocalDate warrantyFromDate, LocalDate warrantyToDate, String EPC, String serviceAvailability, LocalDateTime timestamp, String rowRecordStatus) {
        this.transponderId = transponderId;
        this.serialNumber = serialNumber;
        this.callSign = callSign;
        this.description = description;
        this.warrantyFromDate = warrantyFromDate;
        this.warrantyToDate = warrantyToDate;
        this.EPC = EPC;
        this.serviceAvailability = serviceAvailability;
        this.timestamp = timestamp;
        this.rowRecordStatus = rowRecordStatus;
    }

    public LocalDate getWarrantyFromDate() {
        return warrantyFromDate;
    }

    public void setWarrantyFromDate(LocalDate warrantyFromDate) {
        this.warrantyFromDate = warrantyFromDate;
    }

    public LocalDate getWarrantyToDate() {
        return warrantyToDate;
    }

    public void setWarrantyToDate(LocalDate warrantyToDate) {
        this.warrantyToDate = warrantyToDate;
    }

    public Long getTransponderId() {
        return transponderId;
    }

    public void setTransponderId(Long transponderId) {
        this.transponderId = transponderId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getDescription() {
        return description;
    }

    public String getEPC() {
        return EPC;
    }

    public void setEPC(String EPC) {
        this.EPC = EPC;
    }

    public String getServiceAvailability() {
        return serviceAvailability;
    }

    public void setServiceAvailability(String serviceAvailability) {
        this.serviceAvailability = serviceAvailability;
    }

    public void setDescription(String description) {
        this.description = description;
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
