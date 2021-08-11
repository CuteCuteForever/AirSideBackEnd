package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_BILLING;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_BILLING_REPO extends JpaRepository<RT_BILLING, Long> {
}
