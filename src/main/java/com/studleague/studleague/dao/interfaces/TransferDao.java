package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferDao {

    Optional<Transfer> findById(Long id);

    List<Transfer> findAll();

    void save(Transfer transfer);

    void deleteById(Long id);

    List<Transfer> findAllByPlayerId(Long playerId);

    List<Transfer> findAllByTeamId(Long teamId);

    void deleteAll();

}
