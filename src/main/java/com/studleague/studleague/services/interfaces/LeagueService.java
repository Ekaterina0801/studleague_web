package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.LeagueDTO;
import com.studleague.studleague.entities.League;

import java.util.List;

public interface LeagueService {
    League getLeagueById(long id);

    List<League> getAllLeagues();

    void saveLeague(League league);

    void deleteLeague(long id);

    League addTournamentToLeague(long leagueId, long tournamentId);

    League deleteTournamentToLeague(long leagueId, long tournamentId);

    void deleteAllLeagues();
}
