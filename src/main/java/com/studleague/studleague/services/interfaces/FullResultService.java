package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.ResultTableRow;
import com.studleague.studleague.entities.FullResult;

import java.util.HashMap;
import java.util.List;

public interface FullResultService {

    FullResult getFullResultById(int id);

    List<FullResult> getAllFullResults();

    void saveFullResult(FullResult fullResult);

    void updateFullResult(FullResult fullResult, String[] params);

    void deleteFullResult(int id);

    FullResult addControversialToResult(int result_id, int controversial_id);

    public void deleteControversialFromResult(int result_id, int controversial_id);

    public List<ResultTableRow> fullResultsToTable(int tournament_id, List<FullResult> fullResults);
}
