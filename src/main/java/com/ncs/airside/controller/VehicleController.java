package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.HIBERNATE_SEQUENCE;
import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.database.RT_VEHICLE;
import com.ncs.airside.repository.HIBERNATE_SEQUENCE_REPO;
import com.ncs.airside.repository.RT_VEHICLE_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class VehicleController {

    private static Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private RT_VEHICLE_REPO rt_vehicle_repo;

    @Autowired
    HIBERNATE_SEQUENCE_REPO rt_hibernate_sequence_repo;

    @GetMapping("/vehicleInfos")
    public List<RT_VEHICLE> retrieveVehicleCompanyInfos(){
        return rt_vehicle_repo.findAll();
    }

    @PostMapping("/insertVehicle")
    public ResponseEntity<Object> createVehicle(@RequestBody RT_VEHICLE vehicle){

        Optional<RT_VEHICLE> vehicleOptional =  rt_vehicle_repo.
                findByRegistrationNumberAndRowRecordStatus(vehicle.getRegistrationNumber(),"valid");

        if (vehicleOptional.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Vehicle already existed. Please insert another."));
        }

        List<HIBERNATE_SEQUENCE> hibernateSequencesList = rt_hibernate_sequence_repo.findAll();
        HIBERNATE_SEQUENCE rtHibernateSequence = hibernateSequencesList.get(0);

        Long nextVal = rtHibernateSequence.getNextVal();
        vehicle.setVehicleId(nextVal);
        rt_vehicle_repo.save(vehicle);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Insert Vehicle Successfully"));

    }

    @PostMapping("/updateVehicle")
    public ResponseEntity<Object> updateVehicle(@RequestBody RT_VEHICLE vehicle) {

        Optional<RT_VEHICLE> vehicleOptional = rt_vehicle_repo.
                findByVehicleRowIdAndRowRecordStatus(vehicle.getVehicleRowId(), "valid");

        if (vehicleOptional.isPresent() && (vehicle.getCompanyId().equals(vehicleOptional.get().getCompanyId())
                && vehicle.getRegistrationNumber().equals(vehicleOptional.get().getRegistrationNumber())
                && vehicle.getVehicleId().equals(vehicleOptional.get().getVehicleId())
        )) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("No changes detected."));
        }

        vehicleOptional.get().setRowRecordStatus("invalid");
        rt_vehicle_repo.save(vehicleOptional.get());

        RT_VEHICLE newVehicle = new RT_VEHICLE();
        newVehicle.setVehicleId(vehicle.getVehicleId());
        newVehicle.setCompanyId(vehicle.getCompanyId());
        newVehicle.setRegistrationNumber(vehicle.getRegistrationNumber());
        newVehicle.setRowRecordStatus("valid");
        newVehicle.setTimestamp(LocalDateTime.now());

        rt_vehicle_repo.save(newVehicle);
        return ResponseEntity
                .ok()
                .body(new MessageResponse("Update company " + newVehicle.getRegistrationNumber() + " successfully"));
    }

    @PostMapping("/deleteVehicle")
    public ResponseEntity<Object> deleteVehicle(@RequestBody RT_VEHICLE vehicle) {

        Optional<RT_VEHICLE> vehicleOptional = rt_vehicle_repo.
                findByVehicleRowIdAndRowRecordStatus(vehicle.getVehicleRowId(), "valid");

        vehicleOptional.get().setRowRecordStatus("invalid");
        rt_vehicle_repo.save(vehicleOptional.get());

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Deleted vehicle registration number " + vehicle.getRegistrationNumber() + " successfully"));
    }


    @GetMapping("/vehicle/{registrationNumber}/{rowRecordStatus}")
    public ResponseEntity<Object> retrieveVehicleByRegistrationNumber(@PathVariable String registrationNumber , @PathVariable String rowRecordStatus){


        Optional<RT_VEHICLE> vehicle =  rt_vehicle_repo.findByRegistrationNumberAndRowRecordStatus(registrationNumber,rowRecordStatus);

        if (vehicle.isPresent()){
            return ResponseEntity
                    .ok()
                    .body(vehicle.get());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Unable to find vehicle with registration number "+registrationNumber));
        }
    }

    @GetMapping("/vehicle/{companyId}")
    public ResponseEntity<Object> retrieveVehicleByCompanyId(@PathVariable Long companyId){

        List<RT_VEHICLE> vehicle =  rt_vehicle_repo.findByCompanyIdAndRowRecordStatus(companyId,"valid");

        if (vehicle.size() == 0){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("No vehicle registered with current company"));
        }
        return ResponseEntity
                .ok()
                .body(vehicle);
    }
}

