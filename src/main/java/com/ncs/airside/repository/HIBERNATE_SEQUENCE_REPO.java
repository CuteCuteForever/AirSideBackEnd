package com.ncs.airside.repository;

import com.ncs.airside.model.database.HIBERNATE_SEQUENCE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HIBERNATE_SEQUENCE_REPO extends JpaRepository<HIBERNATE_SEQUENCE, Long> {
}
