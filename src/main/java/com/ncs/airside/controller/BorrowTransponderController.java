package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.database.RT_EPC_ALERT;
import com.ncs.airside.model.database.RT_TRANSPONDER_BORROW;
import com.ncs.airside.repository.RT_TRANSPONDER_BORROWER_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class BorrowTransponderController {


    private static Logger logger = LoggerFactory.getLogger(CustomerBillingController.class);

    @Autowired
    private RT_TRANSPONDER_BORROWER_REPO rt_transponder_borrower_repo;

    @PostMapping("/insertBorrowTransponder")
    public void createCustomerInformation(@RequestBody List<RT_TRANSPONDER_BORROW> rt_transponder_BORROW){

        rt_transponder_BORROW.forEach(s -> {
            System.out.println(s.getEPC());
        });
    }

    @GetMapping(path = "/getborrowedtransponder/{epcNumber}/{rowRecordStatus}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBorrowedTransponder(@PathVariable String epcNumber , @PathVariable String rowRecordStatus){

        Optional<RT_TRANSPONDER_BORROW> transponderBorrowerOptional = rt_transponder_borrower_repo.findByEPCAndRowRecordStatus(epcNumber, rowRecordStatus);

        if (transponderBorrowerOptional.isPresent() ){
            return ResponseEntity.ok().body(transponderBorrowerOptional.get());
        }

        return ResponseEntity.badRequest().body(new MessageResponse("unable to find borrowed Transponder with epc "+epcNumber));
    }

    @GetMapping("/updaterowrecordstatus/{epcNumber}/{status}")
    public ResponseEntity<Object> createCustomerInformation(@PathVariable String epcNumber , @PathVariable String status){

        Optional<RT_TRANSPONDER_BORROW> transponderBorrowerOptional = rt_transponder_borrower_repo.findByEPCAndRowRecordStatus(epcNumber , "VALID");

        if (transponderBorrowerOptional.isPresent()){
            RT_TRANSPONDER_BORROW temp = transponderBorrowerOptional.get();
            temp.setRowRecordStatus("INVALID");
            rt_transponder_borrower_repo.save(temp) ;
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Set Status to Invalid Successfully"));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Unable to find respective EPC "+epcNumber));

    }

}
