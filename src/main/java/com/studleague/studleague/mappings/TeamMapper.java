package com.studleague.studleague.mappings;

import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    @Autowired
    LeagueDao leagueDao;
    public TeamMapper(){

    }

    public Team toEntity(TeamDTO teamDTO){
        Team team = new Team(teamDTO.getId(), teamDTO.getTeamName(), teamDTO.getUniversity());
        team.setLeague(leagueDao.getLeagueById(teamDTO.getLeagueId()));
        return team;
    }

    public TeamDTO toDTO(Team team){
        TeamDTO teamDTO = new TeamDTO(team.getId(),team.getTeamName(), team.getUniversity(), team.getLeague().getId());
        return teamDTO;
    }
}
