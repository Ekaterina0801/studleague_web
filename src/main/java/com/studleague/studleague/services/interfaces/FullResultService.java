package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.FullResult;

import java.util.HashMap;
import java.util.List;

public interface FullResultService {

    FullResult getFullResultById(int id);

    List<FullResult> getAllFullResults();

    void saveFullResult(FullResult fullResult);


    void deleteFullResult(int id);

    FullResult addControversialToResult(int result_id, int controversial_id);

    void deleteControversialFromResult(int result_id, int controversial_id);

    List<InfoTeamResults> fullResultsToTable(List<FullResult> fullResults);

    List<FullResult> getResultsForTeam(int team_id);
}
