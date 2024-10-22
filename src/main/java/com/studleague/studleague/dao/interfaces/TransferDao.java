package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferDao {

    Optional<Transfer> findById(long id);

    List<Transfer> findAll();

    void save(Transfer transfer);

    void deleteById(long id);

    List<Transfer> findAllByPlayerId(long playerId);

    List<Transfer> findAllByTeamId(long teamId);

    void deleteAll();

}
