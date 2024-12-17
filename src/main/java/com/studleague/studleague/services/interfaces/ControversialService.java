package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.controversial.ControversialDTO;
import com.studleague.studleague.entities.Controversial;

import java.time.LocalDateTime;
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

    boolean isManager(Long userId, Long controversialId);

    boolean isManager(Long userId, ControversialDTO controversialDTO);

    List<Controversial> searchControversials(Integer questionNumber, List<String> statuses, LocalDateTime startDate, LocalDateTime endDate, Long fullResultId, List<String> sortBy, List<String> sortOrder);

}
