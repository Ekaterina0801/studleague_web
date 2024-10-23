package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.ControversialDao;
import com.studleague.studleague.dao.interfaces.ResultDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.repository.ControversialRepository;
import com.studleague.studleague.repository.ResultRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    //private ResultDao resultRepository;
    private ResultRepository resultRepository;

    @Autowired
    //private ControversialDao controversialRepository;
    private ControversialRepository controversialRepository;

    @Autowired
    //private TeamDao teamRepository;
    private TeamRepository teamRepository;

    @Autowired
    //private TournamentDao tournamentRepository;
    private TournamentRepository tournamentRepository;


    @Override
    @Transactional
    public FullResult getFullResultById(long id) {
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
        long teamId = fullResult.getTeam().getId();
        long tournamentId = fullResult.getTournament().getId();
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
    public void deleteFullResult(long id) {
        resultRepository.deleteById(id);
    }

    @Override
    @Transactional
    public FullResult addControversialToResult(long resultId, long controversialId) {
        Controversial controversial = EntityRetrievalUtils.getEntityOrThrow(controversialRepository.findById(controversialId), "Controversial", controversialId);
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(resultRepository.findById(resultId), "FullResult", resultId);
        fullResult.addControversialToFullResult(controversial);
        resultRepository.save(fullResult);
        return fullResult;
    }

    @Override
    @Transactional
    public void deleteControversialFromResult(long resultId, long controversialId) {
        Controversial controversial = EntityRetrievalUtils.getEntityOrThrow(controversialRepository.findById(controversialId), "Controversial", controversialId);
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(resultRepository.findById(resultId), "FullResult", resultId);
        fullResult.deleteControversialFromFullResult(controversial);
        resultRepository.save(fullResult);
    }

    @Override
    @Transactional
    public List<FullResult> getResultsForTeam(long teamId) {
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

    private InfoTeamResults createInfoTeamResults(FullResult fullResult, int counter) {
        InfoTeamResults resultRow = new InfoTeamResults();
        resultRow.setNumber(counter);
        resultRow.setTeam(fullResult.getTeam());

        String maskResults = fullResult.getMask_results();
        List<Integer> answers = new ArrayList<>();
        List<Integer> questionNumbers = new ArrayList<>();
        int totalScore = 0;

        for (int i = 0; i < maskResults.length(); i++) {
            String answer = String.valueOf(maskResults.charAt(i));
            questionNumbers.add(i + 1);
            totalScore += parseAnswer(answer, answers);
        }

        resultRow.setAnswers(answers);
        resultRow.setTotalScore(totalScore);
        resultRow.setCountQuestions(maskResults.length());
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
