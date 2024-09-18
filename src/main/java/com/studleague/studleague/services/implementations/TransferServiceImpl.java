package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.implementations.TransferDaoImpl;
import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.services.interfaces.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    private TransferDao transferDAO;

    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private TeamDao teamDao;

    @Override
    @Transactional
    public List<Transfer> getAllTransfers() {
        return transferDAO.getAllTransfers();
    }

    @Override
    @Transactional
    public void saveTransfer(Transfer transfer) {
        Team oldTeam = teamDao.getTeamById(transfer.getOldTeam().getId());
        Team newTeam = teamDao.getTeamById(transfer.getNewTeam().getId());
        Player player = transfer.getPlayer();
        if (oldTeam.getPlayers().contains(player)&&!newTeam.getPlayers().contains(player))
        {
            transferDAO.saveTransfer(transfer);
            oldTeam.deletePlayerFromTeam(player);
            newTeam.addPlayerToTeam(player);
        }


    }

    @Override
    @Transactional
    public Transfer getTransfer(int id) {
        Transfer transfer = transferDAO.getTransferById(id);
        return transfer;
    }

    @Override
    @Transactional
    public void updateTransfer(Transfer transfer, String[] params) {

    }

    @Override
    @Transactional
    public void deleteTransfer(int id) {
        transferDAO.deleteTransfer(id);
    }

    @Override
    @Transactional
    public List<Transfer> getTransfersForPlayer(int player_id) {
        List<Transfer> transfers = transferDAO.getTransfersForPlayer(player_id);
        return transfers;
    }

    @Override
    @Transactional
    public List<Transfer> getTransfersForTeam(int team_id) {
        List<Transfer> transfers = transferDAO.getTransfersForTeam(team_id);
        return transfers;
    }
}
