package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_TRANSPONDER_STATUS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RT_TRANSPONDER_STATUS_REPO extends JpaRepository<RT_TRANSPONDER_STATUS, Long> {
    Optional<RT_TRANSPONDER_STATUS> findByEPCAndRowRecordStatus(String epc , String rowRecordStatus);
    Optional<RT_TRANSPONDER_STATUS> findByEPCAndTransponderStatusAndRowRecordStatus(String epc , String transponderStatus, String rowRecordStatus);
    Optional<RT_TRANSPONDER_STATUS> findByEPCAndTransponderStatusAndRowRecordStatusAndInTimestampIsNotNull(String epc , String transponderStatus, String rowRecordStatus);
}
