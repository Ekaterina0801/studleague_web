package com.studleague.studleague.factory;

import com.studleague.studleague.dto.LeagueDTO;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class LeagueFactory {

    @Autowired
    LeagueRepository leagueRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    public LeagueFactory() {
    }

    public League toEntity(LeagueDTO leagueDTO) {

        List<Team> teams = new ArrayList<>();
        List<Tournament> tournaments = new ArrayList<>();
        if (!leagueDTO.getTeamIds().isEmpty()) {
            teams.addAll(
                    leagueDTO.getTeamIds().stream()
                            .map(teamId -> entityRetrievalUtils.getTeamOrThrow(teamId))
                            .toList()
            );
        }

        if (!leagueDTO.getTournamentIds().isEmpty()) {
            tournaments.addAll(
                    leagueDTO.getTournamentIds().stream()
                            .map(tournamentId -> entityRetrievalUtils.getTournamentOrThrow(tournamentId))
                            .toList()
            );
        }


        return League.builder()
                .id(leagueDTO.getId())
                .name(leagueDTO.getName())
                .teams(teams)
                .tournaments(tournaments)
                .build();
    }

    public LeagueDTO toDTO(League league) {
        List<Long> teamIds = league.getTeams().stream().map(Team::getId).toList();
        List<Long> tournamentIds = league.getTournaments().stream().map(Tournament::getId).toList();
        return LeagueDTO.builder()
                .id(league.getId())
                .name(league.getName())
                .teamIds(teamIds)
                .tournamentIds(tournamentIds)
                .build();
    }
}