package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.FullResult;

import java.util.List;

public interface ResultService {

    FullResult getFullResultById(Long id);

    List<FullResult> getAllFullResults();

    void saveFullResult(FullResult fullResult);

    void deleteFullResult(Long id);

    FullResult addControversialToResult(Long resultId, Long controversialId);

    void deleteControversialFromResult(Long resultId, Long controversialId);

    List<InfoTeamResults> fullResultsToTable(List<FullResult> fullResults);

    List<FullResult> getResultsForTeam(Long teamId);

    public void deleteAllResults();
}
