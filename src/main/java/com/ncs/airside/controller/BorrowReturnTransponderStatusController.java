package com.ncs.airside.controller;

import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.view.V_BORROW_RETURN_STATUS;
import com.ncs.airside.repository.V_BORROW_RETURN_STATUS_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BorrowReturnTransponderStatusController {


    private static Logger logger = LoggerFactory.getLogger(CustomerBillingController.class);

    @Autowired
    private V_BORROW_RETURN_STATUS_REPO v_borrow_return_status_repo;

    @GetMapping("/borrowreturntransponderstatusInfos")
    public List<V_BORROW_RETURN_STATUS> retrieveBorrowReturnTransponderStatusInfos(){
        return v_borrow_return_status_repo.findAll();
    }
}
