package com.ncs.airside.model.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class RT_TRANSPONDER {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long transponderRowId;

    private Long transponderId;
    private String callSign;
    private String serialNumber;
    private String serviceAvailability;
    private String description;
    private LocalDate warrantyFromDate;
    private LocalDate warrantyToDate;
    private String EPC ;
    private String rowRecordStatus;
    private LocalDateTime timestamp ;

    public RT_TRANSPONDER() {
    }

    public RT_TRANSPONDER(Long transponderRowId, Long transponderId, String callSign, String serialNumber, String serviceAvailability, String description, LocalDate warrantyFromDate, LocalDate warrantyToDate, String EPC, String rowRecordStatus, LocalDateTime timestamp) {
        this.transponderRowId = transponderRowId;
        this.transponderId = transponderId;
        this.callSign = callSign;
        this.serialNumber = serialNumber;
        this.serviceAvailability = serviceAvailability;
        this.description = description;
        this.warrantyFromDate = warrantyFromDate;
        this.warrantyToDate = warrantyToDate;
        this.EPC = EPC;
        this.rowRecordStatus = rowRecordStatus;
        this.timestamp = timestamp;
    }

    public Long getTransponderRowId() {
        return transponderRowId;
    }

    public void setTransponderRowId(Long transponderRowId) {
        this.transponderRowId = transponderRowId;
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

    @Override
    public String toString() {
        return "RT_TRANSPONDER{" +
                "transponderRowId=" + transponderRowId +
                ", transponderId=" + transponderId +
                ", callSign='" + callSign + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", serviceAvailability='" + serviceAvailability + '\'' +
                ", description='" + description + '\'' +
                ", warrantyFromDate=" + warrantyFromDate +
                ", warrantyToDate=" + warrantyToDate +
                ", EPC='" + EPC + '\'' +
                ", rowRecordStatus='" + rowRecordStatus + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
