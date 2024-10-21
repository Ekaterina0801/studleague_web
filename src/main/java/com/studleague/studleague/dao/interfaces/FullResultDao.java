package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.FullResult;

import java.util.List;
import java.util.Optional;

public interface FullResultDao {
    Optional<FullResult> getFullResultById(long id);

    List<FullResult> getAllFullResults();

    void saveFullResult(FullResult fullResult);

    void deleteFullResult(long id);

    List<FullResult> getResultsForTeam(long teamId);

    void deleteAll();
}
