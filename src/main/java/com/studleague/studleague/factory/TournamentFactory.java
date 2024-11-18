package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TournamentDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class TournamentFactory implements DTOFactory<TournamentDTO, Tournament>{

    @Autowired
    private final EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private final FullResultFactory fullResultFactory;


    public Tournament mapToEntity(TournamentDTO tournamentDto) {
        Tournament tournament = Tournament.builder()
                .id(tournamentDto.getId())
                .name(tournamentDto.getName())
                .idSite(tournamentDto.getIdSite())
                .players(tournamentDto.getPlayerIds().stream().map(entityRetrievalUtils::getPlayerOrThrow).toList())
                .teams(tournamentDto.getTeamIds().stream().map(entityRetrievalUtils::getTeamOrThrow).toList())
                .dateOfStart(tournamentDto.getDateOfStart())
                .dateOfEnd(tournamentDto.getDateOfEnd())
                .results(tournamentDto.getResults().stream().map(fullResultFactory::mapToEntity).toList())
                .leagues(new ArrayList<>())
                .build();

        for (long id : tournamentDto.getLeaguesIds()) {
            League league = entityRetrievalUtils.getLeagueOrThrow(id);
            tournament.addLeague(league);
        }

        return tournament;
    }

    public TournamentDTO mapToDto(Tournament tournament) {
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
                .results(tournament.getResults().stream().map(fullResultFactory::mapToDto).toList())
                .leaguesIds(leaguesIds)
                .build();
    }
}

