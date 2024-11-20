package com.studleague.studleague.repository;

import com.studleague.studleague.entities.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long>, JpaSpecificationExecutor<League> {

    boolean existsByNameIgnoreCase(String name);

    Optional<League> findByNameIgnoreCase(String name);

    List<League> findAllByCreatedById(Long userId);
}
