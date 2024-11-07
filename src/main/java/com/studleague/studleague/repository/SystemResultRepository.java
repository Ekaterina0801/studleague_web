package com.studleague.studleague.repository;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.SystemResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemResultRepository extends JpaRepository<SystemResult, Long> {

    Optional<SystemResult> findByNameIgnoreCase(String name);

}
