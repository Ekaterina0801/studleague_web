package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Controversial;

import java.util.HashMap;
import java.util.List;

public interface ControversialService {

    List<Controversial> getAllControversials();

    void saveControversial(Controversial controversial);

    Controversial getControversialById(Long id);

    void deleteControversial(Long id);

    HashMap<Integer, Controversial> getControversialsByTournamentIdWithQuestionNumber(Long tournamentId);

    List<Controversial> getControversialsByTournamentId(Long tournamentId);

    HashMap<Integer, Controversial> getControversialsByTeamIdWithQuestionNumber(Long teamId);

    List<Controversial> getControversialsByTeamId(Long teamId);

    void deleteAllControversials();

}
