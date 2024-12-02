package com.studleague.studleague.factory;

import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FullResultFactory implements DTOFactory<FullResultDTO, FullResult>{


    @Autowired
    private ControversialFactory controversialFactory;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    @Lazy
    private TeamMainInfoFactory teamMainInfoFactory;


    FullResultFactory() {
    }

    public FullResult mapToEntity(FullResultDTO fullResultDTO) {
        long tournamentId = fullResultDTO.getTournamentId();
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        return FullResult.builder()
                .id(fullResultDTO.getId())
                .team(teamMainInfoFactory.mapToEntity(fullResultDTO.getTeam()))
                .tournament(tournament)
                .totalScore(fullResultDTO.getTotalScore())
                .mask_results(fullResultDTO.getMaskResults())
                .controversials(fullResultDTO.getControversials().stream().map(x -> controversialFactory.mapToEntity(x)).collect(Collectors.toList()))
                .build();
    }

    public FullResultDTO mapToDto(FullResult fullResult) {
        return FullResultDTO.builder()
                .id(fullResult.getId())
                .maskResults(fullResult.getMask_results())
                .team(teamMainInfoFactory.mapToDto(fullResult.getTeam()))
                .totalScore(fullResult.getTotalScore())
                .tournamentId(fullResult.getTournament().getId())
                .controversials(fullResult.getControversials().stream().map(x -> controversialFactory.mapToDto(x)).collect(Collectors.toList()))
                .build();
    }

}
