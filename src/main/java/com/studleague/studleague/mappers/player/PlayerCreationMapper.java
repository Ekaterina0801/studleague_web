package com.studleague.studleague.mappers.player;


import com.studleague.studleague.dto.player.PlayerCreationDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.mappers.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = MappingUtils.class)
public interface PlayerCreationMapper {


    @Mapping(target = "transfers", ignore = true)
    @Mapping(target = "tournaments", ignore = true)
    @Mapping(target = "teamsCompositions", ignore = true)
    @Mapping(target = "teams", source = "teamIds", qualifiedByName = "teamIdsToTeams")
    Player mapToEntity(PlayerCreationDTO playerDTO);


    @Mapping(target = "teamIds", source = "teams", qualifiedByName = "teamsToTeamIds")
    PlayerCreationDTO mapToDto(Player player);


}
