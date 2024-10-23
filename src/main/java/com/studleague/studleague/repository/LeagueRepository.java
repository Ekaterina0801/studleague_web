package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<League> findByNameIgnoreCase(String name);
}
