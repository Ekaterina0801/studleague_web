package com.studleague.studleague.factory;

import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.repository.ResultRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FullResultFactory {

    @Autowired
    ResultRepository resultDao;

    @Autowired
    TeamRepository teamDao;

    @Autowired
    TournamentRepository tournamentDao;

    @Autowired
    ControversialFactory controversialFactory;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    FullResultFactory() {
    }

    public FullResult toEntity(FullResultDTO fullResultDTO) {
        long teamId = fullResultDTO.getTeam_id();
        long tournamentId = fullResultDTO.getTournament_id();
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        return FullResult.builder()
                .id(fullResultDTO.getId())
                .team(team)
                .tournament(tournament)
                .totalScore(fullResultDTO.getTotalScore())
                .mask_results(fullResultDTO.getMask_results())
                .controversials(fullResultDTO.getControversials().stream().map(x->entityRetrievalUtils.getControversialOrThrow(x)).toList())
                .build();
    }

    public FullResultDTO toDTO(FullResult fullResult) {
        return FullResultDTO.builder()
                .id(fullResult.getId())
                .mask_results(fullResult.getMask_results())
                .team_id(fullResult.getTeam().getId())
                .totalScore(fullResult.getTotalScore())
                .tournament_id(fullResult.getTournament().getId())
                .controversials(fullResult.getControversials().stream().map(Controversial::getId).toList())
                .build();
    }

}
