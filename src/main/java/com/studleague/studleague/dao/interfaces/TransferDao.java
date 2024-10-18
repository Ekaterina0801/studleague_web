package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferDao {

    Optional<Transfer> getTransferById(long id);

    List<Transfer> getAllTransfers();

    void saveTransfer(Transfer transfer);

    void deleteTransfer(long id);

    List<Transfer> getTransfersForPlayer(long playerId);

    List<Transfer> getTransfersForTeam(long teamId);

}
