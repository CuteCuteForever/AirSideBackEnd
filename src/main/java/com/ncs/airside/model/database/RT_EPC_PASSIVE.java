package com.ncs.airside.model.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
public class RT_EPC_PASSIVE {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    Long epcPassiveRowId;

    String EPC;
    int antennaNumber;
    String rowRecordStatus;
    LocalDateTime timestamp;

    public RT_EPC_PASSIVE() {
    }

    public RT_EPC_PASSIVE(Long epcPassiveRowId, String EPC, int antennaNumber, String rowRecordStatus, LocalDateTime timestamp) {
        this.epcPassiveRowId = epcPassiveRowId;
        this.EPC = EPC;
        this.antennaNumber = antennaNumber;
        this.rowRecordStatus = rowRecordStatus;
        this.timestamp = timestamp;
    }

    public Long getEpcPassiveRowId() {
        return epcPassiveRowId;
    }

    public void setEpcPassiveRowId(Long epcPassiveRowId) {
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
}
