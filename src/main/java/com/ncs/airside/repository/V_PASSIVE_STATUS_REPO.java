package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_EPC_ACTIVE;
import com.ncs.airside.model.view.V_ACTIVE_STATUS;
import com.ncs.airside.model.view.V_PASSIVE_STATUS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface V_PASSIVE_STATUS_REPO extends JpaRepository<V_PASSIVE_STATUS, Long> {
    Optional<RT_EPC_ACTIVE> findByEPC(String epc);
    Optional<RT_EPC_ACTIVE> findByEPCAndAntennaNumber(String epc , int antennaNumber );
}