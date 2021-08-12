package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_TRANSPONDER_STATUS;
import com.ncs.airside.repository.RT_TRANSPONDER_STATUS_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TransponderStatusController {


    private static Logger logger = LoggerFactory.getLogger(CustomerBillingController.class);

    @Autowired
    private RT_TRANSPONDER_STATUS_REPO rt_transponder_status_repo;

    @GetMapping("/transponderstatus")
    public ResponseEntity<Object> retrieveTransponderStatus(){

        List<RT_TRANSPONDER_STATUS> transponder_statusesList = this.rt_transponder_status_repo.findAll();

        return ResponseEntity.ok().body(transponder_statusesList);

    }

    @PostMapping(path = "/insertborrowtransponder" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertBorrowTransponder(@RequestBody List<RT_TRANSPONDER_STATUS> rt_transponder_status_List){

        rt_transponder_status_List.forEach( item -> {
            item.setIn_timestamp(null); ; // no need in_timestamp for borrowing of transponder
            this.rt_transponder_status_repo.save(item);
        });

        return ResponseEntity.ok().body(new MessageResponse("Transponder successfully borrowed"));
    }

    @GetMapping(path = "/getborrowedtransponder/{epcNumber}/{rowRecordStatus}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBorrowedTransponder(@PathVariable String epcNumber , @PathVariable String rowRecordStatus){

        Optional<RT_TRANSPONDER_STATUS> transponderBorrowerOptional = rt_transponder_status_repo.findByEPCAndRowRecordStatus(epcNumber, rowRecordStatus);

        if (transponderBorrowerOptional.isPresent() ){
            return ResponseEntity.ok().body(transponderBorrowerOptional.get());
        }

        return ResponseEntity.badRequest().body(new MessageResponse("unable to find borrowed Transponder with epc "+epcNumber));
    }

    @GetMapping("/updatetransponderstatus/{epcNumber}/{status}")
    public ResponseEntity<Object> updateTransponderStatus(@PathVariable String epcNumber , @PathVariable String status){

        Optional<RT_TRANSPONDER_STATUS> transponderBorrowerOptional = rt_transponder_status_repo.findByEPCAndRowRecordStatus(epcNumber , "VALID");

        if (transponderBorrowerOptional.isPresent()){
            RT_TRANSPONDER_STATUS temp = transponderBorrowerOptional.get();
            temp.setRowRecordStatus("invalid");
            rt_transponder_status_repo.save(temp) ;
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Set Status to Invalid Successfully"));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Unable to find respective EPC "+epcNumber));

    }

}
