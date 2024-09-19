package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.ControversialDao;
import com.studleague.studleague.dao.interfaces.FullResultDao;
import com.studleague.studleague.dto.ResultTableRow;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.services.interfaces.FullResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FullResultServiceImpl implements FullResultService {

    @Autowired
    FullResultDao fullResultDao;

    @Autowired
    ControversialDao controversialDao;

    @Override
    @Transactional
    public FullResult getFullResultById(int id) {
        FullResult fullResult = fullResultDao.getFullResultById(id);
        return fullResult;
    }

    @Override
    @Transactional
    public List<FullResult> getAllFullResults() {
        List<FullResult> results = fullResultDao.getAllFullResults();
        return results;
    }

    @Override
    @Transactional
    public void saveFullResult(FullResult fullResult) {
        List<Controversial> controversials = fullResult.getControversials();
        for (Controversial controversial:controversials){
            controversial.setFullResult(fullResult);
            //controversialDao.saveControversial(controversial);

        }
        fullResultDao.saveFullResult(fullResult);
    }

    @Override
    @Transactional
    public void updateFullResult(FullResult fullResult, String[] params) {
        fullResultDao.updateFullResult(fullResult, params);
    }

    @Override
    @Transactional
    public void deleteFullResult(int id) {
        fullResultDao.deleteFullResult(id);
    }

    @Override
    public FullResult addControversialToResult(int result_id, int controversial_id) {
        Controversial controversial = controversialDao.getControversialById(controversial_id);
        FullResult fullResult = fullResultDao.getFullResultById(result_id);
        fullResult.addControversialToFullResult(controversial);
        fullResultDao.saveFullResult(fullResult);
        return fullResult;
    }

    @Override
    public void deleteControversialFromResult(int result_id, int controversial_id) {
        Controversial controversial = controversialDao.getControversialById(controversial_id);
        FullResult fullResult = fullResultDao.getFullResultById(result_id);
        fullResult.deleteControversialFromFullResult(controversial);
        fullResultDao.saveFullResult(fullResult);
    }

    public List<FullResult> getResultsForTeam(int team_id){
        return fullResultDao.getResultsForTeam(team_id);
    }


    @Override
    public List<ResultTableRow> fullResultsToTable(List<FullResult> fullResults)
    {
        List<ResultTableRow> fullResultsTable = new ArrayList<>();
        int counter = 1;
        for(FullResult fullResult:fullResults)
        {
            ResultTableRow resultRow = new ResultTableRow();
            resultRow.setNumber(counter);
            counter+=1;
            resultRow.setTeamName(fullResult.getTeam().getTeamName());

            //List<Controversial> controversials = fullResult.getControversials();
            //HashMap<Integer, Controversial> controversialsByNumber = controversialDao.getControversialByTournamentId(tournament_id);
            String maskResults = fullResult.getMask_results();
            List<Integer> answers = new ArrayList<>();
            List<Integer> questionNumbers = new ArrayList<>();
            int totalScore = 0;
            for (int i = 0; i < maskResults.length(); i++){
                String answer = String.valueOf(maskResults.charAt(i));
                questionNumbers.add(i+1);
                try {
                    int number = Integer.parseInt(answer);
                    answers.add(number);
                    totalScore+=number;
                } catch (NumberFormatException e) {
                    answers.add(0);
                }
            }
            resultRow.setAnswers(answers);
            resultRow.setTotalScore(totalScore);
            resultRow.setCountQuestions(maskResults.length());
            resultRow.setQuestionNumbers(questionNumbers);
            fullResultsTable.add(resultRow);
        }
        return fullResultsTable;

    }




}
