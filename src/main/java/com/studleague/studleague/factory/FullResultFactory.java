package com.studleague.studleague.factory;

import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FullResultFactory implements DTOFactory<FullResultDTO, FullResult>{


    @Autowired
    ControversialFactory controversialFactory;
    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;


    FullResultFactory() {
    }

    public FullResult mapToEntity(FullResultDTO fullResultDTO) {
        long teamId = fullResultDTO.getTeamId();
        long tournamentId = fullResultDTO.getTournamentId();
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        return FullResult.builder()
                .id(fullResultDTO.getId())
                .team(team)
                .tournament(tournament)
                .totalScore(fullResultDTO.getTotalScore())
                .mask_results(fullResultDTO.getMaskResults())
                .controversials(fullResultDTO.getControversials().stream().map(x->controversialFactory.mapToEntity(x)).toList())
                .build();
    }

    public FullResultDTO mapToDto(FullResult fullResult) {
        return FullResultDTO.builder()
                .id(fullResult.getId())
                .maskResults(fullResult.getMask_results())
                .teamId(fullResult.getTeam().getId())
                .totalScore(fullResult.getTotalScore())
                .tournamentId(fullResult.getTournament().getId())
                .controversials(fullResult.getControversials().stream().map(x->controversialFactory.mapToDto(x)).toList())
                .build();
    }

}
