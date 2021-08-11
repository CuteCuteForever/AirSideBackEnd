package com.ncs.airside.controller;

import com.ncs.airside.model.database.RT_CUST_BILLING;
import com.ncs.airside.repository.RT_CUST_BILL_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerBillingController {

    private static Logger logger = LoggerFactory.getLogger(CustomerBillingController.class);

    @Autowired
    private RT_CUST_BILL_REPO RT_CUST_BILL_REPO;

    @GetMapping("/companyInformations")
    public List<RT_CUST_BILLING> retrieveCustomerBilling(){
        return RT_CUST_BILL_REPO.findAll();
    }


}
