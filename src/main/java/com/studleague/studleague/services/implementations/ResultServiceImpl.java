package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.ControversialDao;
import com.studleague.studleague.dao.interfaces.ResultDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.ResultService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
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


    @Override
    @Transactional
    public FullResult getFullResultById(Long id) {
        return EntityRetrievalUtils.getEntityOrThrow(resultRepository.findById(id), "FullResult", id);
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
            resultRepository.save(EntityRetrievalUtils.getEntityByTwoIdOrThrow(resultRepository.findByTeamIdAndTournamentId(teamId, tournamentId), "FullResult",teamId, tournamentId));
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
        Controversial controversial = EntityRetrievalUtils.getEntityOrThrow(controversialRepository.findById(controversialId), "Controversial", controversialId);
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(resultRepository.findById(resultId), "FullResult", resultId);
        fullResult.addControversialToFullResult(controversial);
        resultRepository.save(fullResult);
        return fullResult;
    }

    @Override
    @Transactional
    public void deleteControversialFromResult(Long resultId, Long controversialId) {
        Controversial controversial = EntityRetrievalUtils.getEntityOrThrow(controversialRepository.findById(controversialId), "Controversial", controversialId);
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(resultRepository.findById(resultId), "FullResult", resultId);
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



    /*@Override
    public List<InfoTeamResults> fullResultsToTable(List<FullResult> fullResults) {
        List<InfoTeamResults> fullResultsTable = new ArrayList<>();
        int counter = 1;

        for (FullResult fullResult : fullResults) {
            List<Tournament> tournaments = tournamentDao.tournamentsByTeam(fullResult.getTeam().getId());
            HashMap<Tournament, List<Player>> tournamentsPlayers = new HashMap<>();
            InfoTeamResults resultRow = new InfoTeamResults();
            resultRow.setNumber(counter);
            counter += 1;
            resultRow.setTeam(fullResult.getTeam());

            //List<Controversial> controversials = fullResult.getControversials();
            //HashMap<Integer, Controversial> controversialsByNumber = controversialDao.getControversialByTournamentId(tournament_id);
            String maskResults = fullResult.getMask_results();
            List<Integer> answers = new ArrayList<>();
            List<Integer> questionNumbers = new ArrayList<>();
            int totalScore = 0;
            for (int i = 0; i < maskResults.length(); i++) {
                String answer = String.valueOf(maskResults.charAt(i));
                questionNumbers.add(i + 1);
                try {
                    int number = Integer.parseInt(answer);
                    answers.add(number);
                    totalScore += number;
                } catch (NumberFormatException e) {
                    answers.add(0);
                }
            }
            resultRow.setAnswers(answers);
            resultRow.setTotalScore(totalScore);
            resultRow.setCountQuestions(maskResults.length());
            resultRow.setQuestionNumbers(questionNumbers);
            resultRow.setTournament(fullResult.getTournament());
            resultRow.setPlayers(tournamentsPlayers.get(fullResult.getTournament()));
            fullResultsTable.add(resultRow);
        }
        return fullResultsTable;

    }*/


}
