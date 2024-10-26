package com.studleague.studleague.mappings;

import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TeamMapper {

    @Autowired
    private LeagueRepository leagueDao;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    public TeamMapper() {
    }

    public Team toEntity(TeamDTO teamDTO) {
        long leagueId = teamDTO.getLeagueId();
        League league  = EntityRetrievalUtils.getEntityOrThrow(leagueDao.findById(leagueId), "League", leagueId);
        List<Player> players = new ArrayList<>();
        List<Tournament> tournaments = new ArrayList<>();
        for (Long playerId:teamDTO.getPlayersIds())
        {
            Player player = EntityRetrievalUtils.getEntityOrThrow(playerRepository.findById(playerId), "Player", playerId);
            players.add(player);
        }

        for (Long tournamentId:teamDTO.getTournamentsIds())
        {
            Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
            tournaments.add(tournament);

        }
        return Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .university(teamDTO.getUniversity())
                .idSite(teamDTO.getIdSite())
                .players(players)
                .tournaments(tournaments)
                .league(league)
                .build();
    }

    public TeamDTO toDTO(Team team) {
        List<Long> players = team.getPlayers().stream().map(Player::getId).toList();
        List<Long> tournaments = team.getTournaments().stream().map(Tournament::getId).toList();
        return TeamDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .university(team.getUniversity())
                .playersIds(players)
                .tournamentsIds(tournaments)
                .leagueId(team.getLeague() != null ? team.getLeague().getId() : null)
                .idSite(team.getIdSite())
                .build();
    }
}

