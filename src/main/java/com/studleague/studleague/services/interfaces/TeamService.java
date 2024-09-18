package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Team;

import java.util.List;

public interface TeamService {
    Team getTeamById(int id);

    List<Team> getAllTeams();

    void saveTeam(Team team);

    void updateTeam(Team team, String[] params);

    void deleteTeam(int id);

    List<Team> teamsByLeague(int league_id);

    public Team addPlayerToTeam(int team_id, int player_id);

    public Team deletePlayerFromTeam(int team_id, int player_id);

    public Team addFlagToTeam(int team_id, int flag_id);

    public Team deleteFlagFromTeam(int team_id, int flag_id);

    public Team addLeagueToTeam(int team_id, int league_id);

}
