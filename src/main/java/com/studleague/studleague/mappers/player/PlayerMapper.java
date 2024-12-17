package com.studleague.studleague.mappers.player;


import com.studleague.studleague.dto.player.PlayerDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.mappers.team.TeamMainInfoMapper;
import com.studleague.studleague.mappers.team.TeamMapper;
import com.studleague.studleague.mappers.teamComposition.TeamCompositionMapper;
import com.studleague.studleague.mappers.transfer.TransferMainInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        TeamCompositionMapper.class,
        TeamMapper.class,
        TeamMainInfoMapper.class,
        TransferMainInfoMapper.class,
})
public interface PlayerMapper {

    @Mapping(target = "tournaments", ignore = true)
    @Mapping(target = "transfers", source = "transfers")
    @Mapping(target = "teamsCompositions", source = "teamsCompositions")
    @Mapping(target = "teams", source = "teams")
    Player mapToEntity(PlayerDTO playerDTO);

    @Mapping(target = "transfers", source = "transfers")
    @Mapping(target = "teamsCompositions", source = "teamsCompositions")
    @Mapping(target = "teams", source = "teams")
    PlayerDTO mapToDto(Player player);
}
