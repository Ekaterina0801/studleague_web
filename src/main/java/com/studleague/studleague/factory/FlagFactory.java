package com.studleague.studleague.factory;

import com.studleague.studleague.dto.FlagDTO;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class FlagFactory implements DTOFactory<FlagDTO, Flag>{

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    public Flag mapToEntity(FlagDTO flagDTO) {

        List<Team> teams = new ArrayList<>();

        if (flagDTO.getTeamsIds() != null && !flagDTO.getTeamsIds().isEmpty()) {
            for (long teamId : flagDTO.getTeamsIds()) {
                Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
                teams.add(team);
            }
        }

        League league = entityRetrievalUtils.getLeagueOrThrow(flagDTO.getLeagueId());

        return Flag.builder()
                .id(flagDTO.getId())
                .name(flagDTO.getName())
                .teams(teams)
                .league(league)
                .build();
    }

    public FlagDTO mapToDto(Flag flag) {
        List<Long> teamIds = flag.getTeams() != null ?
                flag.getTeams().stream().map(Team::getId).toList() :
                new ArrayList<>();

        return FlagDTO.builder()
                .id(flag.getId())
                .name(flag.getName())
                .teamsIds(teamIds)
                .leagueId(flag.getLeague() != null ? flag.getLeague().getId() : null)
                .build();
    }
}
