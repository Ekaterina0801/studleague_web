package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDao {
    Optional<Team> findById(long id);

    List<Team> findAll();

    void save(Team team);

    void deleteById(long id);

    List<Team> findAllByLeagueId(long leagueId);

    Optional<Team> findByIdSite(long idSite);

    Optional<Team> findByPlayerIdAndLeagueId(long playerId, long leagueId);

    public boolean existsByIdSite(long idSite);

    void deleteAll();

}
