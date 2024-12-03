package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class TeamFactory implements DTOFactory<TeamDTO, Team>{

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueFactory leagueFactory;

    @Autowired
    private FlagFactory flagFactory;

    @Autowired
    private PlayerMainInfoFactory playerMainInfoFactory;

    @Autowired
    private TournamentMainInfoFactory tournamentMainInfoFactory;

    @Autowired
    private LeagueMainInfoFactory leagueMainInfoFactory;

    public TeamFactory() {
    }

    public Team mapToEntity(TeamDTO teamDTO) {

        return Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .university(teamDTO.getUniversity())
                .idSite(teamDTO.getIdSite())
                .players(teamDTO.getPlayers().stream().map(x -> playerMainInfoFactory.mapToEntity(x)).collect(Collectors.toList()))
                .tournaments(teamDTO.getTournaments().stream().map(x -> tournamentMainInfoFactory.mapToEntity(x)).collect(Collectors.toList()))
                .league(leagueMainInfoFactory.mapToEntity(teamDTO.getLeague()))
                .flags(teamDTO.getFlags().stream().map(x -> flagFactory.mapToEntity(x)).collect(Collectors.toList()))
                .build();
    }

    public TeamDTO mapToDto(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .university(team.getUniversity())
                .players(team.getPlayers().stream().map(x -> playerMainInfoFactory.mapToDto(x)).collect(Collectors.toList()))
                .tournaments(team.getTournaments().stream().map(x -> tournamentMainInfoFactory.mapToDto(x)).collect(Collectors.toList()))
                .league(leagueMainInfoFactory.mapToDto(team.getLeague()))
                .idSite(team.getIdSite())
                .flags(team.getFlags().stream().map(x -> flagFactory.mapToDto(x)).collect(Collectors.toList()))
                .build();
    }


}

