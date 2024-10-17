package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.ControversialDao;
import com.studleague.studleague.dao.interfaces.FullResultDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.interfaces.FullResultService;
import com.studleague.studleague.services.interfaces.TeamService;
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

    @Autowired
    TeamDao teamDao;

    @Autowired
    private TeamService teamService;


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
    public List<InfoTeamResults> fullResultsToTable(List<FullResult> fullResults)
    {
        List<InfoTeamResults> fullResultsTable = new ArrayList<>();
        int counter = 1;

        for(FullResult fullResult:fullResults)
        {
            HashMap<Tournament, List<Player>> tournamentsPlayers = teamService.getTournamentsPlayersByTeam(fullResult.getTeam().getId());
            InfoTeamResults resultRow = new InfoTeamResults();
            resultRow.setNumber(counter);
            counter+=1;
            resultRow.setTeam(fullResult.getTeam());

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
            resultRow.setTournament(fullResult.getTournament());
            resultRow.setPlayers(tournamentsPlayers.get(fullResult.getTournament()));
            fullResultsTable.add(resultRow);
        }
        return fullResultsTable;

    }




}
