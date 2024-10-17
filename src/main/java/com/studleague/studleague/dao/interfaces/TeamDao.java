package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;

import java.util.List;
import java.util.Optional;

public interface TeamDao {
    Optional<Team> getTeamById(int id);

    List<Team> getAllTeams();

    void saveTeam(Team team);

    void deleteTeam(int id);

    List<Team> teamsByLeague(int league_id);

    Optional<Team> getTeamByIdSite(String idSite);

    Optional<Team> getTeamPlayerByLeague(int playerId, int leagueId);

}
