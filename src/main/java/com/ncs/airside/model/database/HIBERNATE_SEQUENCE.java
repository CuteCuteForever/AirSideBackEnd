package com.ncs.airside.model.database;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class HIBERNATE_SEQUENCE {

    @Id
    private Long nextVal;

    public HIBERNATE_SEQUENCE() {
    }

    public HIBERNATE_SEQUENCE(Long nextVal) {
        this.nextVal = nextVal;
    }

    public Long getNextVal() {
        return nextVal;
    }

    public void setNextVal(Long nextVal) {
        this.nextVal = nextVal;
    }
}
