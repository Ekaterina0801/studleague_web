package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private TeamDao teamDao;

    @Override
    @Transactional
    public List<Transfer> getAllTransfers() {
        return transferDao.getAllTransfers();
    }

    @Override
    @Transactional
    public void saveTransfer(Transfer transfer) {
        long oldTeamId = transfer.getOldTeam().getId();
        long newTeamId = transfer.getNewTeam().getId();
        Team oldTeam = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(oldTeamId), "Team", oldTeamId);
        Team newTeam = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(newTeamId), "Team", newTeamId);
        Player player = transfer.getPlayer();
        if (oldTeam.getPlayers().contains(player) && !newTeam.getPlayers().contains(player)) {
            transferDao.saveTransfer(transfer);
            oldTeam.deletePlayerFromTeam(player);
            newTeam.addPlayerToTeam(player);
        }


    }

    @Override
    @Transactional
    public Transfer getTransfer(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(transferDao.getTransferById(id), "Transfer", id);
    }

    @Override
    @Transactional
    public void deleteTransfer(long id) {
        transferDao.deleteTransfer(id);
    }

    @Override
    @Transactional
    public List<Transfer> getTransfersForPlayer(long playerId) {
        return transferDao.getTransfersForPlayer(playerId);
    }

    @Override
    @Transactional
    public List<Transfer> getTransfersForTeam(long teamId) {
        return transferDao.getTransfersForTeam(teamId);
    }
}
