package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentDao {
    Optional<Tournament> getTournamentById(int id);

    List<Tournament> getAllTournaments();

    void saveTournament(Tournament tournament);

    void updateTournament(Tournament tournament, String[] params);

    void deleteTournament(int id);

    Optional<Tournament> getTournamentBySiteId(String idSite);

    List<Tournament> tournamentsByTeam(int team_id);

}
