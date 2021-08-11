package com.ncs.airside.controller;

import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.view.V_VEHICLE_COMPANY;
import com.ncs.airside.repository.V_VEHICLE_COMPANY_REPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VehicleCompanyController {

    @Autowired
    private V_VEHICLE_COMPANY_REPO v_vehicle_company_repo;

    @GetMapping("vehicleCompanyInfos")
    public List<V_VEHICLE_COMPANY> retrieveVehicleCompanyInfos(){
        return v_vehicle_company_repo.findAll();
    }
}
