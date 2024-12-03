package com.studleague.studleague.factory;

import com.studleague.studleague.dto.LeagueResultsDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.LeagueResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeagueResultsFactory implements DTOFactory<LeagueResultsDTO, LeagueResult> {

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
