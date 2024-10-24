package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;

import java.util.HashMap;
import java.util.List;

public interface TeamService {
    Team getTeamById(long id);

    List<Team> getAllTeams();

    void saveTeam(Team team);

    void deleteTeam(long id);

    List<Team> teamsByLeague(long leagueId);

    Team addPlayerToTeam(long teamId, long playerId);

    Team deletePlayerFromTeam(long teamId, long playerId);

    Team addFlagToTeam(long teamId, long flagId);

    Team deleteFlagFromTeam(long teamId, long flagId);

    Team addLeagueToTeam(long teamId, long leagueId);

    Team getTeamByIdSite(long idSite);

    HashMap<Tournament, List<Player>> getTournamentsPlayersByTeam(long teamId);

    List<InfoTeamResults> getInfoTeamResultsByTeam(long teamId);

    Team getTeamByPlayerIdAndLeagueId(long playerId, long leagueId);

    boolean existsByIdSite(long idSite);

    public void deleteAllTeams();
}
