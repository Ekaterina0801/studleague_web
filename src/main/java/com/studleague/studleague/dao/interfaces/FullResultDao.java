package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.FullResult;

import java.util.List;

public interface FullResultDao {
    FullResult getFullResultById(int id);

    List<FullResult> getAllFullResults();

    void saveFullResult(FullResult fullResult);

    void updateFullResult(FullResult fullResult, String[] params);

    void deleteFullResult(int id);

}
