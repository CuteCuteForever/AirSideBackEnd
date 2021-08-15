package com.ncs.airside.model.database;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class RT_CUST_BILLING {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long custBillID;

    private int customerID;
    private int billingID;
    private LocalDateTime timestamp;
    private String rowRecordStatus;

    public RT_CUST_BILLING() {
    }

    public RT_CUST_BILLING(Long custBillID, int customerID, int billingID, LocalDateTime timestamp, String rowRecordStatus) {
        this.custBillID = custBillID;
        this.customerID = customerID;
        this.billingID = billingID;
        this.timestamp = timestamp;
        this.rowRecordStatus = rowRecordStatus;
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

    public Long getCustBillID() {
        return custBillID;
    }

    public void setCustBillID(Long custBillID) {
        this.custBillID = custBillID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getBillingID() {
        return billingID;
    }

    public void setBillingID(int billingID) {
        this.billingID = billingID;
    }
}
