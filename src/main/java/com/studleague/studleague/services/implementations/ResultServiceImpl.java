package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.result.FullResultDTO;
import com.studleague.studleague.dto.result.InfoTeamResults;
import com.studleague.studleague.dto.result.LeagueResult;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappers.player.PlayerMainInfoMapper;
import com.studleague.studleague.mappers.result.FullResultMapper;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.ResultService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private FullResultMapper fullResultMapper;

    @Autowired
    private PlayerMainInfoMapper playerMainInfoMapper;


    @Override
    @Transactional(readOnly = true)
    public FullResult getFullResultById(Long id) {
        return entityRetrievalUtils.getResultOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullResult> getAllFullResults() {
        return resultRepository.findAll();
    }

   @Override
   @Transactional
   @CacheEvict(value = "leagueResults", key = "#fullResult.team.league.id")
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

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", key = "#result.team.league.id")
    public void deleteFullResult(Long id) {
        FullResult result = entityRetrievalUtils.getResultOrThrow(id);
        if (result.getTournament() != null) {
            Tournament tournament = result.getTournament();
            tournament.getResults().remove(result);
            result.setTournament(null);
        }
        resultRepository.delete(result);
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
    @Transactional(readOnly = true)
    public List<FullResult> getResultsForTeam(Long teamId) {
        return resultRepository.findAllByTeamId(teamId);
    }

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", allEntries = true)
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
        FullResult result = fullResultMapper.mapToEntity(resultDTO);
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

    private InfoTeamResults createInfoTeamResults(FullResult fullResult, int counter) {
        InfoTeamResults resultRow = new InfoTeamResults();
        resultRow.setId(counter);
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
        resultRow.setPlayers(players.stream().map(x -> playerMainInfoMapper.mapToDto(x)).collect(Collectors.toList()));
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
    @Cacheable(cacheNames = "leagueResults", key = "#leagueId")
    public List<LeagueResult> calculateResultsBySystem(Long leagueId) {
        System.out.println("Calculate");
        List<LeagueResult> standings = new ArrayList<>();
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        String system = league.getSystemResult().getName();
        int numWorstGamesToExclude = league.getCountExcludedGames();
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
                Double pointsForRound = getPointsForRound(team, tournament);
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

    private Double getPointsForRound(Team team, Tournament tournament) {
        if (!tournament.getTeams().contains(team)) return null;
        return team.getResults().stream()
                .filter(result -> result.getTournament().equals(tournament))
                .map(FullResult::getTotalScore)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private double getMaxTournamentScore(List<Tournament> tournaments) {
        return tournaments.stream()
                .flatMap(tournament -> tournament.getTeams().stream())
                .flatMap(team -> team.getResults().stream())
                .map(FullResult::getTotalScore)
                .filter(Objects::nonNull)
                .max(Double::compareTo)
                .orElse(0.0);
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
        scores = scores.stream()
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
        int scoresLength = scores.size();
        if (scoresLength > numWorstGamesToExclude) {
            scores = scores.subList(numWorstGamesToExclude, scores.size());
        }

        return scores.stream().mapToDouble(Double::doubleValue).sum();
    }


    private double calculateNormalizedPoints(List<Double> scores, double maxTournamentScore, int numWorstGamesToExclude) {
        scores = scores.stream()
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());

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
