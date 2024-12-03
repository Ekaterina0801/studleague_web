package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TeamCompositionDTO;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class TeamCompositionFactory implements DTOFactory<TeamCompositionDTO, TeamComposition>{


    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;


    @Autowired
    private TournamentMainInfoFactory tournamentMainInfoFactory;

    @Autowired
    @Lazy
    private TeamMainInfoFactory teamMainInfoFactory;

    @Autowired
    @Lazy
    private PlayerMainInfoFactory playerMainInfoFactory;


    public TeamCompositionDTO mapToDto(TeamComposition teamComposition) {
        return TeamCompositionDTO.builder()
                .id(teamComposition.getId())
                .parentTeam(teamMainInfoFactory.mapToDto(teamComposition.getParentTeam()))
                .tournament(tournamentMainInfoFactory.mapToDto(teamComposition.getTournament()))
                .players(teamComposition.getPlayers().stream().map(x -> playerMainInfoFactory.mapToDto(x)).collect(Collectors.toList()))
                .build();
    }

    public TeamComposition mapToEntity(TeamCompositionDTO teamCompositionDTO)
    {
        return TeamComposition.builder()
                .id(teamCompositionDTO.getId())
                .parentTeam(teamMainInfoFactory.mapToEntity(teamCompositionDTO.getParentTeam()))
                .players(teamCompositionDTO.getPlayers().stream().map(x -> playerMainInfoFactory.mapToEntity(x)).toList())
                .tournament(tournamentMainInfoFactory.mapToEntity(teamCompositionDTO.getTournament()))
                .build();
    }


}
