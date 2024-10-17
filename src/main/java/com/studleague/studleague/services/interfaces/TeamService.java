package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;

import java.util.HashMap;
import java.util.List;

public interface TeamService {
    Team getTeamById(int id);

    List<Team> getAllTeams();

    void saveTeam(Team team);


    void deleteTeam(int id);

    List<Team> teamsByLeague(int league_id);

    Team addPlayerToTeam(int team_id, int player_id);

    Team deletePlayerFromTeam(int team_id, int player_id);

    Team addFlagToTeam(int team_id, int flag_id);

    Team deleteFlagFromTeam(int team_id, int flag_id);

    Team addLeagueToTeam(int team_id, int league_id);

    Team getTeamByIdSite(String idSite);

    HashMap<Tournament, List<Player>> getTournamentsPlayersByTeam(int team_id);

    List<InfoTeamResults> getInfoTeamResultsByTeam(int team_id);
}
