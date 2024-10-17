package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Flag;

import java.util.HashMap;
import java.util.List;

public interface ControversialService {
    List<Controversial> getAllControversials();

    void saveControversial(Controversial controversial);

    Controversial getControversialById(int id);


    void deleteControversial(int id);

    HashMap<Integer, Controversial> getControversialByTeamId(int team_id);

    HashMap<Integer, Controversial> getControversialByTournament(int tournament_id);

    List<Controversial> getControversialByTournamentList(int tournament_id);


}
