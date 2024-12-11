package com.studleague.studleague.mappers;


import com.studleague.studleague.dto.TransferMainInfoDTO;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferMainInfoMapper implements DTOMapper<TransferMainInfoDTO, Transfer> {


    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private PlayerMainInfoMapper playerMainInfoMapper;

    @Autowired
    private TeamMainInfoMapper teamMainInfoMapper;

    public Transfer mapToEntity(TransferMainInfoDTO transferDTO) {
        if (transferDTO == null) {
            return null;
        }
        return Transfer.builder()
                .id(transferDTO.getId())
                .transferDate(transferDTO.getTransferDate())
                .comments(transferDTO.getComments())
                .player(playerMainInfoMapper.mapToEntity(transferDTO.getPlayer()))
                .oldTeam(teamMainInfoMapper.mapToEntity(transferDTO.getOldTeam()))
                .newTeam(teamMainInfoMapper.mapToEntity(transferDTO.getNewTeam()))
                .build();
    }

    public TransferMainInfoDTO mapToDto(Transfer transfer) {
        if (transfer == null) {
            return null;
        }

        TransferMainInfoDTO.TransferMainInfoDTOBuilder transferDTOBuilder = TransferMainInfoDTO.builder()
                .id(transfer.getId())
                .transferDate(transfer.getTransferDate())
                .comments(transfer.getComments());

        if (transfer.getPlayer() != null) {
            transferDTOBuilder.player(playerMainInfoMapper.mapToDto(transfer.getPlayer()));
        }
        if (transfer.getOldTeam() != null) {
            transferDTOBuilder.oldTeam(teamMainInfoMapper.mapToDto(transfer.getOldTeam()));
        }
        if (transfer.getNewTeam() != null) {
            transferDTOBuilder.newTeam(teamMainInfoMapper.mapToDto(transfer.getNewTeam()));
        }

        return transferDTOBuilder.build();
    }
}
