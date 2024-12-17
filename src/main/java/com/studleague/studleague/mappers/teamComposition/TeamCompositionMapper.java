package com.studleague.studleague.mappers.teamComposition;

import com.studleague.studleague.dto.teamComposition.TeamCompositionDTO;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.mappers.player.PlayerMainInfoMapper;
import com.studleague.studleague.mappers.team.TeamMainInfoMapper;
import com.studleague.studleague.mappers.tournament.TournamentMainInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        TournamentMainInfoMapper.class,
        TeamMainInfoMapper.class,
        PlayerMainInfoMapper.class
})
public interface TeamCompositionMapper {


    @Mapping(target = "parentTeam", source = "parentTeam")
    @Mapping(target = "tournament", source = "tournament")
    @Mapping(target = "players", source = "players")
    TeamCompositionDTO mapToDto(TeamComposition teamComposition);


    @Mapping(target = "parentTeam", source = "parentTeam")
    @Mapping(target = "players", source = "players")
    @Mapping(target = "tournament", source = "tournament")
    TeamComposition mapToEntity(TeamCompositionDTO teamCompositionDTO);
}
