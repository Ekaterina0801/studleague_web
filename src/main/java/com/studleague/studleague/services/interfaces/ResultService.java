package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.LeagueResult;

import java.util.List;
import java.util.Map;

public interface ResultService {

    FullResult getFullResultById(Long id);

    List<FullResult> getAllFullResults();

    void saveFullResult(FullResult fullResult);

    void deleteFullResult(Long id);

    FullResult addControversialToResult(Long resultId, Long controversialId);

    void deleteControversialFromResult(Long resultId, Long controversialId);

    List<InfoTeamResults> fullResultsToTable(List<FullResult> fullResults);

    List<FullResult> getResultsForTeam(Long teamId);

    void deleteAllResults();

    boolean isManager(Long userId, Long resultId);

    boolean isManager(Long userId, FullResultDTO resultDTO);

    List<LeagueResult> calculateResultsBySystem(Long leagueId, String system);


}
