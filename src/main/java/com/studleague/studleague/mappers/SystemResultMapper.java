package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.SystemResultDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.SystemResult;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SystemResultMapper implements DTOMapper<SystemResultDTO, SystemResult> {

    @Autowired
    private final EntityRetrievalUtils entityRetrievalUtils;

    public SystemResultDTO mapToDto(SystemResult systemResult)
    {
        return SystemResultDTO.builder().
                id(systemResult.getId()).
                name(systemResult.getName()).
                description(systemResult.getDescription()).
                countNotIncludedGames(systemResult.getCountNotIncludedGames()).
                leaguesIds(systemResult.getLeagues().stream().map(League::getId).toList()).
                build();
    }

    public SystemResult mapToEntity(SystemResultDTO systemResultDTO)
    {
        List<League> leagues = new ArrayList<>();
        for (Long leagueId: systemResultDTO.getLeaguesIds())
        {
            League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
            leagues.add(league);
        }
        return SystemResult.builder()
                .id(systemResultDTO.getId())
                .name(systemResultDTO.getName())
                .description(systemResultDTO.getDescription())
                .countNotIncludedGames(systemResultDTO.getCountNotIncludedGames())
                .leagues(leagues)
                .build();
    }
}
