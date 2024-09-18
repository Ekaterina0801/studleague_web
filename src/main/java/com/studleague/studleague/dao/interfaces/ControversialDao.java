package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Flag;

import java.util.HashMap;
import java.util.List;

public interface ControversialDao {

        Controversial getControversialById(int id);

        List<Controversial> getAllControversials();

        void saveControversial(Controversial controversial);

        void updateControversial(Controversial controversial, String[] params);

        void deleteControversial(int id);

        HashMap<Integer, Controversial> getControversialByTeamId(int team_id);

        HashMap<Integer, Controversial> getControversialByTournamentId(int team_id);

}
