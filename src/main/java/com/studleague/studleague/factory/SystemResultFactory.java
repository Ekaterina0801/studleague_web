package com.studleague.studleague.factory;

import com.studleague.studleague.dto.SystemResultDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.SystemResult;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SystemResultFactory {

    private final EntityRetrievalUtils entityRetrievalUtils;

    public SystemResultDTO toDTO(SystemResult systemResult)
    {
        return SystemResultDTO.builder().
                id(systemResult.getId()).
                name(systemResult.getName()).
                description(systemResult.getDescription()).
                countNotIncludedGames(systemResult.getCountNotIncludedGames()).
                leagueIds(systemResult.getLeagues().stream().map(League::getId).toList()).
                build();
    }

    public SystemResult toEntity(SystemResultDTO systemResultDTO)
    {
        List<League> leagues = new ArrayList<>();
        for (Long leagueId: systemResultDTO.getLeagueIds())
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
