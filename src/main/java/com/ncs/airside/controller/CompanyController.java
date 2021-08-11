package com.ncs.airside.controller;

import com.ncs.airside.Exception.CustomerInformationNotFoundException;
import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.database.RT_VEHICLE;
import com.ncs.airside.repository.RT_COMPANY_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {

    private static Logger logger = LoggerFactory.getLogger(CustomerBillingController.class);

    @Autowired
    private RT_COMPANY_REPO rt_company_repo;

    @PostMapping("/insertCompany")
    public ResponseEntity<Object> createCompany(@RequestBody RT_COMPANY company){

        System.out.println(company);
        Optional<RT_COMPANY> companyOptional =  rt_company_repo.
                findByCompanyNameAndRowRecordStatus(company.getCompanyName(),"VALID");

        if (companyOptional.isPresent()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Company "+company.getCompanyName() +" already existed. Please insert another."));
        } else {
            rt_company_repo.save(company);
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse("Insert company "+company.getCompanyName() +" successfully"));
        }

    }

    @GetMapping("/companyInfos")
    public List<RT_COMPANY> retrieveCompanyInfos(){
        return rt_company_repo.findAll();
    }

    @GetMapping("/company/{companyName}/{rowRecordStatus}")
    public ResponseEntity<Object> retrieveCompany(@PathVariable String companyName , @PathVariable String rowRecordStatus){
        Optional<RT_COMPANY> companyOptional = rt_company_repo.findByCompanyNameAndRowRecordStatus(companyName,rowRecordStatus);

        if (companyOptional.isPresent()){
            return ResponseEntity
                    .ok()
                    .body(companyOptional.get());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Unable to find vehicle with registration number "+companyName));
        }
    }

/*
    @GetMapping("/customerInfos")
    public List<RT_CUSTOMER> retrieveAllCustomerInformations(){
        logger.info("The current thread name is "+Thread.currentThread().getName());
        return customerInformationRepo.findAll();
    }

    @GetMapping("/customerInfos/{customerID}")
    public RT_CUSTOMER retrieveCustomerInformation(@PathVariable int customerID){
        Optional<RT_CUSTOMER> custInfoOptional = rt_company_repo.findByCustomerID(customerID);

        if (!custInfoOptional.isPresent()){
            throw new CustomerInformationNotFoundException("Unable to find customer id : "+customerID);
        }

        return custInfoOptional.get();
    }

    @DeleteMapping("/customerInfos/{customerID}")
    public void deleteCustomerInformation(@PathVariable Long customerID){
        customerInformationRepo.deleteById(customerID);
    }

    @PostMapping("/customerInfos")
    public ResponseEntity<Object> createCustomerInformation(@RequestBody RT_CUSTOMER customerInformation){

        RT_COMPANY savedCustomerInformation = customerInformationRepo.save(customerInformation);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() //get the current request url which is '/customerInfos'
                .path("/{id}") //append '{id}' to  '/customerInfos' to become '/customerInfos/{id}'
                .buildAndExpand(savedCustomerInformation.getCustomerID()) //replace 'id'
                .toUri();

        return ResponseEntity.created(location).build();
    }*/
}
