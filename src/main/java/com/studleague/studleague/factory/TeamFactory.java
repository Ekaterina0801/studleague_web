package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;



@Component
public class TeamFactory implements DTOFactory<TeamDTO, Team>{


    @Autowired
    @Lazy
    private PlayerFactory playerFactory;

    @Autowired
    private TournamentFactory tournamentFactory;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueFactory leagueFactory;

    public TeamFactory() {
    }

    public Team mapToEntity(TeamDTO teamDTO) {

        return Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .university(teamDTO.getUniversity())
                .idSite(teamDTO.getIdSite())
                .players(teamDTO.getPlayers().stream().map(x->playerFactory.mapToEntityWithoutTeams(x)).toList())
                .tournaments(teamDTO.getTournaments().stream().map(x->tournamentFactory.mapToEntity(x)).toList())
                .league(leagueFactory.mapToEntity(teamDTO.getLeague()))
                .build();
    }

    public TeamDTO mapToDto(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .university(team.getUniversity())
                .players(team.getPlayers().stream().map(x->playerFactory.mapToDtoWithoutTeams(x)).toList())
                .tournaments(team.getTournaments().stream().map(x->tournamentFactory.mapToDto(x)).toList())
                .league(leagueFactory.mapToDto(team.getLeague()))
                .idSite(team.getIdSite())
                .build();
    }

    public Team mapToEntityWithoutTournaments(TeamDTO teamDTO) {

        return Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .university(teamDTO.getUniversity())
                .idSite(teamDTO.getIdSite())
                .players(teamDTO.getPlayers().stream().map(x->playerFactory.mapToEntityWithoutTeams(x)).toList())
                .league(leagueFactory.mapToEntity(teamDTO.getLeague()))
                .build();
    }

    public TeamDTO mapToDtoWithoutTournaments(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .university(team.getUniversity())
                .players(team.getPlayers().stream().map(x->playerFactory.mapToDtoWithoutTeams(x)).toList())
                .league(leagueFactory.mapToDto(team.getLeague()))
                .idSite(team.getIdSite())
                .build();
    }
}

