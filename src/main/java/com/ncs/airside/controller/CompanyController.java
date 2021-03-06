package com.ncs.airside.controller;

import com.ncs.airside.Exception.CustomerInformationNotFoundException;
import com.ncs.airside.model.account.MessageResponse;
import com.ncs.airside.model.database.HIBERNATE_SEQUENCE;
import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.database.RT_VEHICLE;
import com.ncs.airside.repository.HIBERNATE_SEQUENCE_REPO;
import com.ncs.airside.repository.RT_COMPANY_REPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class CompanyController {

    private static Logger logger = LoggerFactory.getLogger(CustomerBillingController.class);

    @Autowired
    private RT_COMPANY_REPO rt_company_repo;

    @Autowired
    HIBERNATE_SEQUENCE_REPO rt_hibernate_sequence_repo;

    @PostMapping("/insertCompany")
    public ResponseEntity<Object> createCompany(@RequestBody RT_COMPANY company) {

        Optional<RT_COMPANY> companyOptional = rt_company_repo.
                findByCompanyNameAndRowRecordStatus(company.getCompanyName(), "valid");

        if (companyOptional.isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Company " + company.getCompanyName() + " already existed. Please insert another."));
        }

        List<HIBERNATE_SEQUENCE> hibernateSequencesList = rt_hibernate_sequence_repo.findAll();
        HIBERNATE_SEQUENCE rtHibernateSequence = hibernateSequencesList.get(0);

        Long nextVal = rtHibernateSequence.getNextVal();
        System.out.println(nextVal);
        company.setCompanyId(nextVal);
        rt_company_repo.save(company);
        return ResponseEntity
                .ok()
                .body(new MessageResponse("Insert company " + company.getCompanyName() + " successfully!"));
    }

    @PostMapping("/updateCompany")
    public ResponseEntity<Object> updateCompany(@RequestBody RT_COMPANY company) {

        System.out.println("REACC");
        Optional<RT_COMPANY> companyOptional = rt_company_repo.
                findByCompanyRowIdAndRowRecordStatus(company.getCompanyRowId(), "valid");

        if (companyOptional.isPresent() && (company.getCompanyName().equals(companyOptional.get().getCompanyName())
                && company.getAddress().equals(companyOptional.get().getAddress())
                && company.getContactPersonName().equals(companyOptional.get().getContactPersonName())
                && company.getContactPersonNumber().equals(companyOptional.get().getContactPersonNumber())
                && company.getDepartment().equals(companyOptional.get().getDepartment())
                && company.getCompanyId().equals(companyOptional.get().getCompanyId())
        )) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("No changes were found for company "+company.getCompanyName()));
        }

        companyOptional.get().setRowRecordStatus("invalid");
        rt_company_repo.save(companyOptional.get());

        //insert new records for company update
        RT_COMPANY newCompany = new RT_COMPANY();

        newCompany.setCompanyName(company.getCompanyName());
        newCompany.setCompanyId(company.getCompanyId());
        newCompany.setAddress(company.getAddress());
        newCompany.setContactPersonName(company.getContactPersonName());
        newCompany.setContactPersonNumber(company.getContactPersonNumber());
        newCompany.setDepartment(company.getDepartment());
        newCompany.setTimestamp(LocalDateTime.now());
        newCompany.setRowRecordStatus("valid");
        rt_company_repo.save(newCompany);

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Updated company " + company.getCompanyName() + " successfully"));
    }


    @GetMapping("/companyInfos")
    public List<RT_COMPANY> retrieveCompanyInfos() {
        return rt_company_repo.findAllByRowRecordStatus("valid");
    }

    @PostMapping("/deleteCompany")
    public ResponseEntity<Object> deleteCompany(@RequestBody RT_COMPANY company) {

        Optional<RT_COMPANY> companyOptional = rt_company_repo.
                findByCompanyRowIdAndRowRecordStatus(company.getCompanyRowId(), "valid");

        companyOptional.get().setRowRecordStatus("invalid");
        rt_company_repo.save(companyOptional.get());

        return ResponseEntity
                .ok()
                .body(new MessageResponse("Deleted company " + company.getCompanyName() + " successfully"));
    }


    @GetMapping("/uniqueCompany")
    public ResponseEntity<Object> retrieveUniqueCompany() {

        List<RT_COMPANY> companyList = rt_company_repo.
                findDistinctCompanyNameByRowRecordStatus("valid");

        return ResponseEntity
                .ok()
                .body(companyList);
    }


    @GetMapping("/company/{companyName}/{rowRecordStatus}")
    public ResponseEntity<Object> retrieveCompany(@PathVariable String companyName, @PathVariable String rowRecordStatus) {
        Optional<RT_COMPANY> companyOptional = rt_company_repo.findByCompanyNameAndRowRecordStatus(companyName, rowRecordStatus);

        if (companyOptional.isPresent()) {
            return ResponseEntity
                    .ok()
                    .body(companyOptional.get());
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Unable to find vehicle with registration number " + companyName));
        }
    }

}