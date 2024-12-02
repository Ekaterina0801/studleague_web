package com.studleague.studleague.factory;

import com.studleague.studleague.dto.LeagueDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class LeagueFactory implements DTOFactory<LeagueDTO, League>{

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    public LeagueFactory() {
    }

    public League mapToEntity(LeagueDTO leagueDTO) {

        List<Team> teams = new ArrayList<>();
        List<Tournament> tournaments = new ArrayList<>();
        if (!leagueDTO.getTeamsIds().isEmpty()) {
            teams.addAll(
                    leagueDTO.getTeamsIds().stream()
                            .map(teamId -> entityRetrievalUtils.getTeamOrThrow(teamId))
                            .toList()
            );
        }

        if (!leagueDTO.getTournamentsIds().isEmpty()) {
            tournaments.addAll(
                    leagueDTO.getTournamentsIds().stream()
                            .map(tournamentId -> entityRetrievalUtils.getTournamentOrThrow(tournamentId))
                            .toList()
            );
        }


        return League.builder()
                .id(leagueDTO.getId())
                .name(leagueDTO.getName())
                .teams(teams)
                .countExcludedGames(leagueDTO.getCountExcludedGames())
                .systemResult(entityRetrievalUtils.getSystemResultOrThrow(leagueDTO.getSystemResultId()))
                .managers(leagueDTO.getManagersIds().stream().map(x->entityRetrievalUtils.getUserOrThrow(x)).toList())
                .tournaments(tournaments)
                .createdBy(entityRetrievalUtils.getUserOrThrow(leagueDTO.getCreatedById()))
                .build();
    }

    public LeagueDTO mapToDto(League league) {
        List<Long> teamIds = league.getTeams().stream().map(Team::getId).toList();
        List<Long> tournamentIds = league.getTournaments().stream().map(Tournament::getId).toList();
        return LeagueDTO.builder()
                .id(league.getId())
                .name(league.getName())
                .teamsIds(teamIds)
                .countExcludedGames(league.getCountExcludedGames())
                .createdById(league.getCreatedBy().getId())
                .managersIds(league.getManagers().stream().map(User::getId).toList())
                .systemResultId(league.getSystemResult().getId())
                .tournamentsIds(tournamentIds)
                .build();
    }
}
