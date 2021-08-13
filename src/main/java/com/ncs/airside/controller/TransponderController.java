package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.database.RT_TRANSPONDER;
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
        return rt_transponder_repo.findAllByRowRecordStatus("valid");
    }

    @PostMapping("/insertTransponder")
    public ResponseEntity<Object> insertTransponder(@RequestBody RT_TRANSPONDER rt_transponder){

        Optional<RT_TRANSPONDER> chkEPCOptional = rt_transponder_repo.findByEPCAndServiceAvailabilityAndRowRecordStatus(rt_transponder.getEPC() , "Not Spare", "valid");
        Optional<RT_TRANSPONDER> chkCallSignOptional = rt_transponder_repo.findByCallSignAndServiceAvailabilityAndRowRecordStatus(rt_transponder.getCallSign() , "Not Spare","valid");

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

    @PostMapping("/updateTransponder")
    public ResponseEntity<Object> updateTransponder(@RequestBody RT_TRANSPONDER transponder) {

        Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.
                findByEPCAndServiceAvailabilityAndRowRecordStatus(transponder.getEPC(), transponder.getServiceAvailability(), "valid");

        if (transponder.getCallSign().equals(transponderOptional.get().getCallSign())
                && transponder.getSerialNumber().equals(transponderOptional.get().getSerialNumber())
                && transponder.getServiceAvailability().equals(transponderOptional.get().getServiceAvailability())
                && transponder.getDescription().equals(transponderOptional.get().getDescription())
                && transponder.getWarrantyFromDate().equals(transponderOptional.get().getWarrantyFromDate())
                && transponder.getWarrantyToDate().equals(transponderOptional.get().getWarrantyToDate())
                && transponder.getEPC().equals(transponderOptional.get().getEPC())
        ) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("No changes detected."));
        }

        transponderOptional.get().setRowRecordStatus("invalid");
        rt_transponder_repo.save(transponderOptional.get());

        //insert new records for company update
        RT_TRANSPONDER newTransponder = new RT_TRANSPONDER();

        newTransponder.setCallSign(transponder.getCallSign());
        newTransponder.setSerialNumber(transponder.getSerialNumber());
        newTransponder.setDescription(transponder.getDescription());
        newTransponder.setServiceAvailability(transponder.getServiceAvailability());
        newTransponder.setWarrantyFromDate(transponder.getWarrantyFromDate());
        newTransponder.setWarrantyToDate(transponder.getWarrantyToDate());
        newTransponder.setEPC(transponder.getEPC());
        newTransponder.setRowRecordStatus("valid");
        newTransponder.setTimestamp(LocalDateTime.now());
        rt_transponder_repo.save(newTransponder);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Update call sign " +transponder.getCallSign() +" successfully"));
    }

    @GetMapping("/deleteTransponder/{callSign}")
    public ResponseEntity<Object> deleteCompany(@PathVariable String callSign) {

        Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.
                findByCallSignAndServiceAvailabilityAndRowRecordStatus(callSign, "Not Spare" , "valid");

        transponderOptional.get().setRowRecordStatus("invalid");
        rt_transponder_repo.save(transponderOptional.get());

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Deleted call sign " + callSign + " successfully"));
    }

    @GetMapping("/gettransponderbyepc/{epcNumber}/{rowRecordStatus}")
    public ResponseEntity getTransponderByEPC(@PathVariable String epcNumber, @PathVariable String rowRecordStatus){

        Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.findByEPCAndServiceAvailabilityAndRowRecordStatus(epcNumber , "Not Spare", rowRecordStatus);

        if (transponderOptional.isPresent()){
            return ResponseEntity.ok().body(transponderOptional.get());
        }
        return ResponseEntity.badRequest().body(new MessageResponse("Unable to find registered transponder"));
    }

    @GetMapping("/gettransponderbycallsign/{callsign}/{rowRecordStatus}")
    public ResponseEntity getTransponderByCallSign(@PathVariable String callsign , @PathVariable String rowRecordStatus){

        Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.findByCallSignAndServiceAvailabilityAndRowRecordStatus(callsign , "Not Spare", rowRecordStatus);

        if (transponderOptional.isPresent()){
            return ResponseEntity.ok().body(transponderOptional.get());
        }
        return ResponseEntity.badRequest().body(new MessageResponse("unable to find Transponder with CallSign "+callsign));
    }

    @GetMapping("updatetransponderstatus/{epcNumber}/{transponderstatus}")
    public void updateTransponderStatus(@PathVariable String epcNumber , @PathVariable String transponderstatus  ) {

        Optional<RT_TRANSPONDER> transponderOptional = rt_transponder_repo.findByEPCAndServiceAvailabilityAndRowRecordStatus(epcNumber , "Not Spare", "VALID");
        transponderOptional.get().setServiceAvailability(transponderstatus);
        rt_transponder_repo.save(transponderOptional.get());

    }

}
