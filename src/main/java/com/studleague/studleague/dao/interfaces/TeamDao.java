package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Team;

import java.util.List;
import java.util.Optional;

public interface TeamDao {
    Optional<Team> getTeamById(long id);

    List<Team> getAllTeams();

    void saveTeam(Team team);

    void deleteTeam(long id);

    List<Team> teamsByLeague(long leagueId);

    Optional<Team> getTeamByIdSite(long idSite);

    Optional<Team> getTeamPlayerByLeague(long playerId, long leagueId);

}
