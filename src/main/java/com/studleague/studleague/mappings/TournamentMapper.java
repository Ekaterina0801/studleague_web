package com.studleague.studleague.mappings;


import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dto.TournamentDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TournamentMapper {

    @Autowired
    private LeagueRepository leagueDao;

    public Tournament toEntity(TournamentDTO tournamentDto) {
        Tournament tournament = Tournament.builder()
                .id(tournamentDto.getId())
                .name(tournamentDto.getName())
                .idSite(tournamentDto.getIdSite())
                .dateOfStart(tournamentDto.getDateOfStart())
                .dateOfEnd(tournamentDto.getDateOfEnd())
                .leagues(new ArrayList<>())
                .build();

        for (long id : tournamentDto.getLeagueIds()) {
            League league = EntityRetrievalUtils.getEntityOrThrow(leagueDao.findById(id), "League", id);
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
                .dateOfStart(tournament.getDateOfStart())
                .dateOfEnd(tournament.getDateOfEnd())
                .leagueIds(leaguesIds)
                .build();
    }
}

