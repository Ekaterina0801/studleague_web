package com.studleague.studleague.mappings;


import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlayerMapper {

    @Autowired
    TeamDao teamDao;

    public PlayerMapper(){

    }

    public Player toEntity(PlayerDTO playerDTO){
        Player player = new Player(playerDTO.getId(), playerDTO.getName(), playerDTO.getPatronymic(), playerDTO.getSurname(), playerDTO.getUniversity(), playerDTO.getDateOfBirth());
        for (int teamId:playerDTO.getTeamIds())
        {
            player.addTeamToPlayer(teamDao.getTeamById(teamId));
        }

        return player;
    }

    public PlayerDTO toDTO(Player player){
        List<Integer> teamIds = new ArrayList<>();
        for (Team team: player.getTeams())
        {
            teamIds.add(team.getId());
        }
        PlayerDTO playerDTO = new PlayerDTO(player.getId(),player.getName(), player.getPatronymic(), player.getSurname(), player.getUniversity(), player.getDateOfBirth(), player.getIdSite(), teamIds);
        return playerDTO;
    }
}
