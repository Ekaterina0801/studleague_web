package com.studleague.studleague.services.implementations;


import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.factory.FullResultFactory;
import com.studleague.studleague.factory.ResultMainInfoFactory;
import com.studleague.studleague.factory.TeamMainInfoFactory;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.ResultService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FullResultFactory fullResultFactory;

    @Autowired
    private TeamMainInfoFactory teamMainInfoFactory;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private ResultMainInfoFactory resultMainInfoFactory;

    @Autowired
    private TeamCompositionService teamCompositionService;

    public List<FullResultDTO> processTeamResults(List<Map<String, Object>> data, Long leagueId, Long tournamentId) {
        List<FullResultDTO> results = new ArrayList<>();
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);

        for (Map<String, Object> row : data) {
            String teamName = (String) row.get("Название");
            if (teamName == null || teamName.isEmpty()) {
                continue;
            }

            Team team = resolveOrCreateTeam(teamName, leagueId, tournament);

            FullResultDTO fullResult = processRowData(row, team, tournamentId);
            resultService.saveFullResult(fullResultFactory.mapToEntity(fullResult));
            results.add(fullResult);
        }

        return results;
    }

    private Team resolveOrCreateTeam(String teamName, Long leagueId, Tournament tournament) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Team> teamPage = teamService.searchTeams(teamName, leagueId, null, null, pageable);

        Team team = teamPage.isEmpty()
                ? createNewTeam(teamName, leagueId, tournament)
                : teamPage.getContent().get(0);

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
        teamCompositionService.save(teamComposition);
    }


    private FullResultDTO processRowData(Map<String, Object> row, Team team, Long tournamentId) {
        StringBuilder maskResults = new StringBuilder();
        int totalScore = 0;
        List<ControversialDTO> controversials = new ArrayList<>();
        FullResultDTO fullResult = FullResultDTO.builder()
                .team(teamMainInfoFactory.mapToDto(team))
                .tournamentId(tournamentId)
                .maskResults("")
                .totalScore(0)
                .controversials(new ArrayList<>())
                .build();
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            try {
                double questionNumberDouble = Double.parseDouble(key);
                int questionNumber = (int) questionNumberDouble;

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
                ControversialDTO controversialDTO =
                        ControversialDTO.builder()
                                .questionNumber(key.matches("\\d+(\\.\\d+)?") ?
                                        Integer.parseInt(key.split("\\.")[0]) : null)
                                .answer(value.toString())
                                .status("Pending")
                                .comment("Спорное значение")
                                .build();
                fullResult.getControversials().add(controversialDTO);
            }
        }
        fullResult.setMaskResults(maskResults.toString());
        fullResult.setTotalScore(totalScore);

        return fullResult;

    }
}

