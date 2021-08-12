package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_TRANSPONDER;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RT_TRANSPONDER_REPO extends JpaRepository<RT_TRANSPONDER, Long> {
    Optional<RT_TRANSPONDER> findByEPCAndServiceAvailabilityAndRowRecordStatus(String epc , String serviceAvailability , String rowRecordStatus);
    Optional<RT_TRANSPONDER> findByCallSignAndServiceAvailabilityAndRowRecordStatus(String callSign , String serviceAvailability , String rowRecordStatus);
}

