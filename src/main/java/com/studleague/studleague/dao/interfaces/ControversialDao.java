package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Controversial;
import java.util.List;
import java.util.Optional;

public interface ControversialDao {

        Optional<Controversial> getControversialById(long id);

        List<Controversial> getAllControversials();

        void saveControversial(Controversial controversial);

        void deleteControversial(long id);

        List<Controversial> getControversialByTeamId(long teamId);

        List<Controversial> getControversialByTournamentId(long tournamentId);

}
