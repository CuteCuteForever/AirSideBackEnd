package com.ncs.airside.model.database;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class RT_VEHICLE {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int vehicleId;

    private Long companyId;
    private String registrationNumber;
    private String rowRecordStatus;
    private LocalDateTime timestamp;


    public RT_VEHICLE() {
    }

    public RT_VEHICLE(int vehicleId, Long companyId, String registrationNumber, String rowRecordStatus, LocalDateTime timestamp) {
        this.vehicleId = vehicleId;
        this.companyId = companyId;
        this.registrationNumber = registrationNumber;
        this.rowRecordStatus = rowRecordStatus;
        this.timestamp = timestamp;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
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

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
