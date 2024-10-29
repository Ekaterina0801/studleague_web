package com.studleague.studleague.repository;

import com.studleague.studleague.entities.FullResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<FullResult,Long> {
    List<FullResult> findAllByTeamId(Long teamId);

    boolean existsByTeamIdAndTournamentId(Long teamId, Long tournamentId);

    Optional<FullResult> findByTeamIdAndTournamentId(Long teamId, Long tournamentId);
}
