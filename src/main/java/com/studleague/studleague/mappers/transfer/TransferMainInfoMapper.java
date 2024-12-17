package com.studleague.studleague.mappers.transfer;


import com.studleague.studleague.dto.transfer.TransferMainInfoDTO;
import com.studleague.studleague.entities.Transfer;
import com.studleague.studleague.mappers.player.PlayerMainInfoMapper;
import com.studleague.studleague.mappers.team.TeamMainInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {PlayerMainInfoMapper.class, TeamMainInfoMapper.class})
public interface TransferMainInfoMapper {


    @Mapping(target = "player", source = "player")
    @Mapping(target = "oldTeam", source = "oldTeam")
    @Mapping(target = "newTeam", source = "newTeam")
    Transfer mapToEntity(TransferMainInfoDTO transferDTO);


    @Mapping(target = "player", source = "player")
    @Mapping(target = "oldTeam", source = "oldTeam")
    @Mapping(target = "newTeam", source = "newTeam")
    TransferMainInfoDTO mapToDto(Transfer transfer);
}
