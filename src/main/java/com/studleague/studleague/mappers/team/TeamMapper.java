package com.studleague.studleague.mappers.team;

import com.studleague.studleague.dto.team.TeamDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.mappers.flag.FlagMapper;
import com.studleague.studleague.mappers.league.LeagueMainInfoMapper;
import com.studleague.studleague.mappers.player.PlayerMainInfoMapper;
import com.studleague.studleague.mappers.tournament.TournamentMainInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        PlayerMainInfoMapper.class,
        TournamentMainInfoMapper.class,
        LeagueMainInfoMapper.class,
        FlagMapper.class
})
public interface TeamMapper {


    @Mapping(target = "teamCompositions", ignore = true)
    @Mapping(target = "results", ignore = true)
    @Mapping(target = "players", source = "players")
    @Mapping(target = "tournaments", source = "tournaments")
    @Mapping(target = "league", source = "league")
    @Mapping(target = "flags", source = "flags")
    Team mapToEntity(TeamDTO teamDTO);

    @Mapping(target = "players", source = "players")
    @Mapping(target = "tournaments", source = "tournaments")
    @Mapping(target = "league", source = "league")
    @Mapping(target = "flags", source = "flags")
    TeamDTO mapToDto(Team team);
}
