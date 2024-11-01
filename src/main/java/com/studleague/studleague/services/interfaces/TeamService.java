package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;

import java.util.HashMap;
import java.util.List;

public interface TeamService {
    Team getTeamById(Long id);

    List<Team> getAllTeams();

    void saveTeam(Team team);

    void deleteTeam(Long id);

    List<Team> teamsByLeague(Long leagueId);

    Team addPlayerToTeam(Long teamId, Long playerId);

    Team deletePlayerFromTeam(Long teamId, Long playerId);

    Team addFlagToTeam(Long teamId, Long flagId);

    Team deleteFlagFromTeam(Long teamId, Long flagId);

    Team addLeagueToTeam(Long teamId, Long leagueId);

    Team getTeamByIdSite(Long idSite);

    List<InfoTeamResults> getInfoTeamResultsByTeam(Long teamId);

    List<Team> getTeamsByPlayerIdAndLeagueId(Long playerId, Long leagueId);

    boolean existsByIdSite(Long idSite);

    void deleteAllTeams();

    List<Team> getTeamsByFlagId(Long flagId);

    List<Team> getTeamsByPlayerId(Long playerId);

    boolean isManager(Long userId, Long teamId);

    boolean isManager(Long userId, TeamDTO teamDTO);

}
