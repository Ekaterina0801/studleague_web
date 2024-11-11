package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDao {
    Optional<Team> findById(Long id);

    List<Team> findAll();

    void save(Team team);

    void deleteById(Long id);

    List<Team> findAllByLeagueId(Long leagueId);

    Optional<Team> findByIdSite(Long idSite);

    Optional<Team> findByPlayerIdAndLeagueId(Long playerId, Long leagueId);

    boolean existsByIdSite(Long idSite);

    void deleteAll();

}
