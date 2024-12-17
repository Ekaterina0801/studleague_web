package com.studleague.studleague.mappers.tournament;

import com.studleague.studleague.dto.tournament.TournamentMainInfoDTO;
import com.studleague.studleague.entities.Tournament;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TournamentMainInfoMapper {


    @Mapping(target = "teams", ignore = true)
    @Mapping(target = "teamCompositions", ignore = true)
    @Mapping(target = "results", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "leagues", ignore = true)
    Tournament mapToEntity(TournamentMainInfoDTO tournamentDto);


    TournamentMainInfoDTO mapToDto(Tournament tournament);
}
