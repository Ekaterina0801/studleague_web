package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Flag;

import java.util.HashMap;
import java.util.List;

public interface ControversialService {
    public List<Controversial> getAllControversials();

    public void saveControversial(Controversial controversial);

    public Controversial getControversialById(int id);

    public void updateControversial(Controversial controversial, String[] params);

    public void deleteControversial(int id);

    public HashMap<Integer, Controversial> getControversialByTeamId(int team_id);

    public HashMap<Integer, Controversial> getControversialByTournament(int tournament_id);

    public List<Controversial> getControversialByTournamentList(int tournament_id);


}
