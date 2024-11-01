package com.studleague.studleague.factory;

import com.studleague.studleague.dto.FlagDTO;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.repository.FlagRepository;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlagFactory {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private FlagRepository flagRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    public FlagFactory() {
    }

    public Flag toEntity(FlagDTO flagDTO) {

        List<Team> teams = new ArrayList<>();

        // Проверяем на null и пустоту списка teamIds
        if (flagDTO.getTeamIds() != null && !flagDTO.getTeamIds().isEmpty()) {
            for (long teamId : flagDTO.getTeamIds()) {
                Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
                teams.add(team);
            }
        }

        // Получаем лигу с проверкой на null
        League league = entityRetrievalUtils.getLeagueOrThrow(flagDTO.getLeagueId());

        return Flag.builder()
                .id(flagDTO.getId())
                .name(flagDTO.getName())
                .teams(teams)
                .league(league)
                .build();
    }

    public FlagDTO toDTO(Flag flag) {
        List<Long> teamIds = flag.getTeams() != null ?
                flag.getTeams().stream().map(Team::getId).toList() :
                new ArrayList<>(); // Обрабатываем случай, если teams равен null

        return FlagDTO.builder()
                .id(flag.getId())
                .name(flag.getName())
                .teamIds(teamIds)
                .leagueId(flag.getLeague() != null ? flag.getLeague().getId() : null) // Проверяем на null
                .build();
    }
}
