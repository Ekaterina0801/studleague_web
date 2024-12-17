package com.studleague.studleague.mappers.result;

import com.studleague.studleague.dto.result.LeagueResult;
import com.studleague.studleague.dto.result.LeagueResultsDTO;
import com.studleague.studleague.mappers.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = MappingUtils.class)
public interface LeagueResultsMapper {

    @Mapping(target = "team", source = "teamId", qualifiedByName = "teamIdToTeam")
    LeagueResult mapToEntity(LeagueResultsDTO dto);

    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamName", source = "team.teamName")
    LeagueResultsDTO mapToDto(LeagueResult entity);


}
