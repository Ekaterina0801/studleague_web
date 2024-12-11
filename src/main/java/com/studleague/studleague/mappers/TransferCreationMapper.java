package com.studleague.studleague.mappers;


import com.studleague.studleague.dto.TransferCreationDTO;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferCreationMapper implements DTOMapper<TransferCreationDTO, Transfer> {


    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private TeamMapper teamMapper;

    public Transfer mapToEntity(TransferCreationDTO transferDTO) {
        if (transferDTO == null) {
            return null;
        }
        return Transfer.builder()
                .id(transferDTO.getId())
                .transferDate(transferDTO.getTransferDate())
                .comments(transferDTO.getComments())
                .player(entityRetrievalUtils.getPlayerOrThrow(transferDTO.getPlayerId()))
                .oldTeam(entityRetrievalUtils.getTeamOrThrow(transferDTO.getOldTeamId()))
                .newTeam(entityRetrievalUtils.getTeamOrThrow(transferDTO.getNewTeamId()))
                .build();
    }

    public TransferCreationDTO mapToDto(Transfer transfer) {
        if (transfer == null) {
            return null;
        }

        TransferCreationDTO.TransferCreationDTOBuilder transferDTOBuilder = TransferCreationDTO.builder()
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
