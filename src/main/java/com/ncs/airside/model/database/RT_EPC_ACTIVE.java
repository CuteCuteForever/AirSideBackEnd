package com.ncs.airside.model.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RT_EPC_ACTIVE {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    Long epcActiveRowId;

    String EPC;
    int antennaNumber;
    String rowRecordStatus;
    LocalDateTime timestamp;

    public RT_EPC_ACTIVE() {
    }

    public RT_EPC_ACTIVE(Long epcActiveRowId, String EPC, int antennaNumber, String rowRecordStatus, LocalDateTime timestamp) {
        this.epcActiveRowId = epcActiveRowId;
        this.EPC = EPC;
        this.antennaNumber = antennaNumber;
        this.rowRecordStatus = rowRecordStatus;
        this.timestamp = timestamp;
    }

    public Long getEpcActiveRowId() {
        return epcActiveRowId;
    }

    public void setEpcActiveRowId(Long epcActiveRowId) {
        this.epcActiveRowId = epcActiveRowId;
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
