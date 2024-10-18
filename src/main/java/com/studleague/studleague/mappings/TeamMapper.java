package com.studleague.studleague.mappings;

import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TeamMapper {

    @Autowired
    private LeagueDao leagueDao;

    public TeamMapper() {
    }

    public Team toEntity(TeamDTO teamDTO) {
        long leagueId = teamDTO.getLeagueId();
        League league  = EntityRetrievalUtils.getEntityOrThrow(leagueDao.getLeagueById(leagueId), "League", leagueId);
        return Team.builder()
                .id(teamDTO.getId())
                .teamName(teamDTO.getTeamName())
                .university(teamDTO.getUniversity())
                .idSite(teamDTO.getIdSite())
                .league(league)
                .build();
    }

    public TeamDTO toDTO(Team team) {
        return TeamDTO.builder()
                .id(team.getId())
                .teamName(team.getTeamName())
                .university(team.getUniversity())
                .leagueId(team.getLeague() != null ? team.getLeague().getId() : null) // Handle possible null league
                .idSite(team.getIdSite())
                .build();
    }
}

