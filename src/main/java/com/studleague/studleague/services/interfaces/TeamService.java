package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.result.InfoTeamResults;
import com.studleague.studleague.dto.team.TeamDTO;
import com.studleague.studleague.entities.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TeamService {
    Team getTeamById(Long id);

    List<Team> getAllTeams();

    Team saveTeam(Team team);

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

    Page<Team> searchTeams(String name, Long leagueId, List<Long> flagIds, Sort sort, Pageable pageable);

}
