package com.ncs.airside.model.helper;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class READER_ERROR_CODE {

    HashMap<Integer,String> map ;

    public READER_ERROR_CODE(){
        map=new HashMap<Integer,String>();
        map.put(0 , "API is called successfully");
        map.put(1 , "Tag inventory is completed successfully and reader is able to deliver data response within the predefined inventory time");
        map.put(2 , "Inventory timeout");
        map.put(5 , "Access password error.");
        map.put(9 , "Kill password error.");
        map.put(10 , "All-zero tag killing password is invalid.");
        map.put(11 , "Command is not support by the tag");
        map.put(12 , "All-zero tag access password is invalid for such command.");
        map.put(13 , "Fail to setup read protection for a protection enabled tag.");
        map.put(14 , "Fail to unlock a protection disabled tag.");
        map.put(16 , "Some bytes stored in the tag are locked.");
        map.put(17 , "Lock operation failed.");
        map.put(18 , "Already locked, lock operation failed.");
        map.put(19 , "Fail to store the value of some preserved parameters. Configuration will still valid before reader shut down.");
        map.put(20 , "Modification failed.");
        map.put(21 , "Response within the predefined inventory time.");
        map.put(23 , "Further data is waiting to be delivered.");
        map.put(24 , "Reader memory is full");
        map.put(25 , "All-zero access password is invalid for such command or command is not supported by the tag.");
        map.put(248 , "Error detected in antenna check.");
        map.put(249 , "Operation failed.");
        map.put(250 , "Tag is detected, but fails to complete operation due to poor communication.");
        map.put(251 , "No tag is detected.");
        map.put(252 , "Error code returned from tags.");
        map.put(253 , "Command length error.");
        map.put(254 , "Illegal command.");
        map.put(255 , "Parameter error.");
        map.put(48, "Communication error.");
        map.put(51 , "Reader is busy, operation in process.");
        map.put(53 , "Port is already opened.");
        map.put(55 , "Invalid handle.");
    }

    public String getErrorCode(int key){
        return this.map.get(key);
    }
}


