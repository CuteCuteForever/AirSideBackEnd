package com.ncs.airside.repository;

import com.ncs.airside.model.database.RT_VEHICLE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RT_VEHICLE_REPO extends JpaRepository<RT_VEHICLE, Integer> {
    Optional<RT_VEHICLE> findByRegistrationNumberAndRowRecordStatus(String registrationNumber, String rowRecordStatus);
    List<RT_VEHICLE> findByCompanyIdAndRowRecordStatus(Long companyId, String rowRecordStatus);
    List<RT_VEHICLE> findByVehicleIdAndRowRecordStatus(Long vehicleId, String rowRecordStatus);
    Optional<RT_VEHICLE> findByVehicleRowIdAndRowRecordStatus(Long vehicleRowId, String rowRecordStatus);
}
