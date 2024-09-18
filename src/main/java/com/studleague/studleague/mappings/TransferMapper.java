package com.studleague.studleague.mappings;

import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dto.TransferDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper{
    public TransferMapper() {
    }

    @Autowired
    PlayerDao playerDao;
    @Autowired
    TeamDao teamDao;

    public Transfer toEntity(TransferDTO transferDTO) {
        if (transferDTO == null) {
            return null;
        }

        Transfer transfer = new Transfer();
        transfer.setId(transferDTO.getId());
        transfer.setTransferDate(transferDTO.getTransferDate());
        transfer.setComments(transferDTO.getComments());

        Player player = playerDao.getPlayerById(transferDTO.getPlayerId());
        transfer.setPlayer(player);

        Team oldTeam = teamDao.getTeamById(transferDTO.getOldTeamId());
        transfer.setOldTeam(oldTeam);

        Team newTeam = teamDao.getTeamById(transferDTO.getNewTeamId());
        transfer.setNewTeam(newTeam);

        return transfer;
    }


    public TransferDTO toDTO(Transfer transfer) {
        if (transfer == null) {
            return null;
        }

        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setId(transfer.getId());
        transferDTO.setTransferDate(transfer.getTransferDate());
        transferDTO.setComments(transfer.getComments());

        if (transfer.getPlayer() != null) {
            transferDTO.setPlayerId(transfer.getPlayer().getId());
        }
        if (transfer.getOldTeam() != null) {
            transferDTO.setOldTeamId(transfer.getOldTeam().getId());
        }
        if (transfer.getNewTeam() != null) {
            transferDTO.setNewTeamId(transfer.getNewTeam().getId());
        }

        return transferDTO;
    }
}

