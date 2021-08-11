/*
package com.ncs.airside.controller;

import com.ncs.airside.model.view.NCS_V_COMPANY_STATUS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ViewController {

    private Logger logger = LoggerFactory.getLogger(RFIDController.class);

    @Autowired
    private NcsViewCompanyStatus ncsViewCompanyStatus;

    @GetMapping("/getCompanyStatusView")
    public List<NCS_V_COMPANY_STATUS> getCompanyStatusView(){
        return ncsViewCompanyStatus.findAll();
    }

}
*/
