package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_COMPANY;
import com.ncs.airside.model.database.RT_VEHICLE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RT_COMPANY_REPO extends JpaRepository<RT_COMPANY, Long> {
    Optional<RT_COMPANY> findByCompanyNameAndRowRecordStatus(String companyName , String rowRecordStatus);
    List<RT_COMPANY> findAllByRowRecordStatus(String rowRecordStatus);
    void deleteByCompanyName(String companyName);
    List<RT_COMPANY> findDistinctCompanyNameByRowRecordStatus(String rowRecordStatus);
}
