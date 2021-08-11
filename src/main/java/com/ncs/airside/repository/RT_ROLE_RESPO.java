package com.ncs.airside.repository;

import com.ncs.airside.model.account.ERole;
import com.ncs.airside.model.account.RT_ROLE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RT_ROLE_RESPO extends JpaRepository<RT_ROLE, Long> {
    Optional<RT_ROLE> findByName(ERole name);
}