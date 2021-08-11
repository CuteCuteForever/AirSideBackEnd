package com.ncs.airside.model.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RT_EPC_ALERT {

    @Id
    @GeneratedValue
    private Long epcAlertId;

    private String epc;
    private LocalDateTime timestamp;
    private String rowRecordStatus;

    public RT_EPC_ALERT() {
    }

    public RT_EPC_ALERT(Long epcAlertId, String epc, LocalDateTime timestamp, String rowRecordStatus) {
        this.epcAlertId = epcAlertId;
        this.epc = epc;
        this.timestamp = timestamp;
        this.rowRecordStatus = rowRecordStatus;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public Long getEpcAlertId() {
        return epcAlertId;
    }

    public void setEpcAlertId(Long epcAlertId) {
        this.epcAlertId = epcAlertId;
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
