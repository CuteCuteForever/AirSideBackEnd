package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_TRANSPONDER;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RT_TRANSPONDER_REPO extends JpaRepository<RT_TRANSPONDER, Long> {
    Optional<RT_TRANSPONDER> findByEPCAndRowRecordStatus(String epc , String rowRecordStatus);
    Optional<RT_TRANSPONDER> findByCallSignAndRowRecordStatus(String callSign , String rowRecordStatus);
}

