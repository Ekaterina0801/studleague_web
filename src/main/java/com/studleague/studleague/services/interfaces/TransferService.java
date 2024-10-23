package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Transfer;

import java.util.List;

public interface TransferService {
    List<Transfer> getAllTransfers();

    void saveTransfer(Transfer transfer);

    Transfer getTransfer(long id);

    void deleteTransfer(long id);

    List<Transfer> getTransfersForPlayer(long player_id);

    List<Transfer> getTransfersForTeam(long team_id);

    void deleteAllTransfers();
}
