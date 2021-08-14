package com.ncs.airside.model.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RT_TRANSPONDER_STATUS {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long transponderStatusId;

    private String EPC;
    private Long companyId;
    private LocalDateTime outTimestamp;
    private LocalDateTime inTimestamp;
    private String rentalDuration;
    private Long transponderId;
    private String transponderStatus;
    private Long vehicleId;
    private String rowRecordStatus;
    private LocalDateTime timestamp;

    public RT_TRANSPONDER_STATUS() {
    }

    public RT_TRANSPONDER_STATUS(Long transponderStatusId, String EPC, Long companyId, LocalDateTime outTimestamp, LocalDateTime inTimestamp, String rentalDuration, Long transponderId, String transponderStatus, Long vehicleId, String rowRecordStatus, LocalDateTime timestamp) {
        this.transponderStatusId = transponderStatusId;
        this.EPC = EPC;
        this.companyId = companyId;
        this.outTimestamp = outTimestamp;
        this.inTimestamp = inTimestamp;
        this.rentalDuration = rentalDuration;
        this.transponderId = transponderId;
        this.transponderStatus = transponderStatus;
        this.vehicleId = vehicleId;
        this.rowRecordStatus = rowRecordStatus;
        this.timestamp = timestamp;
    }

    public Long getTransponderStatusId() {
        return transponderStatusId;
    }

    public void setTransponderStatusId(Long transponderStatusId) {
        this.transponderStatusId = transponderStatusId;
    }

    public String getEPC() {
        return EPC;
    }

    public void setEPC(String EPC) {
        this.EPC = EPC;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public LocalDateTime getOutTimestamp() {
        return outTimestamp;
    }

    public void setOutTimestamp(LocalDateTime outTimestamp) {
        this.outTimestamp = outTimestamp;
    }

    public LocalDateTime getInTimestamp() {
        return inTimestamp;
    }

    public void setInTimestamp(LocalDateTime inTimestamp) {
        this.inTimestamp = inTimestamp;
    }

    public Long getTransponderId() {
        return transponderId;
    }

    public void setTransponderId(Long transponderId) {
        this.transponderId = transponderId;
    }

    public String getTransponderStatus() {
        return transponderStatus;
    }

    public void setTransponderStatus(String transponderStatus) {
        this.transponderStatus = transponderStatus;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRowRecordStatus() {
        return rowRecordStatus;
    }

    public void setRowRecordStatus(String rowRecordStatus) {
        this.rowRecordStatus = rowRecordStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(String rentalDuration) {
        this.rentalDuration = rentalDuration;
    }
}
