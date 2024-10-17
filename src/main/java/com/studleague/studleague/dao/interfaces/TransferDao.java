package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransferDao {

    Optional<Transfer> getTransferById(int id);

    List<Transfer> getAllTransfers();

    void saveTransfer(Transfer t);

    void deleteTransfer(int id);

    List<Transfer> getTransfersForPlayer(int player_id);

    List<Transfer> getTransfersForTeam(int team_id);

}
