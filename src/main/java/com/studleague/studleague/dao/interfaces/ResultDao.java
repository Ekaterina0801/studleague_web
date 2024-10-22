package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.FullResult;

import java.util.List;
import java.util.Optional;

public interface ResultDao {
    Optional<FullResult> findById(long id);

    List<FullResult> findAll();

    void save(FullResult fullResult);

    void deleteById(long id);

    List<FullResult> findAllByTeamId(long teamId);

    void deleteAll();
}
