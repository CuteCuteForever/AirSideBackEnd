package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_EPC_ACTIVE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RT_EPC_ACTIVE_REPO extends JpaRepository<RT_EPC_ACTIVE, Long> {
    Optional<RT_EPC_ACTIVE> findByEPCAndRowRecordStatus(String epc , String rowRecordStatus);
    Optional<RT_EPC_ACTIVE> findByEPCAndAntennaNumberAndRowRecordStatus(String epc , int antennaNumber, String rowRecordStatus);
}
