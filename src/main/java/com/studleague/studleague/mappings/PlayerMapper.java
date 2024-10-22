package com.studleague.studleague.mappings;


import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerMapper {

    @Autowired
    TeamDao teamDao;

    public PlayerMapper() {
    }

    public Player toEntity(PlayerDTO playerDTO) {
        Player player = Player.builder()
                .id(playerDTO.getId())
                .name(playerDTO.getName())
                .patronymic(playerDTO.getPatronymic())
                .surname(playerDTO.getSurname())
                .university(playerDTO.getUniversity())
                .dateOfBirth(playerDTO.getDateOfBirth())
                .build();

        for (long teamId : playerDTO.getTeamIds()) {
            if (teamId!=0){
                Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.findById(teamId), "Team", teamId);
                player.addTeamToPlayer(team);
            }

        }

        return player;
    }

    public PlayerDTO toDTO(Player player) {
        List<Long> teamIds = new ArrayList<>();
        for (Team team : player.getTeams()) {
            teamIds.add(team.getId());
        }

        return PlayerDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .patronymic(player.getPatronymic())
                .surname(player.getSurname())
                .university(player.getUniversity())
                .dateOfBirth(player.getDateOfBirth())
                .idSite(player.getIdSite())
                .teamIds(teamIds)
                .build();
    }
}
