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

   /* @Override
    @Transactional
    public void saveFullResult(FullResult fullResult) {
        for (Controversial controversial : fullResult.getControversials()) {
            controversial.setFullResult(fullResult);
        }
        Long teamId = fullResult.getTeam().getId();
        Long tournamentId = fullResult.getTournament().getId();
        Long resultId = fullResult.getId();
        if (resultId != null) {
            if (resultRepository.existsById(resultId)) {
                FullResult existingResult = entityRetrievalUtils.getResultOrThrow(resultId);
                updateFullResult(existingResult, fullResult);
            }
        } else if (resultRepository.existsByTeamIdAndTournamentId(teamId, tournamentId)) {
            FullResult existingFullResult = entityRetrievalUtils.getResultByTeamIdAndTournamentIdOrThrow(teamId, tournamentId);
            updateFullResult(existingFullResult, fullResult);
        } else {
            resultRepository.save(fullResult);
        }
    }*/
   @Override
   @Transactional
   public void saveFullResult(FullResult fullResult) {
       FullResult existingFullResult = findExistingFullResult(fullResult);

       if (existingFullResult != null) {
           updateExistingFullResult(existingFullResult, fullResult);
           resultRepository.save(existingFullResult);
       } else {
           resultRepository.save(fullResult);
       }
   }

    private FullResult findExistingFullResult(FullResult fullResult) {
        if (fullResult.getId() != null && resultRepository.existsById(fullResult.getId())) {
            return resultRepository.findById(fullResult.getId()).orElse(null);
        }
        if (resultRepository.existsByTeamIdAndTournamentId(fullResult.getTeam().getId(), fullResult.getTournament().getId())) {
            return entityRetrievalUtils.getResultByTeamIdAndTournamentIdOrThrow(
                    fullResult.getTeam().getId(), fullResult.getTournament().getId());
        }
        return null;
    }

    private void updateExistingFullResult(FullResult existingFullResult, FullResult fullResult) {
        for (Controversial controversial : fullResult.getControversials()) {
            if (!existingFullResult.getControversials().contains(controversial)) {
                existingFullResult.addControversialToFullResult(controversial);
            }
        }
        existingFullResult.setMask_results(fullResult.getMask_results());
        existingFullResult.setTotalScore(fullResult.getTotalScore());
    }



    @Transactional
    private void updateFullResult(FullResult existingFullResult, FullResult newFullResult) {
        existingFullResult.setMask_results(newFullResult.getMask_results());
        existingFullResult.setControversials(newFullResult.getControversials());
        existingFullResult.setTournament(newFullResult.getTournament());
        existingFullResult.setTeam(newFullResult.getTeam());
        existingFullResult.setTotalScore(newFullResult.getTotalScore());
        resultRepository.save(existingFullResult);
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
    public void deleteAllResults() {
        resultRepository.deleteAll();
    }

    @Override
    public boolean isManager(Long userId, Long resultId) {
        if (userId == null)
            return false;
        FullResult result = entityRetrievalUtils.getResultOrThrow(resultId);
        Long leagueId = result.getTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public boolean isManager(Long userId, FullResultDTO resultDTO) {
        if (userId == null)
            return false;
        FullResult result = fullResultFactory.mapToEntity(resultDTO);
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
        if (maskResults != null) {
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

    @Override
    public List<LeagueResult> calculateResultsBySystem(Long leagueId, String system, int numWorstGamesToExclude) {
        List<LeagueResult> standings = new ArrayList<>();
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        List<Tournament> tournaments = league.getTournaments()
                .stream()
                .sorted(Comparator.comparing(Tournament::getDateOfStart))
                .toList();
        Map<Long, LeagueResult> teamResultsMap = new HashMap<>();
        List<Team> leagueTeams = league.getTeams();
        for (Team team : leagueTeams) {
            LeagueResult leagueResult = new LeagueResult();
            leagueResult.setTeam(team);
            leagueResult.setTotalScore(0.0);
            standings.add(leagueResult);
            teamResultsMap.put(team.getId(), leagueResult);
        }

        for (int i = 0; i < tournaments.size(); i++) {
            Tournament tournament = tournaments.get(i);
            int tournamentNumber = i + 1;

            for (Team team : leagueTeams) {
                LeagueResult leagueResult = teamResultsMap.get(team.getId());
                double pointsForRound = getPointsForRound(team, tournament);
                leagueResult.getResultsByTour().put(tournamentNumber, pointsForRound);
            }
        }

        double maxTournamentScore = getMaxTournamentScore(tournaments);

        for (LeagueResult leagueResult : standings) {
            List<Double> scores = new ArrayList<>(leagueResult.getResultsByTour().values());
            double totalScore = calculateTotalScore(scores, system, maxTournamentScore, numWorstGamesToExclude);
            leagueResult.setTotalScore(totalScore);
        }

        standings.sort(Comparator.comparingDouble(LeagueResult::getTotalScore).reversed());
        return standings;
    }

    private double getPointsForRound(Team team, Tournament tournament) {
        if (!tournament.getTeams().contains(team)) return 0.0;
        return team.getResults().stream()
                .filter(result -> result.getTournament().equals(tournament))
                .map(FullResult::getTotalScore)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(0);
    }

    private double getMaxTournamentScore(List<Tournament> tournaments) {
        return tournaments.stream()
                .flatMap(tournament -> tournament.getTeams().stream())
                .flatMap(team -> team.getResults().stream())
                .map(FullResult::getTotalScore)
                .filter(Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
    }

    private double calculateTotalScore(List<Double> scores, String system, double maxTournamentScore, int numWorstGamesToExclude) {
        switch (system.toLowerCase()) {
            case "standard":
                return calculateStandardPoints(scores, numWorstGamesToExclude);

            case "micromatch":
                return scores.stream().mapToDouble(Double::doubleValue).sum();

            case "normalized":
                return calculateNormalizedPoints(scores, maxTournamentScore, numWorstGamesToExclude);

            default:
                throw new IllegalArgumentException("Unknown scoring system: " + system);
        }
    }


    private double calculateStandardPoints(List<Double> scores, int numWorstGamesToExclude) {
        scores = scores.stream().sorted().collect(Collectors.toList());
        int scoresLength = scores.size();
        if (scoresLength > numWorstGamesToExclude) {
            scores = scores.subList(numWorstGamesToExclude, scores.size());
        }

        return scores.stream().mapToDouble(Double::doubleValue).sum();
    }

    private double calculateNormalizedPoints(List<Double> scores, double maxTournamentScore, int numWorstGamesToExclude) {
        scores = scores.stream().sorted().collect(Collectors.toList());

        if (numWorstGamesToExclude > 0 && scores.size() > numWorstGamesToExclude) {
            scores = scores.subList(numWorstGamesToExclude, scores.size());
        }

        double totalPoints = scores.stream().mapToDouble(Double::doubleValue).sum();
        return (maxTournamentScore > 0) ? totalPoints / maxTournamentScore : 0;
    }


    private int calculateMicromatchPoints(FullResult result) {
        return 0;
    }


}
