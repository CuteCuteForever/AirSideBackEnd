package com.ncs.airside.repository;

import com.ncs.airside.model.view.V_BORROW_RETURN_STATUS;
import com.ncs.airside.model.view.V_VEHICLE_COMPANY;
import org.springframework.data.jpa.repository.JpaRepository;

public interface V_BORROW_RETURN_STATUS_REPO extends JpaRepository<V_BORROW_RETURN_STATUS, Long> {
}