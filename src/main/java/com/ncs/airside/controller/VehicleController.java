package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_VEHICLE;
import com.ncs.airside.repository.RT_VEHICLE_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class VehicleController {

    private static Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private RT_VEHICLE_REPO rt_vehicle_repo;

    @GetMapping("/vehicleInfos")
    public List<RT_VEHICLE> retrieveVehicleCompanyInfos(){
        return rt_vehicle_repo.findAll();
    }

    @PostMapping("/insertVehicle")
    public ResponseEntity<Object> createVehicle(@RequestBody RT_VEHICLE vehicle){

        System.out.println(vehicle);

        Optional<RT_VEHICLE> vehicleOptional =  rt_vehicle_repo.
                findByRegistrationNumberAndRowRecordStatus(vehicle.getRegistrationNumber(),"VALID");

        if (vehicleOptional.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Vehicle already existed. Please insert another."));
        } else {
            rt_vehicle_repo.save(vehicle);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Insert Vehicle Successfully"));
        }
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

}
