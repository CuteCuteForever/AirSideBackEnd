package com.ncs.airside.model.helper;

public class RT_REPAIR_TRANSPONDER {

    String EPC;
    String callSign;
    String serialNumber;

    public RT_REPAIR_TRANSPONDER() {
    }

    public RT_REPAIR_TRANSPONDER(String EPC, String callSign, String serialNumber) {
        this.EPC = EPC;
        this.callSign = callSign;
        this.serialNumber = serialNumber;
    }

    public String getEPC() {
        return EPC;
    }

    public void setEPC(String EPC) {
        this.EPC = EPC;
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
}
