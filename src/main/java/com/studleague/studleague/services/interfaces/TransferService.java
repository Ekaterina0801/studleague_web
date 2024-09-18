package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Transfer;

import java.util.List;

public interface TransferService {
    public List<Transfer> getAllTransfers();

    public void saveTransfer(Transfer transfer);

    public Transfer getTransfer(int id);

    public void updateTransfer(Transfer transfer, String[] params);

    public void deleteTransfer(int id);

    public List<Transfer> getTransfersForPlayer(int player_id);

    public List<Transfer> getTransfersForTeam(int team_id);
}
