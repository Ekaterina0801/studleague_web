package com.studleague.studleague.utils;

import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.factory.FullResultFactory;
import com.studleague.studleague.factory.TeamMainInfoFactory;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.ResultService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelParserService {

    @Autowired
    TeamService teamService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private FullResultFactory fullResultFactory;

    @Autowired
    private TeamMainInfoFactory teamMainInfoFactory;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private TournamentService tournamentService;


    public List<FullResultDTO> parseExcelFile(File file, Long leagueId, Long tournamentId) throws Exception {
        List<FullResultDTO> results = new ArrayList<>();
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            int questionStartColumn = 2;
            Row headerRow = sheet.getRow(0);


            int questionCount = headerRow.getLastCellNum() - questionStartColumn;


            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                String teamName = row.getCell(0) != null ? row.getCell(0).toString() : "Unknown Team";
                Pageable pageable = PageRequest.of(0, 1);
                Page<Team> teamPage = teamService.searchTeams(teamName, leagueId, null, null, pageable);
                List<Team> teamsByTournament = tournament.getTeams();
                Team team;
                if (teamPage.isEmpty()) {
                    System.out.println("Команда с именем '" + teamName + "' и ID лиги " + leagueId + " не найдена.");
                    team = Team.builder()
                            .teamName(teamName)
                            .tournaments(new ArrayList<>(List.of(tournament)))
                            .league(entityRetrievalUtils.getLeagueOrThrow(leagueId))
                            .build();

                    teamService.saveTeam(team);
                    TeamComposition teamComposition = TeamComposition.builder().parentTeam(team).tournament(tournament).build();
                    tournament.addTeamComposition(teamComposition);
                    tournamentService.saveTournament(tournament);
                } else {


                    //TODO: Отрефакторить
                    team = teamPage.getContent().get(0);
                    if (!teamsByTournament.contains(team)) {
                        tournament.addTeam(team);
                        TeamComposition teamComposition = TeamComposition.builder().parentTeam(team).tournament(tournament).build();
                        tournament.addTeamComposition(teamComposition);
                        tournamentService.saveTournament(tournament);
                    }
                }


                FullResultDTO fullResult = FullResultDTO.builder()
                        .team(teamMainInfoFactory.mapToDto(team))
                        .tournamentId(tournamentId)
                        .maskResults("")
                        .totalScore(0)
                        .controversials(new ArrayList<>())
                        .build();

                StringBuilder maskResults = new StringBuilder();
                int totalScore = 0;
                int score;
                for (int questionNumber = 1; questionNumber <= questionCount; questionNumber++) {
                    Cell cell = row.getCell(questionStartColumn + questionNumber - 1);
                    String answer = cell != null ? cell.toString() : "0";

                    try {

                        score = Integer.parseInt(answer);
                        maskResults.append(score);
                        totalScore += score;
                    } catch (NumberFormatException e) {
                        try {
                            double doubleScore = Double.parseDouble(answer);
                            score = (int) doubleScore;
                            maskResults.append(score);
                            totalScore += score;
                        } catch (NumberFormatException ex) {
                            maskResults.append("X");

                            ControversialDTO controversial = ControversialDTO.builder()
                                    .id(null)
                                    .questionNumber(questionNumber)
                                    .answer(answer)
                                    .issuedAt(LocalDateTime.now())
                                    .status("Pending")
                                    .comment("Спорное значение")
                                    .build();
                            fullResult.getControversials().add(controversial);
                        }

                    }
                }

                fullResult.setMaskResults(maskResults.toString());
                fullResult.setTotalScore(totalScore);
                resultService.saveFullResult(fullResultFactory.mapToEntity(fullResult));
                results.add(fullResult);
            }
        }
        return results;
    }
}

