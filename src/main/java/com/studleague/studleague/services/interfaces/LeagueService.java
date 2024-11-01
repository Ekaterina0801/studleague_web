package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.LeagueDTO;
import com.studleague.studleague.entities.League;

import java.util.List;

public interface LeagueService {
    League getLeagueById(Long id);

    List<League> getAllLeagues();

    void saveLeague(League league);

    void deleteLeague(Long id);

    League addTournamentToLeague(Long leagueId, Long tournamentId);

    League deleteTournamentToLeague(Long leagueId, Long tournamentId);

    void deleteAllLeagues();

    List<League> getLeaguesForCurrentUser();

    boolean isManager(Long userId, Long leagueId);

    boolean isManager(Long userId, LeagueDTO leagueDTO);
}
