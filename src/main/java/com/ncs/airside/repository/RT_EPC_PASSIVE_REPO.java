package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_EPC_ACTIVE;
import com.ncs.airside.model.database.RT_EPC_PASSIVE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RT_EPC_PASSIVE_REPO extends JpaRepository<RT_EPC_PASSIVE, Long> {
    Optional<RT_EPC_PASSIVE> findByEPCAndRowRecordStatus(String epc , String rowRecordStatus);
}