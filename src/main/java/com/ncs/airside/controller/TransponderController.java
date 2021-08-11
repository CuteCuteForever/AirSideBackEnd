package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.database.RT_EPC_ALERT;
import com.ncs.airside.model.database.RT_TRANSPONDER;
import com.ncs.airside.repository.RT_TRANSPONDER_BORROWER_REPO;
import com.ncs.airside.repository.RT_TRANSPONDER_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class TransponderController {

    private static Logger logger = LoggerFactory.getLogger(TransponderController.class);

    @Autowired
    private RT_TRANSPONDER_REPO rt_transponder_repo;

    @GetMapping("/transpondersInfos")
    public List<RT_TRANSPONDER> retrieveAllTransponders(){
        return rt_transponder_repo.findAll();
    }

    @PostMapping("/insertTransponder")
    public ResponseEntity<Object> createCustomerInformation(@RequestBody RT_TRANSPONDER rt_transponder){



        Optional<RT_TRANSPONDER> chkEPCOptional = rt_transponder_repo.findByEPCAndRowRecordStatus(rt_transponder.getEPC() , "valid");
        Optional<RT_TRANSPONDER> chkCallSignOptional = rt_transponder_repo.findByCallSignAndRowRecordStatus(rt_transponder.getCallSign() , "valid");

        if (chkEPCOptional.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("EPC Existed. Please use another EPC."));
        }

        if (chkCallSignOptional.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Call Sign Existed. Please use another EPC."));
        }

        rt_transponder_repo.save(rt_transponder);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Insert Transponder Successfully"));
    }

    @GetMapping("/gettransponderbyepc/{epcNumber}/{rowRecordStatus}")
    public ResponseEntity getTransponderByEPC(@PathVariable String epcNumber, @PathVariable String rowRecordStatus){

        Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.findByEPCAndRowRecordStatus(epcNumber , rowRecordStatus);

        if (transponderOptional.isPresent()){
            return ResponseEntity.ok().body(transponderOptional.get());
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Unable to find registered transponder"));
    }

    @GetMapping("/gettransponderbycallsign/{callsign}/{rowRecordStatus}")
    public ResponseEntity getTransponderByCallSign(@PathVariable String callsign , @PathVariable String rowRecordStatus){

        Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.findByCallSignAndRowRecordStatus(callsign , rowRecordStatus);

        if (transponderOptional.isPresent()){
            return ResponseEntity.ok().body(transponderOptional.get());
        }
        return ResponseEntity.badRequest().body(new MessageResponse("unable to find Transponder with CallSign "+callsign));
    }

    @GetMapping("updatetransponderstatus/{epcNumber}/{transponderstatus}")
    public void updateTransponderStatus(@PathVariable String epcNumber , @PathVariable String transponderstatus  ) {

        Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.findByEPCAndRowRecordStatus(epcNumber , "VALID");
        transponderOptional.get().setServiceAvailability(transponderstatus);
        rt_transponder_repo.save(transponderOptional.get());

    }

}
