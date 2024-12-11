package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FullResultMapper implements DTOMapper<FullResultDTO, FullResult> {


    @Autowired
    private ControversialMapper controversialMapper;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    @Lazy
    private TeamMainInfoMapper teamMainInfoMapper;


    FullResultMapper() {
    }

    public FullResult mapToEntity(FullResultDTO fullResultDTO) {
        long tournamentId = fullResultDTO.getTournamentId();
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        return FullResult.builder()
                .id(fullResultDTO.getId())
                .team(teamMainInfoMapper.mapToEntity(fullResultDTO.getTeam()))
                .tournament(tournament)
                .totalScore(fullResultDTO.getTotalScore())
                .mask_results(fullResultDTO.getMaskResults())
                .controversials(fullResultDTO.getControversials().stream().map(x -> controversialMapper.mapToEntity(x)).collect(Collectors.toList()))
                .build();
    }

    public FullResultDTO mapToDto(FullResult fullResult) {
        return FullResultDTO.builder()
                .id(fullResult.getId())
                .maskResults(fullResult.getMask_results())
                .team(teamMainInfoMapper.mapToDto(fullResult.getTeam()))
                .totalScore(fullResult.getTotalScore())
                .tournamentId(fullResult.getTournament().getId())
                .controversials(fullResult.getControversials().stream().map(x -> controversialMapper.mapToDto(x)).collect(Collectors.toList()))
                .build();
    }

}
