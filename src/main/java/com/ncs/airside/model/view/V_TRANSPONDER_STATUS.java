package com.ncs.airside.model.view;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "v_transponder_status")
@Subselect("select * from v_transponder_status")
public class V_TRANSPONDER_STATUS {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
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

    private String registrationNumber;

    private String companyName;
    private String address;
    private String contactPersonName;
    private String contactPersonNumber;
    private String department;

    private String callSign;
    private String serialNumber;
    private String serviceAvailability;
    private String description;
    private LocalDate warrantyFromDate;
    private LocalDate warrantyToDate;

    private String duration;

    public V_TRANSPONDER_STATUS() {
    }

    public V_TRANSPONDER_STATUS(Long transponderStatusId, String EPC, Long companyId, LocalDateTime outTimestamp, LocalDateTime inTimestamp, String rentalDuration, Long transponderId, String transponderStatus, Long vehicleId, String rowRecordStatus, LocalDateTime timestamp, String registrationNumber, String companyName, String address, String contactPersonName, String contactPersonNumber, String department, String callSign, String serialNumber, String serviceAvailability, String description, LocalDate warrantyFromDate, LocalDate warrantyToDate, String duration) {
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
        this.registrationNumber = registrationNumber;
        this.companyName = companyName;
        this.address = address;
        this.contactPersonName = contactPersonName;
        this.contactPersonNumber = contactPersonNumber;
        this.department = department;
        this.callSign = callSign;
        this.serialNumber = serialNumber;
        this.serviceAvailability = serviceAvailability;
        this.description = description;
        this.warrantyFromDate = warrantyFromDate;
        this.warrantyToDate = warrantyToDate;
        this.duration = duration;
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

    public String getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(String rentalDuration) {
        this.rentalDuration = rentalDuration;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getServiceAvailability() {
        return serviceAvailability;
    }

    public void setServiceAvailability(String serviceAvailability) {
        this.serviceAvailability = serviceAvailability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
