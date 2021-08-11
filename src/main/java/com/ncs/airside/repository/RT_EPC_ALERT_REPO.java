package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_EPC_ALERT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RT_EPC_ALERT_REPO extends JpaRepository<RT_EPC_ALERT, Long> {
    Optional<RT_EPC_ALERT> findByEpcAndRowRecordStatus(String epc , String rowRecordStatus);
}
