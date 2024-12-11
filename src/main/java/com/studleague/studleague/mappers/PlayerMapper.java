package com.studleague.studleague.mappers;


import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PlayerMapper implements DTOMapper<PlayerDTO, Player> {


    @Autowired
    @Lazy
    private TeamCompositionMapper teamCompositionMapper;


    @Autowired
    @Lazy
    private TeamMapper teamMapper;

    @Autowired
    private TeamMainInfoMapper teamMainInfoMapper;

    @Autowired
    private TransferMainInfoMapper transferMainInfoMapper;


    public PlayerMapper() {
    }

    public Player mapToEntity(PlayerDTO playerDTO) {
        return Player.builder()
                .id(playerDTO.getId())
                .name(playerDTO.getName())
                .patronymic(playerDTO.getPatronymic())
                .surname(playerDTO.getSurname())
                .transfers(playerDTO.getTransfers().stream().map(x -> transferMainInfoMapper.mapToEntity(x)).collect(Collectors.toList()))
                .university(playerDTO.getUniversity())
                .teamsCompositions(playerDTO.getTeamsCompositions().stream().map(x -> teamCompositionMapper.mapToEntity(x)).collect(Collectors.toList()))
                .dateOfBirth(playerDTO.getDateOfBirth())
                .teams(playerDTO.getTeams().stream().map(x -> teamMainInfoMapper.mapToEntity(x)).collect(Collectors.toList()))
                .build();
    }

    public PlayerDTO mapToDto(Player player) {
        return PlayerDTO.builder()
                .id(player.getId())
                .name(player.getName())
                .patronymic(player.getPatronymic())
                .surname(player.getSurname())
                .university(player.getUniversity())
                .transfers(player.getTransfers().stream().map(x -> transferMainInfoMapper.mapToDto(x)).collect(Collectors.toList()))
                .teamsCompositions(player.getTeamsCompositions().stream().map(x -> teamCompositionMapper.mapToDto(x)).collect(Collectors.toList()))
                .dateOfBirth(player.getDateOfBirth())
                .idSite(player.getIdSite())
                .teams(player.getTeams().stream().map(x -> teamMainInfoMapper.mapToDto(x)).collect(Collectors.toList()))
                .build();
    }
}
