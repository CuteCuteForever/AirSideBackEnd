package com.ncs.airside.model.view;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "v_epc_passive")
@Subselect("select * from v_epc_passive")
public class V_PASSIVE_STATUS {

    @Id
    private String epcPassiveRowId;

    private String EPC;
    private int antennaNumber;
    private Long transponderId;
    private String callSign;
    private String serialNumber;
    private String serviceAvailability;
    private String description;
    private String warrantyFromDate;
    private String warrantyToDate;

    public V_PASSIVE_STATUS() {
    }

    public V_PASSIVE_STATUS(String epcPassiveRowId, String EPC, int antennaNumber, Long transponderId, String callSign, String serialNumber, String serviceAvailability, String description, String warrantyFromDate, String warrantyToDate) {
        this.epcPassiveRowId = epcPassiveRowId;
        this.EPC = EPC;
        this.antennaNumber = antennaNumber;
        this.transponderId = transponderId;
        this.callSign = callSign;
        this.serialNumber = serialNumber;
        this.serviceAvailability = serviceAvailability;
        this.description = description;
        this.warrantyFromDate = warrantyFromDate;
        this.warrantyToDate = warrantyToDate;
    }

    public String getEpcPassiveRowId() {
        return epcPassiveRowId;
    }

    public void setEpcPassiveRowId(String epcPassiveRowId) {
        this.epcPassiveRowId = epcPassiveRowId;
    }

    public String getEPC() {
        return EPC;
    }

    public void setEPC(String EPC) {
        this.EPC = EPC;
    }

    public int getAntennaNumber() {
        return antennaNumber;
    }

    public void setAntennaNumber(int antennaNumber) {
        this.antennaNumber = antennaNumber;
    }

    public Long getTransponderId() {
        return transponderId;
    }

    public void setTransponderId(Long transponderId) {
        this.transponderId = transponderId;
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

    public String getWarrantyFromDate() {
        return warrantyFromDate;
    }

    public void setWarrantyFromDate(String warrantyFromDate) {
        this.warrantyFromDate = warrantyFromDate;
    }

    public String getWarrantyToDate() {
        return warrantyToDate;
    }

    public void setWarrantyToDate(String warrantyToDate) {
        this.warrantyToDate = warrantyToDate;
    }
}

