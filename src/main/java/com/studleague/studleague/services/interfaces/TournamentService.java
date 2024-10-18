package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.TournamentDto;
import com.studleague.studleague.entities.Tournament;

import java.util.List;

public interface TournamentService {
    Tournament getTournamentById(long id);

    List<Tournament> getAllTournaments();

    void saveTournament(Tournament tournament);

    void deleteTournament(long id);

    Tournament addResultToTournament(long tournamentId, long resultId);

    Tournament deleteResultFromTournament(long tournamentId, long resultId);

    Tournament addPlayerToTournament(long tournamentId, long playerId);

    Tournament deletePlayerFromTournament(long tournamentId, long playerId);

    Tournament deleteTeamFromTournament(long tournamentId, long teamId);

    Tournament addTeamToTournament(long tournamentId, long teamId);

    Tournament addTeamAndPlayerToTournament(long tournamentId, long teamId, long playerId);

    Tournament getTournamentBySiteId(long idSite);
}
