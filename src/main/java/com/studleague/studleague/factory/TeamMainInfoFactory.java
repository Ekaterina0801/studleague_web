package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TeamMainInfoDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TeamMainInfoFactory implements DTOFactory<TeamMainInfoDTO, Team> {

    @Autowired
    @Lazy
    private PlayerFactory playerFactory;

    @Autowired
    private TournamentFactory tournamentFactory;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueFactory leagueFactory;

    @Autowired
    private FlagFactory flagFactory;

    public TeamMainInfoFactory() {
    }

    public Team mapToEntity(TeamMainInfoDTO teamDTO) {

        return Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .university(teamDTO.getUniversity())
                .idSite(teamDTO.getIdSite())
                .league(leagueFactory.mapToEntity(teamDTO.getLeague()))
                .tournaments(teamDTO.getTournamentIds().stream().map(x -> entityRetrievalUtils.getTournamentOrThrow(x)).collect(Collectors.toList()))
                .build();
    }

    public TeamMainInfoDTO mapToDto(Team team) {
        return TeamMainInfoDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .university(team.getUniversity())
                .league(leagueFactory.mapToDto(team.getLeague()))
                .tournamentIds(team.getTournaments().stream().map(Tournament::getId).collect(Collectors.toList()))
                .idSite(team.getIdSite())
                .build();
    }

}
