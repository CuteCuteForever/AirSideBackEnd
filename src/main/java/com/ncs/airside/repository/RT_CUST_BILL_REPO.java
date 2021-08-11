package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_CUST_BILLING;
import com.ncs.airside.model.database.RT_TRANSPONDER;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RT_CUST_BILL_REPO extends JpaRepository<RT_CUST_BILLING, Long> {
}

