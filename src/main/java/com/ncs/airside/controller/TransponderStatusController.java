package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_TRANSPONDER;
import com.ncs.airside.model.database.RT_TRANSPONDER_STATUS;
import com.ncs.airside.model.helper.RT_REPAIR_TRANSPONDER;
import com.ncs.airside.model.helper.RT_RETURN_TRANSPONDER;
import com.ncs.airside.model.helper.RT_SERVICED_TRANSPONDER;
import com.ncs.airside.model.view.V_TRANSPONDER_STATUS;
import com.ncs.airside.repository.RT_TRANSPONDER_REPO;
import com.ncs.airside.repository.RT_TRANSPONDER_STATUS_REPO;
import com.ncs.airside.repository.V_TRANSPONDER_STATUS_REPO;
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
public class TransponderStatusController {


    private static Logger logger = LoggerFactory.getLogger(CustomerBillingController.class);

    @Autowired
    private RT_TRANSPONDER_STATUS_REPO rt_transponder_status_repo;

    @Autowired
    private RT_TRANSPONDER_REPO rt_transponder_repo;
    @Autowired
    private V_TRANSPONDER_STATUS_REPO v_transponder_status_repo;

    @GetMapping("/transponderstatusview")
    public ResponseEntity<Object> retrieveTransponderStatusView(){

        List<V_TRANSPONDER_STATUS> v_transponder_statusList = this.v_transponder_status_repo.findAll();
        return ResponseEntity.ok().body(v_transponder_statusList);
    }

    @GetMapping("/transponderstatus")
    public ResponseEntity<Object> retrieveTransponderStatus(){

        List<RT_TRANSPONDER_STATUS> transponder_statusesList = this.rt_transponder_status_repo.findAll();

        return ResponseEntity.ok().body(transponder_statusesList);

    }

