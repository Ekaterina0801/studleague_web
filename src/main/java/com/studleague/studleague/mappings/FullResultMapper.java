package com.studleague.studleague.mappings;

import com.studleague.studleague.dao.interfaces.ResultDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FullResultMapper {

    @Autowired
    ResultDao resultDao;

    @Autowired
    TeamDao teamDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    ControversialMapper controversialMapper;

    FullResultMapper() {
    }

    public FullResult toEntity(FullResultDTO fullResultDTO) {
        long teamId = fullResultDTO.getTeam_id();
        long tournamentId = fullResultDTO.getTournament_id();
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.findById(teamId), "Team", teamId);
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.findById(tournamentId), "Tournament", tournamentId);
        return FullResult.builder()
                .id(fullResultDTO.getId())
                .team(team)
                .tournament(tournament)
                .mask_results(fullResultDTO.getMask_results())
                .controversials(controversialMapper.convertDTOListToEntityList(fullResultDTO.getControversials()))
                .build();
    }

    public FullResultDTO toDTO(FullResult fullResult) {
        return FullResultDTO.builder()
                .id(fullResult.getId())
                .mask_results(fullResult.getMask_results())
                .team_id(fullResult.getTeam().getId())
                .tournament_id(fullResult.getTournament().getId())
                .controversials(controversialMapper.convertEntityListToDTOList(fullResult.getControversials()))
                .build();
    }

}
