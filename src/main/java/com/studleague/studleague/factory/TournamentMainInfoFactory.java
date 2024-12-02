package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TournamentMainInfoDTO;
import com.studleague.studleague.entities.Tournament;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TournamentMainInfoFactory implements DTOFactory<TournamentMainInfoDTO, Tournament> {

    public Tournament mapToEntity(TournamentMainInfoDTO tournamentDto) {
        return Tournament.builder()
                .id(tournamentDto.getId())
                .name(tournamentDto.getName())
                .idSite(tournamentDto.getIdSite())
                .dateOfStart(tournamentDto.getDateOfStart())
                .dateOfEnd(tournamentDto.getDateOfEnd())
                .leagues(new ArrayList<>())
                .build();
    }

    public TournamentMainInfoDTO mapToDto(Tournament tournament) {

        return TournamentMainInfoDTO.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .idSite(tournament.getIdSite())
                .dateOfStart(tournament.getDateOfStart())
                .dateOfEnd(tournament.getDateOfEnd())
                .build();
    }
}
