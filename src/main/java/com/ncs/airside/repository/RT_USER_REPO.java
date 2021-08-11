package com.ncs.airside.repository;

import com.ncs.airside.model.account.RT_USER;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RT_USER_REPO extends JpaRepository<RT_USER, Long> {

    Optional<RT_USER> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
