package com.studleague.studleague.mappers.tournament;

import com.studleague.studleague.dto.tournament.TournamentDTO;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.mappers.MappingUtils;
import com.studleague.studleague.mappers.result.FullResultMapper;
import com.studleague.studleague.mappers.teamComposition.TeamCompositionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {FullResultMapper.class, TeamCompositionMapper.class, MappingUtils.class})
public interface TournamentMapper {


    @Mapping(target = "teams", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "leagues", source = "leaguesIds", qualifiedByName = "leaguesIdsToEntities")
    Tournament mapToEntity(TournamentDTO tournamentDto);


    @Mapping(target = "leaguesIds", source = "leagues", qualifiedByName = "leaguesToIds")
    TournamentDTO mapToDto(Tournament tournament);


}

