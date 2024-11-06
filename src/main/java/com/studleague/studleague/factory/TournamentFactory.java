package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TournamentDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TournamentFactory {

    @Autowired
    private LeagueRepository leagueDao;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    public Tournament toEntity(TournamentDTO tournamentDto) {
        List<Player> players = new ArrayList<>();
        List<Team> teams = new ArrayList<>();
        for (Long playerId: tournamentDto.getPlayerIds())
        {
            Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);
            players.add(player);
        }
        for (Long teamId: tournamentDto.getTeamIds())
        {
            Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
            teams.add(team);
        }
        Tournament tournament = Tournament.builder()
                .id(tournamentDto.getId())
                .name(tournamentDto.getName())
                .idSite(tournamentDto.getIdSite())
                .players(players)
                .teams(teams)
                .dateOfStart(tournamentDto.getDateOfStart())
                .dateOfEnd(tournamentDto.getDateOfEnd())
                .leagues(new ArrayList<>())
                .build();

        for (long id : tournamentDto.getLeagueIds()) {
            League league = entityRetrievalUtils.getLeagueOrThrow(id);
            tournament.addLeague(league);
        }

        return tournament;
    }

    public TournamentDTO toDTO(Tournament tournament) {
        List<Long> leaguesIds = new ArrayList<>();
        for (League league : tournament.getLeagues()) {
            leaguesIds.add(league.getId());
        }

        return TournamentDTO.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .idSite(tournament.getIdSite())
                .playerIds(tournament.getPlayers().stream().map(Player::getId).toList())
                .teamIds(tournament.getTeams().stream().map(Team::getId).toList())
                .dateOfStart(tournament.getDateOfStart())
                .dateOfEnd(tournament.getDateOfEnd())
                .leagueIds(leaguesIds)
                .build();
    }
}
