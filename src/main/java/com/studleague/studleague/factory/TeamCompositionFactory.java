package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TeamCompositionDTO;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TeamCompositionFactory implements DTOFactory<TeamCompositionDTO, TeamComposition>{


    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    @Lazy
    private TeamFactory teamFactory;

    @Autowired
    private TournamentFactory tournamentFactory;

    @Autowired
    private PlayerFactory playerFactory;


    public TeamCompositionDTO mapToDto(TeamComposition teamComposition) {
        return TeamCompositionDTO.builder()
                .id(teamComposition.getId())
                .parentTeam(teamFactory.mapToDto(teamComposition.getParentTeam()))
                .tournament(tournamentFactory.mapToDto(teamComposition.getTournament()))
                .players(teamComposition.getPlayers().stream().map(x->playerFactory.mapToDtoWithoutTeams(x)).toList())
                .build();
    }

    public TeamComposition mapToEntity(TeamCompositionDTO teamCompositionDTO)
    {
        return TeamComposition.builder()
                .id(teamCompositionDTO.getId())
                .parentTeam(teamFactory.mapToEntity(teamCompositionDTO.getParentTeam()))
                .players(teamCompositionDTO.getPlayers().stream().map(x->playerFactory.mapToEntityWithoutTeams(x)).toList())
                .tournament(tournamentFactory.mapToEntity(teamCompositionDTO.getTournament()))
                .build();
    }


}
