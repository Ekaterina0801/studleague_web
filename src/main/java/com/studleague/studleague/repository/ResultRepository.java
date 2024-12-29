package com.studleague.studleague.repository;

import com.studleague.studleague.entities.FullResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<FullResult, Long>, JpaSpecificationExecutor<FullResult> {
    List<FullResult> findAllByTeamId(Long teamId);

    boolean existsByTeamIdAndTournamentId(Long teamId, Long tournamentId);

    Optional<FullResult> findByTeamIdAndTournamentId(Long teamId, Long tournamentId);


    @Query("SELECT fr FROM FullResult fr " +
            "JOIN fr.team t " +
            "JOIN t.league l " +
            "JOIN fr.tournament tor " +
            "WHERE tor.id = :tournamentId AND l.id = :leagueId")
    List<FullResult> findAllByTournamentIdAndLeagueId(@Param("tournamentId") Long tournamentId,
                                                      @Param("leagueId") Long leagueId);

}
