package com.studleague.studleague.mappings;


import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dto.TournamentDto;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Tournament;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TournamentMapper {

    @Autowired
    LeagueDao leagueDao;

    @Autowired
    public TournamentMapper(){

    }

    public Tournament toEntity(TournamentDto tournamentDto){
        Tournament tournament = new Tournament(tournamentDto.getId(), tournamentDto.getName(), tournamentDto.getIdSite(),tournamentDto.getDateOfStart(),tournamentDto.getDateOfEnd());
        for (int id: tournamentDto.getLeagueIds())
        {
            tournament.addLeague(leagueDao.getLeagueById(id));
        }
        //tournament.(leagueDao.getLeagueById(teamDTO.getLeagueId()));
        return tournament;
    }

    public TournamentDto toDTO(Tournament tournament){
        List<Integer> leaguesIds = new ArrayList<>();
        for (League league: tournament.getLeagues())
        {
            leaguesIds.add(league.getId());
        }
        TournamentDto tournamentDto = new TournamentDto(tournament.getId(),tournament.getName(),tournament.getIdSite(),tournament.getDateOfStart(),tournament.getDateOfEnd(),leaguesIds);
        return tournamentDto;
    }
}
