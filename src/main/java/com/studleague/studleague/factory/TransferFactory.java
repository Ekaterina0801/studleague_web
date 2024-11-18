package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TransferDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferFactory implements DTOFactory<TransferDTO, Transfer>{


    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private PlayerFactory playerFactory;

    @Autowired
    private TeamFactory teamFactory;

    public Transfer mapToEntity(TransferDTO transferDTO) {
        if (transferDTO == null) {
            return null;
        }
        return Transfer.builder()
                .id(transferDTO.getId())
                .transferDate(transferDTO.getTransferDate())
                .comments(transferDTO.getComments())
                .player(playerFactory.mapToEntity(transferDTO.getPlayer()))
                .oldTeam(teamFactory.mapToEntity(transferDTO.getOldTeam()))
                .newTeam(teamFactory.mapToEntity(transferDTO.getNewTeam()))
                .build();
    }

    public TransferDTO mapToDto(Transfer transfer) {
        if (transfer == null) {
            return null;
        }

        TransferDTO.TransferDTOBuilder transferDTOBuilder = TransferDTO.builder()
                .id(transfer.getId())
                .transferDate(transfer.getTransferDate())
                .comments(transfer.getComments());

        if (transfer.getPlayer() != null) {
            transferDTOBuilder.player(playerFactory.mapToDto(transfer.getPlayer()));
        }
        if (transfer.getOldTeam() != null) {
            transferDTOBuilder.oldTeam(teamFactory.mapToDto(transfer.getOldTeam()));
        }
        if (transfer.getNewTeam() != null) {
            transferDTOBuilder.newTeam(teamFactory.mapToDto(transfer.getNewTeam()));
        }

        return transferDTOBuilder.build();
    }
}
