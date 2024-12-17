package com.studleague.studleague.mappers.league;

import com.studleague.studleague.dto.league.LeagueDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.mappers.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = MappingUtils.class)
public interface LeagueMapper {


    @Mapping(target = "teams", source = "teamsIds", qualifiedByName = "teamIdsToTeams", ignore = true)
    @Mapping(target = "tournaments", source = "tournamentsIds", qualifiedByName = "tournamentIdsToTournaments")
    @Mapping(target = "managers", source = "managersIds", qualifiedByName = "userIdsToManagers")
    @Mapping(target = "systemResult", source = "systemResultId", qualifiedByName = "systemResultIdToSystemResult")
    @Mapping(target = "createdBy", source = "createdById", qualifiedByName = "userIdToUser")
    League mapToEntity(LeagueDTO leagueDTO);


    @Mapping(target = "teamsIds", source = "teams", qualifiedByName = "teamsToTeamIds")
    @Mapping(target = "tournamentsIds", source = "tournaments", qualifiedByName = "tournamentsToTournamentIds")
    @Mapping(target = "managersIds", source = "managers", qualifiedByName = "managersToUserIds")
    @Mapping(target = "systemResultId", source = "systemResult.id")
    @Mapping(target = "createdById", source = "createdBy.id")
    LeagueDTO mapToDto(League league);


}
