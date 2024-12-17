package com.studleague.studleague.mappers.player;

import com.studleague.studleague.dto.player.PlayerMainInfoDTO;
import com.studleague.studleague.entities.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerMainInfoMapper {

    @Mapping(target = "transfers", ignore = true)
    @Mapping(target = "tournaments", ignore = true)
    @Mapping(target = "teamsCompositions", ignore = true)
    @Mapping(target = "teams", ignore = true)
    Player mapToEntity(PlayerMainInfoDTO playerDTO);

    PlayerMainInfoDTO mapToDto(Player player);
}
