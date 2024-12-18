package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.league.LeagueDTO;
import com.studleague.studleague.entities.League;

import java.util.List;

public interface LeagueService {
    League getLeagueById(Long id);

    List<League> getAllLeagues();

    void saveLeague(League league);

    void deleteLeague(Long id);

    League addTournamentToLeague(Long leagueId, Long tournamentId);

    League deleteTournamentFromLeague(Long leagueId, Long tournamentId);

    void deleteAllLeagues();

    List<League> getLeaguesForCurrentUser();

    boolean isManager(Long userId, Long leagueId);

    boolean isManager(Long userId, LeagueDTO leagueDTO);

    League getLeagueWithResults(Long leagueId);

    League changeSystemResultOfLeague(Long leagueId, Long systemResultId);

    League deleteTeamFromLeague(Long leagueId, Long teamId);

    void addManager(Long leagueId, Long managerId);

    void deleteManager(Long leagueId, Long managerId);
}
