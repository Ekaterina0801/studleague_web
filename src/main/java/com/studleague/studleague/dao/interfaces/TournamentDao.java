package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentDao {
    Optional<Tournament> getTournamentById(long id);

    List<Tournament> getAllTournaments();

    void saveTournament(Tournament tournament);

    void deleteTournament(long id);

    Optional<Tournament> getTournamentBySiteId(long idSite);

    List<Tournament> tournamentsByTeam(long teamId);

    public boolean existsByIdSite(long idSite);

    void deleteAll();

}
