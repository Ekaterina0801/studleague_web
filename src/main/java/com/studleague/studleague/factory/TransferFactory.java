package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TransferDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferFactory {

    @Autowired
    PlayerRepository playerDao;

    @Autowired
    TeamRepository teamDao;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    public Transfer toEntity(TransferDTO transferDTO) {
        if (transferDTO == null) {
            return null;
        }
        Team oldTeam = entityRetrievalUtils.getTeamOrThrow(transferDTO.getOldTeamId());
        Team newTeam = entityRetrievalUtils.getTeamOrThrow(transferDTO.getNewTeamId());
        Player player = entityRetrievalUtils.getPlayerOrThrow(transferDTO.getPlayerId());

        return Transfer.builder()
                .id(transferDTO.getId())
                .transferDate(transferDTO.getTransferDate())
                .comments(transferDTO.getComments())
                .player(player)
                .oldTeam(oldTeam)
                .newTeam(newTeam)
                .build();
    }

    public TransferDTO toDTO(Transfer transfer) {
        if (transfer == null) {
            return null;
        }

        TransferDTO.TransferDTOBuilder transferDTOBuilder = TransferDTO.builder()
                .id(transfer.getId())
                .transferDate(transfer.getTransferDate())
                .comments(transfer.getComments());

        if (transfer.getPlayer() != null) {
            transferDTOBuilder.playerId(transfer.getPlayer().getId());
        }
        if (transfer.getOldTeam() != null) {
            transferDTOBuilder.oldTeamId(transfer.getOldTeam().getId());
        }
        if (transfer.getNewTeam() != null) {
            transferDTOBuilder.newTeamId(transfer.getNewTeam().getId());
        }

        return transferDTOBuilder.build();
    }
}
