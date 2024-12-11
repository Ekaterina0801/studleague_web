package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class TeamMapper implements DTOMapper<TeamDTO, Team> {

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueMapper leagueMapper;

    @Autowired
    private FlagMapper flagMapper;

    @Autowired
    private PlayerMainInfoMapper playerMainInfoMapper;

    @Autowired
    private TournamentMainInfoMapper tournamentMainInfoMapper;

    @Autowired
    private LeagueMainInfoMapper leagueMainInfoMapper;

    public TeamMapper() {
    }

    public Team mapToEntity(TeamDTO teamDTO) {

        return Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .university(teamDTO.getUniversity())
                .idSite(teamDTO.getIdSite())
                .players(teamDTO.getPlayers().stream().map(x -> playerMainInfoMapper.mapToEntity(x)).collect(Collectors.toList()))
                .tournaments(teamDTO.getTournaments().stream().map(x -> tournamentMainInfoMapper.mapToEntity(x)).collect(Collectors.toList()))
                .league(leagueMainInfoMapper.mapToEntity(teamDTO.getLeague()))
                .flags(teamDTO.getFlags().stream().map(x -> flagMapper.mapToEntity(x)).collect(Collectors.toList()))
                .build();
    }

    public TeamDTO mapToDto(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .university(team.getUniversity())
                .players(team.getPlayers().stream().map(x -> playerMainInfoMapper.mapToDto(x)).collect(Collectors.toList()))
                .tournaments(team.getTournaments().stream().map(x -> tournamentMainInfoMapper.mapToDto(x)).collect(Collectors.toList()))
                .league(leagueMainInfoMapper.mapToDto(team.getLeague()))
                .idSite(team.getIdSite())
                .flags(team.getFlags().stream().map(x -> flagMapper.mapToDto(x)).collect(Collectors.toList()))
                .build();
    }


}

