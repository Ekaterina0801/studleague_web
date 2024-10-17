package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.FullResult;

import java.util.List;
import java.util.Optional;

public interface FullResultDao {
    Optional<FullResult> getFullResultById(int id);

    List<FullResult> getAllFullResults();

    void saveFullResult(FullResult fullResult);

    void deleteFullResult(int id);

    List<FullResult> getResultsForTeam(int id);
}
