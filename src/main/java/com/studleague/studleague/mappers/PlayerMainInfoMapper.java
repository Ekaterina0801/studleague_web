package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.PlayerMainInfoDTO;
import com.studleague.studleague.entities.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMainInfoMapper implements DTOMapper<PlayerMainInfoDTO, Player> {


    public PlayerMainInfoMapper() {
    }

    public Player mapToEntity(PlayerMainInfoDTO playerDTO) {
        return Player.builder()
                .id(playerDTO.getId())
                .name(playerDTO.getName())
                .patronymic(playerDTO.getPatronymic())
                .surname(playerDTO.getSurname())
                .university(playerDTO.getUniversity())
                .dateOfBirth(playerDTO.getDateOfBirth())
                .build();
    }

    public PlayerMainInfoDTO mapToDto(Player player) {
        return PlayerMainInfoDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .patronymic(player.getPatronymic())
                .surname(player.getSurname())
                .university(player.getUniversity())
                .dateOfBirth(player.getDateOfBirth())
                .idSite(player.getIdSite())
                .build();
    }
}
