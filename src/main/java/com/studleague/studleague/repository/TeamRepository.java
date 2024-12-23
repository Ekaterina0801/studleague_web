package com.studleague.studleague.repository;

import com.studleague.studleague.entities.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>, JpaSpecificationExecutor<Team> {

    List<Team> findAllByLeagueId(Long leagueId);

    Optional<Team> findByIdSite(Long idSite);

    @Query("SELECT t FROM Player p " +
                       "JOIN p.teams t " +
                       "WHERE t.league.id = :leagueId AND p.id = :playerId")
    List<Team> findAllByPlayerIdAndLeagueId(@Param("playerId") Long playerId, @Param("leagueId")Long leagueId);

    boolean existsByIdSite(Long idSite);

    boolean existsByTeamNameIgnoreCaseAndLeagueId(String teamName, Long teamId);

    Optional<Team> findByTeamNameIgnoreCaseAndLeagueId(String teamName, Long leagueId);

    boolean existsByIdSiteAndLeagueId(Long idSite, Long leagueId);

    Page<Team> findAll(Specification<Team> specification, Pageable pageable);


}
