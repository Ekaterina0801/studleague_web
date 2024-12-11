package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.LeagueResult;
import com.studleague.studleague.dto.LeagueResultsDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeagueResultsMapper implements DTOMapper<LeagueResultsDTO, LeagueResult> {

    private final EntityRetrievalUtils entityRetrievalUtils;

    @Override
    public LeagueResult mapToEntity(LeagueResultsDTO dto) {
        Team team = entityRetrievalUtils.getTeamOrThrow(dto.getTeamId());

        return LeagueResult.builder()
                .resultsByTour(dto.getResultsByTour())
                .team(team)
                .totalScore(dto.getTotalScore())
                .build();
    }

    @Override
    public LeagueResultsDTO mapToDto(LeagueResult entity) {
        return LeagueResultsDTO.builder()
                .resultsByTour(entity.getResultsByTour())
                .teamId(entity.getTeam().getId())
                .teamName(entity.getTeam().getTeamName())
                .totalScore(entity.getTotalScore())
                .build();
    }
}
