package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.TeamMainInfoDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TeamMainInfoMapper implements DTOMapper<TeamMainInfoDTO, Team> {

    @Autowired
    @Lazy
    private PlayerMapper playerMapper;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueMapper leagueMapper;

    @Autowired
    private FlagMapper flagMapper;

    public TeamMainInfoMapper() {
    }

    public Team mapToEntity(TeamMainInfoDTO teamDTO) {

        return Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .university(teamDTO.getUniversity())
                .idSite(teamDTO.getIdSite())
                .league(leagueMapper.mapToEntity(teamDTO.getLeague()))
                .tournaments(teamDTO.getTournamentIds().stream().map(x -> entityRetrievalUtils.getTournamentOrThrow(x)).collect(Collectors.toList()))
                .build();
    }

    public TeamMainInfoDTO mapToDto(Team team) {
        return TeamMainInfoDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .university(team.getUniversity())
                .league(leagueMapper.mapToDto(team.getLeague()))
                .tournamentIds(team.getTournaments().stream().map(Tournament::getId).collect(Collectors.toList()))
                .idSite(team.getIdSite())
                .build();
    }

}
