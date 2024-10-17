package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.TournamentDto;
import com.studleague.studleague.entities.Tournament;

import java.util.List;

public interface TournamentService {
    Tournament getTournamentById(int id);

    List<Tournament> getAllTournaments();

    void saveTournament(Tournament tournament);

    void deleteTournament(int id);

    Tournament addResultToTournament(int tournament_id, int result_id);

    Tournament deleteResultFromTournament(int tournament_id, int result_id);

    Tournament addPlayerToTournament(int tournament_id, int player_id);

    Tournament deletePlayerFromTournament(int tournament_id, int player_id);

    Tournament deleteTeamFromTournament(int tournament_id, int team_id);

    Tournament addTeamToTournament(int tournament_id, int team_id);

    Tournament addTeamAndPlayerToTournament(int tournament_id, int team_id, int player_id);

    Tournament getTournamentBySiteId(String idSite);
}
