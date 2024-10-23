package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TransferRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    @Autowired
    //private TransferDao transferDao;
    private TransferRepository transferRepository;

    @Autowired
    //private PlayerDao playerDao;
    private PlayerRepository playerRepository;

    @Autowired
    //private TeamDao teamDao;
    private TeamRepository teamRepository;

    @Override
    @Transactional
    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }

    @Override
    @Transactional
    public void saveTransfer(Transfer transfer) {
        long oldTeamId = transfer.getOldTeam().getId();
        long newTeamId = transfer.getNewTeam().getId();
        Team oldTeam = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(oldTeamId), "Team", oldTeamId);
        Team newTeam = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(newTeamId), "Team", newTeamId);
        Player player = transfer.getPlayer();
        if (oldTeam.getPlayers().contains(player) && !newTeam.getPlayers().contains(player)) {
            transferRepository.save(transfer);
            oldTeam.deletePlayerFromTeam(player);
            newTeam.addPlayerToTeam(player);
        }


    }

    @Override
    @Transactional
    public Transfer getTransfer(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(transferRepository.findById(id), "Transfer", id);
    }

    @Override
    @Transactional
    public void deleteTransfer(long id) {
        transferRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Transfer> getTransfersForPlayer(long playerId) {
        return transferRepository.findAllByPlayerId(playerId);
    }

    @Override
    @Transactional
    public List<Transfer> getTransfersForTeam(long teamId) {
        return transferRepository.findAllByTeamId(teamId);
    }

    @Override
    @Transactional
    public void deleteAllTransfers()
    {
        transferRepository.deleteAll();
    }
}
