package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.FullResult;

import java.util.List;
import java.util.Optional;

public interface ResultDao {
    Optional<FullResult> findById(Long id);

    List<FullResult> findAll();

    void save(FullResult fullResult);

    void deleteById(Long id);

    List<FullResult> findAllByTeamId(Long teamId);

    void deleteAll();
}
