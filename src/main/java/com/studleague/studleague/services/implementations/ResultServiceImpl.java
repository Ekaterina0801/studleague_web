package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.factory.FullResultFactory;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.LeagueResult;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.ResultService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;

@Service("resultService")
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ControversialRepository controversialRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TournamentService tournamentService;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private FullResultFactory fullResultFactory;


    @Override
    @Transactional
    public FullResult getFullResultById(Long id) {
        return entityRetrievalUtils.getResultOrThrow(id);
    }

    @Override
    @Transactional
    public List<FullResult> getAllFullResults() {
        return resultRepository.findAll();
    }

    @Override
    @Transactional
    public void saveFullResult(FullResult fullResult) {
        List<Controversial> controversials = fullResult.getControversials();
        for (Controversial controversial : controversials) {
            controversial.setFullResult(fullResult);
        }
        Long teamId = fullResult.getTeam().getId();
        Long tournamentId = fullResult.getTournament().getId();
        if (resultRepository.existsByTeamIdAndTournamentId(teamId, tournamentId))
        {
            FullResult existingFullResult = entityRetrievalUtils.getResultByTeamIdAndTournamentIdOrThrow(teamId, tournamentId);
            existingFullResult.setTotalScore(fullResult.getTotalScore());
            resultRepository.save(existingFullResult);
        }
        else {
            resultRepository.save(fullResult);
        }
    }

    @Override
    @Transactional
    public void deleteFullResult(Long id) {
        resultRepository.deleteById(id);
    }

    @Override
    @Transactional
    public FullResult addControversialToResult(Long resultId, Long controversialId) {
        Controversial controversial = entityRetrievalUtils.getControversialOrThrow(controversialId);
        FullResult fullResult = entityRetrievalUtils.getResultOrThrow(resultId);
        fullResult.addControversialToFullResult(controversial);
        resultRepository.save(fullResult);
        return fullResult;
    }

    @Override
    @Transactional
    public void deleteControversialFromResult(Long resultId, Long controversialId) {
        Controversial controversial = entityRetrievalUtils.getControversialOrThrow(controversialId);
        FullResult fullResult = entityRetrievalUtils.getResultOrThrow(resultId);
        fullResult.deleteControversialFromFullResult(controversial);
        resultRepository.save(fullResult);
    }

    @Override
    @Transactional
    public List<FullResult> getResultsForTeam(Long teamId) {
        return resultRepository.findAllByTeamId(teamId);
    }

    @Override
    @Transactional
    public void deleteAllResults()
    {
        resultRepository.deleteAll();
    }

    @Override
    public boolean isManager(Long userId, Long resultId) {
        if (userId==null)
            return false;
        FullResult result = entityRetrievalUtils.getResultOrThrow(resultId);
        Long leagueId = result.getTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public boolean isManager(Long userId, FullResultDTO resultDTO) {
        if (userId==null)
            return false;
        FullResult result = fullResultFactory.toEntity(resultDTO);
        Long leagueId = result.getTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public List<InfoTeamResults> fullResultsToTable(List<FullResult> fullResults) {
        List<InfoTeamResults> fullResultsTable = new ArrayList<>();

        for (int i = 0; i < fullResults.size(); i++) {
            FullResult fullResult = fullResults.get(i);
            InfoTeamResults resultRow = createInfoTeamResults(fullResult, i + 1);
            fullResultsTable.add(resultRow);
        }

        return fullResultsTable;
    }
//PageRequest
    //async
    private InfoTeamResults createInfoTeamResults(FullResult fullResult, int counter) {
        InfoTeamResults resultRow = new InfoTeamResults();
        resultRow.setNumber(counter);
        resultRow.setTeam(fullResult.getTeam());

        String maskResults = fullResult.getMask_results();
        List<Integer> answers = new ArrayList<>();
        List<Integer> questionNumbers = new ArrayList<>();
        int totalScore = 0;
        int countQuestions = 0;
        List<Player> players = playerRepository.findAllByTeamIdAndTournamentId(fullResult.getTeam().getId(), fullResult.getTournament().getId());
        if (maskResults!=null)
        {
        for (int i = 0; i < maskResults.length(); i++) {
            String answer = String.valueOf(maskResults.charAt(i));
            questionNumbers.add(i + 1);
            totalScore += parseAnswer(answer, answers);
        }
        countQuestions = maskResults.length();
        }
        resultRow.setPlayers(players);
        resultRow.setAnswers(answers);
        resultRow.setTotalScore(totalScore);
        resultRow.setCountQuestions(countQuestions);
        resultRow.setQuestionNumbers(questionNumbers);
        resultRow.setTournament(fullResult.getTournament());
        return resultRow;
    }

    private int parseAnswer(String answer, List<Integer> answers) {
        try {
            int number = Integer.parseInt(answer);
            answers.add(number);
            return number;
        } catch (NumberFormatException e) {
            answers.add(0);
            return 0;
        }
    }

    /*public Map<Team, List<Integer>> calculateResultsBySystem(Long leagueId, String system) {
        // Хранит количество баллов за каждый тур для каждой команды
        Map<Team, List<Integer>> standings = new HashMap<>();

        List<Team> teams = teamRepository.findAllByLeagueId(leagueId);

        for (Team team : teams) {
            List<Integer> tourPoints = new ArrayList<>();
            for (FullResult result : team.getResults()) {
                Integer totalScore = result.getTotalScore();
                if (totalScore != null) {
                    tourPoints.add(totalScore);
                }
            }

            standings.put(team, tourPoints);
        }

        return standings;
    }*/



    public List<FullResult> getSortedResultsByTournamentStartDate(List<FullResult> results) {
        return results.stream()
                .sorted(Comparator.comparing(result -> {
                    Tournament tournament = result.getTournament();
                    return tournament != null ? tournament.getDateOfStart() : null;
                }))
                .collect(Collectors.toList());
    }

/*    public List<LeagueResult> calculateResultsBySystem(Long leagueId, String system) {
        // Хранит количество баллов за каждый тур для каждой команды
        List<LeagueResult> standings = new ArrayList<>();

        List<Team> teams = teamRepository.findAllByLeagueId(leagueId);

        for (Team team : teams) {
            LeagueResult leagueResult = new LeagueResult();
            leagueResult.setTeam(team);
            List<Integer> tourPoints = new ArrayList<>();
            List<FullResult> teamResults = getSortedResultsByTournamentStartDate(team.getResults());
            //TODO: сортировка по дате
            int number = 1;
            int sumPoints = 0;
            for (FullResult result : teamResults) {
                Integer totalScore = result.getTotalScore();
                if (totalScore!=null)
                {
                    leagueResult.getResultsByTour().put(number, totalScore);
                    sumPoints+=totalScore;
                }
                else
                {
                    leagueResult.getResultsByTour().put(number, 0);
                }

                number+=1;

            }
            leagueResult.setTotalScore(sumPoints);
            standings.add(leagueResult);

            //standings.put(team, tourPoints);
        }

        return standings;
    }*/



    /*public List<LeagueResult> calculateResultsBySystem(Long leagueId, String system) {
        List<LeagueResult> standings = new ArrayList<>();
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        List<Tournament> tournaments = league.getTournaments();
        Set<Long> processedTeamIds = new HashSet<>();

        for (Tournament tournament : tournaments) {
            List<Team> teams = tournament.getTeams();

            for (Team team : teams) {
                if (processedTeamIds.contains(team.getId())) {
                    continue;
                }
                processedTeamIds.add(team.getId());

                LeagueResult leagueResult = new LeagueResult();
                leagueResult.setTeam(team);
                List<FullResult> teamResults = getSortedResultsByTournamentStartDate(team.getResults());
                //TODO: сортировка по дате
                int number = 1;
                int sumPoints = 0;

                for (FullResult result : teamResults) {
                    Integer totalScore = result.getTotalScore();
                    if (totalScore != null) {
                        leagueResult.getResultsByTour().put(number, totalScore);
                        sumPoints += totalScore;
                    } else {
                        leagueResult.getResultsByTour().put(number, 0);
                    }

                    number += 1;
                }

                leagueResult.setTotalScore(sumPoints);
                standings.add(leagueResult);
            }
        }

        standings.sort(Comparator.comparingInt(LeagueResult::getTotalScore).reversed());

        return standings;
    }
*/
    public List<LeagueResult> calculateResultsBySystem(Long leagueId, String system) {
        List<LeagueResult> standings = new ArrayList<>();
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        List<Tournament> tournaments = league.getTournaments();
        Map<Long, LeagueResult> teamResultsMap = new HashMap<>();

        int tournamentNumber = 1;

        for (Tournament tournament : tournaments) {
            List<Team> teams = tournament.getTeams();

            int maxTournamentScore = teams.stream()
                    .flatMap(team -> team.getResults().stream())
                    .map(FullResult::getTotalScore)
                    .filter(Objects::nonNull)
                    .max(Integer::compareTo)
                    .orElse(0);

            for (Team team : teams) {
                LeagueResult leagueResult = teamResultsMap.computeIfAbsent(team.getId(), id -> {
                    LeagueResult lr = new LeagueResult();
                    lr.setTeam(team);
                    lr.setTotalScore(0.0);
                    standings.add(lr);
                    return lr;
                });

                FullResult result = team.getResults().stream()
                        .filter(r -> r.getTournament().equals(tournament))
                        .findFirst()
                        .orElse(null);

                if (result != null) {
                    Integer totalScore = result.getTotalScore();
                    int pointsForRound = (totalScore != null) ? totalScore : 0;
                    double pointsToAdd;
                    switch (system.toLowerCase()) {
                        case "standard":
                            pointsToAdd = pointsForRound;
                            leagueResult.getResultsByTour().put(tournamentNumber, pointsForRound);
                            break;

                        case "micromatch":
                            int micromatchScore = calculateMicromatchPoints(result);
                            pointsToAdd = micromatchScore;
                            leagueResult.getResultsByTour().put(tournamentNumber, micromatchScore);
                            break;

                        case "normalized":
                            double normalizedScore = maxTournamentScore > 0 ?
                                    (double) pointsForRound / maxTournamentScore : 0;
                            pointsToAdd = normalizedScore;
                            leagueResult.getResultsByTour().put(tournamentNumber, pointsForRound);
                            break;

                        default:
                            throw new IllegalArgumentException("Unknown scoring system: " + system);
                    }
                    leagueResult.setTotalScore(leagueResult.getTotalScore() + pointsToAdd);
                } else {
                    leagueResult.getResultsByTour().put(tournamentNumber, 0);
                }
            }

            tournamentNumber++;
        }

        standings.sort(Comparator.comparingDouble(LeagueResult::getTotalScore).reversed());

        return standings;
    }

    private int calculateMicromatchPoints(FullResult result) {
        return 0;
    }


}