    @PostMapping(path = "/insertborrowtransponder" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertBorrowTransponder(@RequestBody List<RT_TRANSPONDER_STATUS> rt_transponder_status_List){


        boolean isAllTransponderValid = true;
        String invalidTransponderEPC ="" ;
        //to check that the borrow transponder exist in transponder table with serviceAvailability = "Not Spare" and RRS = "valid"
        for (RT_TRANSPONDER_STATUS rt_transponder_status : rt_transponder_status_List) {
            String epcNumber = rt_transponder_status.getEPC();

            Optional<RT_TRANSPONDER> rt_transponder= rt_transponder_repo.findByEPCAndServiceAvailabilityAndRowRecordStatus(epcNumber, "Not Spare" , "valid");

            if (!rt_transponder.isPresent()) {
                isAllTransponderValid = false;
                invalidTransponderEPC = rt_transponder_status.getEPC();
                break;
            }
        }

        if (!isAllTransponderValid){
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to find Not Spare transponder with EPC "+invalidTransponderEPC +"."));
        }

        boolean isExistingBorrowTransponderStatusFound = false;
        // to check whether existing epc found that is transponderStatus="Rent Out" and RRS="Valid"
        for (RT_TRANSPONDER_STATUS rt_transponder_status : rt_transponder_status_List) {
            String epcNumber = rt_transponder_status.getEPC();

            Optional<RT_TRANSPONDER_STATUS> rt_transponder_statusOpt= rt_transponder_status_repo.findByEPCAndTransponderStatusAndRowRecordStatus(epcNumber, "Rent Out" , "valid");

            if (rt_transponder_statusOpt.isPresent()) {
                isExistingBorrowTransponderStatusFound = true;
                invalidTransponderEPC = rt_transponder_status.getEPC();
                break;
            }
        }

        if (isExistingBorrowTransponderStatusFound){
            return ResponseEntity.badRequest().body(new MessageResponse("Found existing transponder with EPC "+invalidTransponderEPC +". Please borrow another."));
        }

        //Save to DB
        rt_transponder_status_List.forEach( item -> {
            item.setOutTimestamp(LocalDateTime.now());
            item.setInTimestamp(null); ; // no need in_timestamp for borrowing of transponder
            this.rt_transponder_status_repo.save(item);
        });

        return ResponseEntity.ok().body(new MessageResponse("Transponder successfully borrowed"));
    }

    @PostMapping(path = "/insertreturntransponder" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertReturnTransponder(@RequestBody List<RT_RETURN_TRANSPONDER> return_transponderList){

        String inValidEPCNumber = "";
        boolean isAllBorrowedTransponderExist = true;
        //check all epc that need to return exist on the transponder status borrow
        for (RT_RETURN_TRANSPONDER rt_return_transponder : return_transponderList) {
            Optional<RT_TRANSPONDER_STATUS> rt_transponder_statusOptional = rt_transponder_status_repo.findByEPCAndTransponderStatusAndRowRecordStatus(rt_return_transponder.getEPC() , "Rent Out" , "valid");

            if (!rt_transponder_statusOptional.isPresent()){
                inValidEPCNumber = rt_return_transponder.getEPC();
                isAllBorrowedTransponderExist = false;
                break;
            }
        }

        if (!isAllBorrowedTransponderExist){
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to find Borrowed transponder with EPC "+inValidEPCNumber +"."));
        }

        // Save all to db and update in_timestamp
        for (RT_RETURN_TRANSPONDER rt_return_transponder : return_transponderList) {

            Optional<RT_TRANSPONDER_STATUS> rt_transponder_status_from_db = rt_transponder_status_repo.findByEPCAndTransponderStatusAndRowRecordStatus(rt_return_transponder.getEPC() , "Rent Out" , "valid");

            RT_TRANSPONDER_STATUS rtTransponderStatus_from_db = rt_transponder_status_from_db.get();

            rtTransponderStatus_from_db.setRowRecordStatus("invalid");
            rt_transponder_status_repo.save(rtTransponderStatus_from_db);

            RT_TRANSPONDER_STATUS rt_new_TransponderStatus = new RT_TRANSPONDER_STATUS(
                    null,
                    rtTransponderStatus_from_db.getEPC(),
                    rtTransponderStatus_from_db.getCompanyId(),
                    rtTransponderStatus_from_db.getOutTimestamp(),
                    LocalDateTime.now(),
                    rtTransponderStatus_from_db.getRentalDuration(),
                    rtTransponderStatus_from_db.getTransponderId(),
                    "Available",
                    rtTransponderStatus_from_db.getVehicleId(),
                    "valid",
                    LocalDateTime.now() //timeStamp
            );

            rt_transponder_status_repo.save(rt_new_TransponderStatus);
        }
        return ResponseEntity.ok().body(new MessageResponse("Transponder successfully returned"));
    }

    @PostMapping(path = "/insertrepairtransponder" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertRepairTransponder(@RequestBody List<RT_REPAIR_TRANSPONDER> repair_transponderList){

        String inValidEPCNumber = "";
        boolean isAllRepairedTransponderExist = true;
        //check all epc that need to return exist on the transponder status borrow
        for (RT_REPAIR_TRANSPONDER rt_repair_transponder : repair_transponderList) {
            Optional<RT_TRANSPONDER_STATUS> rt_transponder_statusOptional = rt_transponder_status_repo.findByEPCAndTransponderStatusAndRowRecordStatusAndInTimestampIsNotNull(rt_repair_transponder.getEPC() , "Available" , "valid");

            if (!rt_transponder_statusOptional.isPresent()){
                inValidEPCNumber = rt_repair_transponder.getEPC();
                isAllRepairedTransponderExist = false;
                break;
            }
        }

        if (!isAllRepairedTransponderExist){
            return ResponseEntity.badRequest().body(new MessageResponse("Repaired transponder have not being returned yet. Please return transponder with EPC "+inValidEPCNumber +" first."));
        }

        // Save all to db and update in_timestamp
        for (RT_REPAIR_TRANSPONDER rt_repair_transponder : repair_transponderList) {

            Optional<RT_TRANSPONDER_STATUS> rt_transponder_status_from_db = rt_transponder_status_repo.findByEPCAndTransponderStatusAndRowRecordStatusAndInTimestampIsNotNull(rt_repair_transponder.getEPC() , "Available" , "valid");

            RT_TRANSPONDER_STATUS rtTransponderStatus_from_db = rt_transponder_status_from_db.get();

            rtTransponderStatus_from_db.setRowRecordStatus("invalid");
            rt_transponder_status_repo.save(rtTransponderStatus_from_db);

            RT_TRANSPONDER_STATUS rt_new_TransponderStatus = new RT_TRANSPONDER_STATUS(
                    null,
                    rtTransponderStatus_from_db.getEPC(),
                    rtTransponderStatus_from_db.getCompanyId(),
                    LocalDateTime.now(),
                    null,
                    null,
                    rtTransponderStatus_from_db.getTransponderId(),
                    "Repair",
                    rtTransponderStatus_from_db.getVehicleId(),
                    "valid",
                    LocalDateTime.now() //timeStamp
            );

            rt_transponder_status_repo.save(rt_new_TransponderStatus);
        }
        return ResponseEntity.ok().body(new MessageResponse("Transponder successfully returned"));
    }

    @PostMapping(path = "/insertservicedtransponder" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertServicedTransponder(@RequestBody List<RT_SERVICED_TRANSPONDER> serviced_transponderList){

        String inValidEPCNumber = "";
        boolean isAllBorrowedTransponderExist = true;
        //check all epc that need to return exist on the transponder status borrow
        for (RT_SERVICED_TRANSPONDER rt_serviced_transponder : serviced_transponderList) {
            Optional<RT_TRANSPONDER_STATUS> rt_transponder_statusOptional = rt_transponder_status_repo.findByEPCAndTransponderStatusAndRowRecordStatus(rt_serviced_transponder.getEPC() , "Repair" , "valid");

            if (!rt_transponder_statusOptional.isPresent()){
                inValidEPCNumber = rt_serviced_transponder.getEPC();
                isAllBorrowedTransponderExist = false;
                break;
            }
        }

        if (!isAllBorrowedTransponderExist){
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to find repair transponder with EPC "+inValidEPCNumber +"."));
        }

        // Save all to db and update in_timestamp
        for (RT_SERVICED_TRANSPONDER rt_serviced_transponder : serviced_transponderList) {

            Optional<RT_TRANSPONDER_STATUS> rt_transponder_status_from_db = rt_transponder_status_repo.findByEPCAndTransponderStatusAndRowRecordStatus(rt_serviced_transponder.getEPC() , "Repair" , "valid");

            RT_TRANSPONDER_STATUS rtTransponderStatus_from_db = rt_transponder_status_from_db.get();

            rtTransponderStatus_from_db.setRowRecordStatus("invalid");
            rt_transponder_status_repo.save(rtTransponderStatus_from_db);

            RT_TRANSPONDER_STATUS rt_new_TransponderStatus = new RT_TRANSPONDER_STATUS(
                    null,
                    rtTransponderStatus_from_db.getEPC(),
                    rtTransponderStatus_from_db.getCompanyId(),
                    rtTransponderStatus_from_db.getOutTimestamp(),
                    LocalDateTime.now(),
                    rtTransponderStatus_from_db.getRentalDuration(),
                    rtTransponderStatus_from_db.getTransponderId(),
                    "Available",
                    rtTransponderStatus_from_db.getVehicleId(),
                    "valid",
                    LocalDateTime.now() //timeStamp
            );

            rt_transponder_status_repo.save(rt_new_TransponderStatus);
        }
        return ResponseEntity.ok().body(new MessageResponse("Transponder successfully return from serviced"));
    }

}
