package com.studleague.studleague.factory;


import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class PlayerFactory implements DTOFactory<PlayerDTO, Player>{


    @Autowired
    @Lazy
    private TeamCompositionFactory teamCompositionFactory;


    @Autowired
    @Lazy
    private TeamFactory teamFactory;

    public PlayerFactory() {
    }

    public Player mapToEntity(PlayerDTO playerDTO) {
        return Player.builder()
                .id(playerDTO.getId())
                .name(playerDTO.getName())
                .patronymic(playerDTO.getPatronymic())
                .surname(playerDTO.getSurname())
                .university(playerDTO.getUniversity())
                .teamsCompositions(playerDTO.getTeamsCompositions().stream().map(x->teamCompositionFactory.mapToEntity(x)).toList())
                .dateOfBirth(playerDTO.getDateOfBirth())
                .teams(playerDTO.getTeams().stream().map(x->teamFactory.mapToEntity(x)).toList())
                .build();
    }

    public PlayerDTO mapToDto(Player player) {
        return PlayerDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .patronymic(player.getPatronymic())
                .surname(player.getSurname())
                .university(player.getUniversity())
                .teamsCompositions(player.getTeamsCompositions().stream().map(x->teamCompositionFactory.mapToDto(x)).toList())
                .dateOfBirth(player.getDateOfBirth())
                .idSite(player.getIdSite())
                .teams(player.getTeams().stream().map(x->teamFactory.mapToDto(x)).toList())
                .build();
    }

    public Player mapToEntityWithoutTeams(PlayerDTO playerDTO) {
        return Player.builder()
                .id(playerDTO.getId())
                .name(playerDTO.getName())
                .patronymic(playerDTO.getPatronymic())
                .surname(playerDTO.getSurname())
                .university(playerDTO.getUniversity())
                .dateOfBirth(playerDTO.getDateOfBirth())
                .build();
    }

    public PlayerDTO mapToDtoWithoutTeams(Player player) {
        return PlayerDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .patronymic(player.getPatronymic())
                .surname(player.getSurname())
                .university(player.getUniversity())
                .dateOfBirth(player.getDateOfBirth())
                .build();
    }
}
