package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.League;

import java.util.List;

public interface LeagueService {
    League getLeagueById(int id);

    List<League> getAllLeagues();

    void saveLeague(League league);

    void updateLeague(League league, String[] params);

    void deleteLeague(int id);

    public League addTournamentToLeague(int league_id, int tournament_id);

    public League deleteTournamentToLeague(int league_id, int tournament_id);
}
