package com.studleague.studleague.mappers.flag;

import com.studleague.studleague.dto.flag.FlagDTO;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.mappers.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = MappingUtils.class)
public interface FlagMapper {


    @Mapping(target = "teams", source = "teamsIds", qualifiedByName = "teamIdsToTeams")
    @Mapping(target = "league", source = "leagueId", qualifiedByName = "leagueIdToLeague")
    Flag mapToEntity(FlagDTO flagDTO);


    @Mapping(target = "teamsIds", source = "teams", qualifiedByName = "teamsToTeamIds")
    @Mapping(target = "leagueId", source = "league.id")
    FlagDTO mapToDto(Flag flag);


}
