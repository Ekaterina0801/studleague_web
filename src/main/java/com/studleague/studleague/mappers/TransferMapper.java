package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.TransferDTO;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper implements DTOMapper<TransferDTO, Transfer> {


    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private TeamMapper teamMapper;

    public Transfer mapToEntity(TransferDTO transferDTO) {
        if (transferDTO == null) {
            return null;
        }
        return Transfer.builder()
                .id(transferDTO.getId())
                .transferDate(transferDTO.getTransferDate())
                .comments(transferDTO.getComments())
                .player(playerMapper.mapToEntity(transferDTO.getPlayer()))
                .oldTeam(teamMapper.mapToEntity(transferDTO.getOldTeam()))
                .newTeam(teamMapper.mapToEntity(transferDTO.getNewTeam()))
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
            transferDTOBuilder.player(playerMapper.mapToDto(transfer.getPlayer()));
        }
        if (transfer.getOldTeam() != null) {
            transferDTOBuilder.oldTeam(teamMapper.mapToDto(transfer.getOldTeam()));
        }
        if (transfer.getNewTeam() != null) {
            transferDTOBuilder.newTeam(teamMapper.mapToDto(transfer.getNewTeam()));
        }

        return transferDTOBuilder.build();
    }
}
