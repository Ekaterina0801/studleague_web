package com.studleague.studleague.mappers.team;

import com.studleague.studleague.dto.team.TeamCreationDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.mappers.MappingUtils;
import com.studleague.studleague.mappers.flag.FlagMapper;
import com.studleague.studleague.mappers.league.LeagueMapper;
import com.studleague.studleague.mappers.tournament.TournamentMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        LeagueMapper.class,
        TournamentMapper.class,
        FlagMapper.class,
        MappingUtils.class
})
public interface TeamCreationMapper {


    @Mapping(target = "teamCompositions", ignore = true)
    @Mapping(target = "results", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "flags", ignore = true)
    @Mapping(target = "league", source = "leagueId", qualifiedByName = "leagueIdToLeague")
    @Mapping(target = "tournaments", source = "tournamentIds", qualifiedByName = "tournamentIdsToTournaments")
    Team mapToEntity(TeamCreationDTO teamDTO);


    @Mapping(target = "leagueId", source = "league.id")
    @Mapping(target = "tournamentIds", source = "tournaments", qualifiedByName = "tournamentsToTournamentIds")
    TeamCreationDTO mapToDto(Team team);
}
