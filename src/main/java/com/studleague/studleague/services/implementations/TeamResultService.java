package com.studleague.studleague.services.implementations;


import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappers.result.FullResultMapper;
import com.studleague.studleague.mappers.result.ResultMainInfoMapper;
import com.studleague.studleague.mappers.team.TeamMainInfoMapper;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.ResultService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TeamResultService {

    @Autowired
    private TeamService teamService;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private ResultService resultService;

    @Autowired
    private FullResultMapper fullResultMapper;

    @Autowired
    private TeamMainInfoMapper teamMainInfoMapper;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private ResultMainInfoMapper resultMainInfoMapper;

    @Autowired
    private TeamCompositionService teamCompositionService;

    @CacheEvict(value = "leagueResults", key = "#leagueId")
    public List<FullResult> processTeamResults(List<Map<String, Object>> data, Long leagueId, Long tournamentId) {
        List<FullResult> results = new ArrayList<>();
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);

        for (Map<String, Object> row : data) {
            String teamName = (String) row.get("Название");
            if (teamName == null || teamName.isEmpty()) {
                continue;
            }

            Team team = resolveOrCreateTeam(teamName, leagueId, tournament);

            FullResult fullResult = processRowData(row, team, tournamentId);
            resultService.saveFullResult(fullResult);
            results.add(fullResult);
        }

        return results;
    }

    private Team resolveOrCreateTeam(String teamName, Long leagueId, Tournament tournament) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Team> teamPage = teamService.searchTeams(teamName, leagueId, null, null, pageable);
        Team team;
        if (teamPage.getContent().isEmpty()) {
            team = createNewTeam(teamName, leagueId, tournament);
        } else {
            team = teamPage.getContent().get(0);
        }

        linkTeamToTournament(team, tournament);
        return team;
    }


    private Team createNewTeam(String teamName, Long leagueId, Tournament tournament) {
        Team team = Team.builder()
                .teamName(teamName)
                .tournaments(new ArrayList<>(List.of(tournament)))
                .league(entityRetrievalUtils.getLeagueOrThrow(leagueId))
                .build();
        teamService.saveTeam(team);
        return team;
    }

    private void linkTeamToTournament(Team team, Tournament tournament) {
        if (teamCompositionService.existsByTeamAndTournament(tournament.getId(), team.getId())) {
            return;
        }
        TeamComposition teamComposition = TeamComposition.builder()
                .parentTeam(team)
                .tournament(tournament)
                .build();
        team.getTeamCompositions().add(teamComposition);
        tournament.addTeamComposition(teamComposition);
        tournament.addTeam(team);
        teamCompositionService.save(teamComposition);
    }


    private FullResult processRowData(Map<String, Object> row, Team team, Long tournamentId) {
        StringBuilder maskResults = new StringBuilder();
        int totalScore = 0;
        FullResult fullResult = FullResult.builder()
                .team(team)
                .tournament(entityRetrievalUtils.getTournamentOrThrow(tournamentId))
                .mask_results("")
                .totalScore(0.0)
                .controversials(new ArrayList<>())
                .build();
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            try {
                if (!key.matches("\\d+(\\.\\d+)?")) {
                    continue;
                }
                if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                    maskResults.append(0);
                } else
                if (value instanceof Number) {
                    int score = ((Number) value).intValue();
                    maskResults.append(score);
                    totalScore += score;
                } else {
                    throw new IllegalArgumentException("Некорректное значение: " + value);
                }
            } catch (NumberFormatException e) {
                System.out.println("Ненумерованный ключ: " + key + ", значение: " + value);
            } catch (Exception e) {
                maskResults.append("X");
                Controversial controversial =
                        Controversial.builder()
                                .questionNumber(key.matches("\\d+(\\.\\d+)?") ?
                                        Integer.parseInt(key.split("\\.")[0]) : null)
                                .answer(value.toString())
                                .status("Pending")
                                .comment("Спорное значение")
                                .build();
                fullResult.addControversialToFullResult(controversial);
            }
        }
        fullResult.setMask_results(maskResults.toString());
        fullResult.setTotalScore((double) totalScore);

        return fullResult;

    }
}

