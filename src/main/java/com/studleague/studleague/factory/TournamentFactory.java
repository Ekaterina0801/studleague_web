package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TournamentDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class TournamentFactory implements DTOFactory<TournamentDTO, Tournament>{

    @Autowired
    private final EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    @Lazy
    private final FullResultFactory fullResultFactory;

    @Lazy
    private final TeamCompositionFactory teamCompositionFactory;


    public Tournament mapToEntity(TournamentDTO tournamentDto) {
        Tournament tournament = Tournament.builder()
                .id(tournamentDto.getId())
                .name(tournamentDto.getName())
                .idSite(tournamentDto.getIdSite())
                .dateOfStart(tournamentDto.getDateOfStart())
                .dateOfEnd(tournamentDto.getDateOfEnd())
                .teamCompositions(tournamentDto.getTeamCompositions().stream().map(teamCompositionFactory::mapToEntity).collect(Collectors.toList()))
                .results(tournamentDto.getResults().stream().map(fullResultFactory::mapToEntity).collect(Collectors.toList()))
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
                .dateOfStart(tournament.getDateOfStart())
                .teamCompositions(tournament.getTeamCompositions().stream().map(teamCompositionFactory::mapToDto).collect(Collectors.toList()))
                .dateOfEnd(tournament.getDateOfEnd())
                .results(tournament.getResults().stream().map(fullResultFactory::mapToDto).collect(Collectors.toList()))
                .leaguesIds(leaguesIds)
                .build();
    }
}

