package com.ncs.airside.controller;

import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.view.V_VEHICLE_COMPANY;
import com.ncs.airside.repository.V_VEHICLE_COMPANY_REPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class VehicleCompanyController {

    @Autowired
    private V_VEHICLE_COMPANY_REPO v_vehicle_company_repo;

    @GetMapping("vehicleCompanyInfos")
    public List<V_VEHICLE_COMPANY> retrieveVehicleCompanyInfos(){
        return v_vehicle_company_repo.findAll();
    }

    @GetMapping("vehicleCompany/{companyId}")
    public ResponseEntity getVehicleCompanyByCompanyId(@PathVariable Long companyId){
        Optional<V_VEHICLE_COMPANY> v_vehicle_company = v_vehicle_company_repo.findByCompanyId(companyId);

        if (v_vehicle_company.isPresent()){
            return ResponseEntity.ok().body(v_vehicle_company);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Unable to find vehicle-company info by "+companyId));
        }
    }


}
