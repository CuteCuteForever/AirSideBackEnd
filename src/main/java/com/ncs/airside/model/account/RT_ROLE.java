package com.ncs.airside.model.account;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class RT_ROLE {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private int id;

    @Enumerated(EnumType.STRING)
    private ERole name;

    public RT_ROLE(){
    }

    public RT_ROLE(ERole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

}
