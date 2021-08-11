package com.ncs.airside.model.view;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Immutable
@Table(name = "V_BORROW_RETURN_STATUS")
@Subselect("select * from V_BORROW_RETURN_STATUS")
public class V_BORROW_RETURN_STATUS {

    @Id
    private Long id;

    private LocalDateTime borrowTimeStamp;
    private LocalDateTime returnTimeStamp;
    private String rowRecordStatus;
    private String difference;
    private String epc;
    private String call_sign;
    private String serial_number;
    private String transponder_status;


    public V_BORROW_RETURN_STATUS() {
    }

    public V_BORROW_RETURN_STATUS(Long id, LocalDateTime borrowTimeStamp, LocalDateTime returnTimeStamp, String rowRecordStatus, String difference, String epc, String call_sign, String serial_number, String transponder_status) {
        this.id = id;
        this.borrowTimeStamp = borrowTimeStamp;
        this.returnTimeStamp = returnTimeStamp;
        this.rowRecordStatus = rowRecordStatus;
        this.difference = difference;
        this.epc = epc;
        this.call_sign = call_sign;
        this.serial_number = serial_number;
        this.transponder_status = transponder_status;
    }


    public String getRowRecordStatus() {
        return rowRecordStatus;
    }

    public void setRowRecordStatus(String rowRecordStatus) {
        this.rowRecordStatus = rowRecordStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getBorrowTimeStamp() {
        return borrowTimeStamp;
    }

    public void setBorrowTimeStamp(LocalDateTime borrowTimeStamp) {
        this.borrowTimeStamp = borrowTimeStamp;
    }

    public LocalDateTime getReturnTimeStamp() {
        return returnTimeStamp;
    }

    public void setReturnTimeStamp(LocalDateTime returnTimeStamp) {
        this.returnTimeStamp = returnTimeStamp;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getCall_sign() {
        return call_sign;
    }

    public void setCall_sign(String call_sign) {
        this.call_sign = call_sign;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getTransponder_status() {
        return transponder_status;
    }

    public void setTransponder_status(String transponder_status) {
        this.transponder_status = transponder_status;
    }
}
