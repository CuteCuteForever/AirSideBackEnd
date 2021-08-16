package com.ncs.airside.repository;

import com.ncs.airside.model.view.V_VEHICLE_COMPANY;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface V_VEHICLE_COMPANY_REPO extends JpaRepository<V_VEHICLE_COMPANY, Long> {

    Optional<V_VEHICLE_COMPANY> findByCompanyId(Long companyId );
}