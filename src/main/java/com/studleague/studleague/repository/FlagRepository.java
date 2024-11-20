package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Flag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlagRepository extends JpaRepository<Flag, Long>, JpaSpecificationExecutor<Flag> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Flag> findByNameIgnoreCase(String name);


}
