package com.studleague.studleague.factory;

import com.studleague.studleague.dto.LeagueMainInfoDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeagueMainInfoFactory implements DTOFactory<LeagueMainInfoDTO, League> {

    private final EntityRetrievalUtils entityRetrievalUtils;

    @Override
    public League mapToEntity(LeagueMainInfoDTO dto) {
        return League.builder().id(dto.getId())
                .name(dto.getName())
                .countExcludedGames(dto.getCountExcludedGames())
                .systemResult(entityRetrievalUtils.getSystemResultOrThrow(dto.getSystemResultId()))
                .createdBy(entityRetrievalUtils.getUserOrThrow(dto.getCreatedById()))
                .build();
    }

    @Override
    public LeagueMainInfoDTO mapToDto(League entity) {
        return LeagueMainInfoDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .countExcludedGames(entity.getCountExcludedGames())
                .systemResultId(entity.getSystemResult().getId())
                .createdById(entity.getCreatedBy().getId())
                .build();
    }
}
