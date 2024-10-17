package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Transfer;

import java.util.List;

public interface TransferService {
    List<Transfer> getAllTransfers();

    void saveTransfer(Transfer transfer);

    Transfer getTransfer(int id);

    void updateTransfer(Transfer transfer, String[] params);

    void deleteTransfer(int id);

    List<Transfer> getTransfersForPlayer(int player_id);

    List<Transfer> getTransfersForTeam(int team_id);
}
