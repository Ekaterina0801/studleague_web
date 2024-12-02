package com.studleague.studleague.factory;


import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PlayerFactory implements DTOFactory<PlayerDTO, Player>{


    @Autowired
    @Lazy
    private TeamCompositionFactory teamCompositionFactory;


    @Autowired
    @Lazy
    private TeamFactory teamFactory;

    @Autowired
    private TeamMainInfoFactory teamMainInfoFactory;

    public PlayerFactory() {
    }

    public Player mapToEntity(PlayerDTO playerDTO) {
        return Player.builder()
                .id(playerDTO.getId())
                .name(playerDTO.getName())
                .patronymic(playerDTO.getPatronymic())
                .surname(playerDTO.getSurname())
                .university(playerDTO.getUniversity())
                .teamsCompositions(playerDTO.getTeamsCompositions().stream().map(x -> teamCompositionFactory.mapToEntity(x)).collect(Collectors.toList()))
                .dateOfBirth(playerDTO.getDateOfBirth())
                .teams(playerDTO.getTeams().stream().map(x -> teamMainInfoFactory.mapToEntity(x)).collect(Collectors.toList()))
                .build();
    }

    public PlayerDTO mapToDto(Player player) {
        return PlayerDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .patronymic(player.getPatronymic())
                .surname(player.getSurname())
                .university(player.getUniversity())
                .teamsCompositions(player.getTeamsCompositions().stream().map(x -> teamCompositionFactory.mapToDto(x)).collect(Collectors.toList()))
                .dateOfBirth(player.getDateOfBirth())
                .idSite(player.getIdSite())
                .teams(player.getTeams().stream().map(x -> teamMainInfoFactory.mapToDto(x)).collect(Collectors.toList()))
                .build();
    }
}
