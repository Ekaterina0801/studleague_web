package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Team;

import java.util.List;

public interface TeamDao {
    Team getTeamById(int id);

    List<Team> getAllTeams();

    void saveTeam(Team team);

    void updateTeam(Team team, String[] params);

    void deleteTeam(int id);

    List<Team> teamsByLeague(int league_id);

    List<Team> tournamentsByTeam(int team_id);
}
