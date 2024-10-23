package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.FullResult;

import java.util.List;

public interface ResultService {

    FullResult getFullResultById(long id);

    List<FullResult> getAllFullResults();

    void saveFullResult(FullResult fullResult);

    void deleteFullResult(long id);

    FullResult addControversialToResult(long resultId, long controversialId);

    void deleteControversialFromResult(long resultId, long controversialId);

    List<InfoTeamResults> fullResultsToTable(List<FullResult> fullResults);

    List<FullResult> getResultsForTeam(long teamId);

    public void deleteAllResults();
}
