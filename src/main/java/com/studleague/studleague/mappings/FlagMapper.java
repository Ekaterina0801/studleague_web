package com.studleague.studleague.mappings;

import com.studleague.studleague.dao.interfaces.FlagDao;
import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dto.FlagDTO;
import com.studleague.studleague.dto.TeamDTO;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.repository.FlagRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlagMapper {

    @Autowired
    private TeamRepository teamRepository;

    public FlagMapper() {
    }

    public Flag toEntity(FlagDTO flagDTO) {

        List<Team> teams = new ArrayList<>();
        if (!flagDTO.getTeamIds().isEmpty())
        {
            for (long teamId:flagDTO.getTeamIds())
            {
                Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
                teams.add(team);
            }
        }
        return Flag.builder()
                .id(flagDTO.getId())
                .name(flagDTO.getName())
                .teams(teams)
                .build();
    }

    public FlagDTO toDTO(Flag flag) {
        List<Long> teamIds = flag.getTeams().stream().map(Team::getId).toList();
        return FlagDTO.builder()
                .id(flag.getId())
                .name(flag.getName())
                .teamIds(teamIds)
                .build();
    }
}
