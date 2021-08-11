package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_TRANSPONDER_BORROW;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RT_TRANSPONDER_BORROWER_REPO extends JpaRepository<RT_TRANSPONDER_BORROW, Long> {
    Optional<RT_TRANSPONDER_BORROW> findByEPCAndRowRecordStatus(String epc , String rowRecordStatus);
}
