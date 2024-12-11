package com.studleague.studleague.mappers;


import com.studleague.studleague.dto.PlayerCreationDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class PlayerCreationMapper implements DTOMapper<PlayerCreationDTO, Player> {

    private final EntityRetrievalUtils entityRetrievalUtils;


    public PlayerCreationMapper(EntityRetrievalUtils entityRetrievalUtils) {
        this.entityRetrievalUtils = entityRetrievalUtils;
    }

    public Player mapToEntity(PlayerCreationDTO playerDTO) {
        return Player.builder()
                .id(playerDTO.getId())
                .name(playerDTO.getName())
                .patronymic(playerDTO.getPatronymic())
                .surname(playerDTO.getSurname())
                .university(playerDTO.getUniversity())
                .dateOfBirth(playerDTO.getDateOfBirth())
                .teams(playerDTO.getTeamIds() != null ?
                        playerDTO.getTeamIds().stream()
                                .map(entityRetrievalUtils::getTeamOrThrow)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }

    public PlayerCreationDTO mapToDto(Player player) {
        return PlayerCreationDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .patronymic(player.getPatronymic())
                .surname(player.getSurname())
                .university(player.getUniversity())
                .dateOfBirth(player.getDateOfBirth())
                .idSite(player.getIdSite())
                .teamIds(player.getTeams() != null ? player.getTeams().stream().map(Team::getId).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
}

