package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAllByLeagueId(long leagueId);

    Optional<Team> findByIdSite(long idSite);

    @Query("SELECT t FROM Player p " +
                       "JOIN p.teams t " +
                       "WHERE t.league.id = :leagueId AND p.id = :playerId")
    Optional<Team> findByPlayerIdAndLeagueId(@Param("playerId") long playerId, @Param("leagueId")long leagueId);

    boolean existsByIdSite(long idSite);
}
