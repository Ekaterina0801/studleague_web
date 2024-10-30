package com.studleague.studleague.factory;

import com.studleague.studleague.dto.TeamCompositionDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class TeamCompositionFactory {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;


    public TeamCompositionDTO toDTO(TeamComposition teamComposition) {
        return TeamCompositionDTO.builder()
                .id(teamComposition.getId())
                .parentId(teamComposition.getParentTeam().getId())
                .tournamentId(teamComposition.getTournament().getId())
                .playerIds(teamComposition.getPlayers().stream().map(Player::getId).toList())
                .build();
    }

    public TeamComposition toEntity(TeamCompositionDTO teamCompositionDTO)
    {
        List<Player> players = new ArrayList<>();
        for (Long playerId:teamCompositionDTO.getPlayerIds())
        {
            Player player = EntityRetrievalUtils.getEntityOrThrow(playerRepository.findById(playerId), "Player", playerId);
            players.add(player);
        }

        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamCompositionDTO.getParentId()), "Team", teamCompositionDTO.getParentId());
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(teamCompositionDTO.getTournamentId()), "Tournament", teamCompositionDTO.getTournamentId());
        return TeamComposition.builder()
                .id(teamCompositionDTO.getId())
                .parentTeam(team)
                .players(players)
                .tournament(tournament)
                .build();
    }


}
