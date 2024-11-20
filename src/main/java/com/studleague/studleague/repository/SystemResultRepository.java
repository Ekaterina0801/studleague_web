package com.studleague.studleague.repository;

import com.studleague.studleague.entities.SystemResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemResultRepository extends JpaRepository<SystemResult, Long>, JpaSpecificationExecutor<SystemResult> {

    Optional<SystemResult> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

}
