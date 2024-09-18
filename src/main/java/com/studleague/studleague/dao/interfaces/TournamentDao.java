package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;

import java.util.List;

public interface TournamentDao {
    Tournament getTournamentById(int id);

    List<Tournament> getAllTournaments();

    void saveTournament(Tournament tournament);

    void updateTournament(Tournament tournament, String[] params);

    void deleteTournament(int id);


}
