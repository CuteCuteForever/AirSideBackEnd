package com.ncs.airside.model.rfid;

public class RfidResponse {

    int counter;
    String EPCStr;
    int writeDataInfo;
    int readDataInfo;
    String Memdata;

    public RfidResponse(int counter, String EPCStr, int writeDataInfo, int readDataInfo, String memdata) {
        this.counter = counter;
        this.EPCStr = EPCStr;
        this.writeDataInfo = writeDataInfo;
        this.readDataInfo = readDataInfo;
        Memdata = memdata;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public RfidResponse() {
    }

    public String getEPCStr() {
        return EPCStr;
    }

    public void setEPCStr(String EPCStr) {
        this.EPCStr = EPCStr;
    }

    public int getWriteDataInfo() {
        return writeDataInfo;
    }

    public void setWriteDataInfo(int writeDataInfo) {
        this.writeDataInfo = writeDataInfo;
    }

    public int getReadDataInfo() {
        return readDataInfo;
    }

    public void setReadDataInfo(int readDataInfo) {
        this.readDataInfo = readDataInfo;
    }

    public String getMemdata() {
        return Memdata;
    }

    public void setMemdata(String memdata) {
        Memdata = memdata;
    }
}
