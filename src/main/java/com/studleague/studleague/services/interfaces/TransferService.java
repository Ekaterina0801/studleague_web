package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Transfer;

import java.util.List;

public interface TransferService {
    List<Transfer> getAllTransfers();

    void saveTransfer(Transfer transfer);

    Transfer getTransfer(Long id);

    void deleteTransfer(Long id);

    List<Transfer> getTransfersForPlayer(Long player_id);

    List<Transfer> getTransfersForTeam(Long team_id);

    void deleteAllTransfers();
}
