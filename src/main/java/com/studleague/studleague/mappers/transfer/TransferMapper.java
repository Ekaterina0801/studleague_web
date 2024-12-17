package com.studleague.studleague.mappers.transfer;

import com.studleague.studleague.dto.transfer.TransferDTO;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.mappers.player.PlayerMapper;
import com.studleague.studleague.mappers.team.TeamMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PlayerMapper.class, TeamMapper.class})
public interface TransferMapper {


    @Mapping(target = "player", source = "player")
    @Mapping(target = "oldTeam", source = "oldTeam")
    @Mapping(target = "newTeam", source = "newTeam")
    Transfer mapToEntity(TransferDTO transferDTO);


    @Mapping(target = "player", source = "player")
    @Mapping(target = "oldTeam", source = "oldTeam")
    @Mapping(target = "newTeam", source = "newTeam")
    TransferDTO mapToDto(Transfer transfer);
}
