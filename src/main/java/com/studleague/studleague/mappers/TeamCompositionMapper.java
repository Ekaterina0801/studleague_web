package com.studleague.studleague.mappers;

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
public class TeamCompositionMapper implements DTOMapper<TeamCompositionDTO, TeamComposition> {


    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;


    @Autowired
    private TournamentMainInfoMapper tournamentMainInfoMapper;

    @Autowired
    @Lazy
    private TeamMainInfoMapper teamMainInfoMapper;

    @Autowired
    @Lazy
    private PlayerMainInfoMapper playerMainInfoMapper;


    public TeamCompositionDTO mapToDto(TeamComposition teamComposition) {
        return TeamCompositionDTO.builder()
                .id(teamComposition.getId())
                .parentTeam(teamMainInfoMapper.mapToDto(teamComposition.getParentTeam()))
                .tournament(tournamentMainInfoMapper.mapToDto(teamComposition.getTournament()))
                .players(teamComposition.getPlayers().stream().map(x -> playerMainInfoMapper.mapToDto(x)).collect(Collectors.toList()))
                .build();
    }

    public TeamComposition mapToEntity(TeamCompositionDTO teamCompositionDTO)
    {
        return TeamComposition.builder()
                .id(teamCompositionDTO.getId())
                .parentTeam(teamMainInfoMapper.mapToEntity(teamCompositionDTO.getParentTeam()))
                .players(teamCompositionDTO.getPlayers().stream().map(x -> playerMainInfoMapper.mapToEntity(x)).toList())
                .tournament(tournamentMainInfoMapper.mapToEntity(teamCompositionDTO.getTournament()))
                .build();
    }


}
