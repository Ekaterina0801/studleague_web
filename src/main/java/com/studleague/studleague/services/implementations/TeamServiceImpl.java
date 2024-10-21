package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.*;
import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    TeamDao teamDao;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    FlagDao flagDao;

    @Autowired
    LeagueDao leagueDao;

    @Autowired
    private FullResultDao fullResultDao;

    @Autowired
    private TournamentDao tournamentDao;



    @Override
    @Transactional
    public Team getTeamById(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(id), "Team", id);
    }

    @Override
    @Transactional
    public List<Team> getAllTeams() {
        return teamDao.getAllTeams();
    }

    @Override
    @Transactional
    public void saveTeam(Team team) {
        teamDao.saveTeam(team);
    }

    @Override
    @Transactional
    public void deleteTeam(long id) {
        teamDao.deleteTeam(id);
    }

    @Override
    @Transactional
    public List<Team> teamsByLeague(long leagueId) {
        return teamDao.teamsByLeague(leagueId);
    }

    @Override
    @Transactional
    public Team addPlayerToTeam(long teamId, long playerId) {
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerDao.getPlayerById(playerId), "Player", playerId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        team.addPlayerToTeam(player);
        teamDao.saveTeam(team);
        return team;
    }

    @Override
    @Transactional
    public Team deletePlayerFromTeam(long teamId, long playerId) {
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerDao.getPlayerById(playerId), "Player", playerId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        team.deletePlayerFromTeam(player);
        teamDao.saveTeam(team);
        return team;
    }

    @Override
    @Transactional
    public Team addFlagToTeam(long teamId, long flagId) {
        Flag flag = EntityRetrievalUtils.getEntityOrThrow(flagDao.getFlagById(flagId), "Flag", flagId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        team.addFlagToTeam(flag);
        teamDao.saveTeam(team);
        return team;
    }

    @Override
    @Transactional
    public Team addLeagueToTeam(long teamId, long leagueId) {
        League league = EntityRetrievalUtils.getEntityOrThrow(leagueDao.getLeagueById(leagueId), "League", leagueId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        team.setLeague(league);
        teamDao.saveTeam(team);
        return team;
    }

    @Override
    @Transactional
    public Team deleteFlagFromTeam(long teamId, long flagId) {
        Flag flag = EntityRetrievalUtils.getEntityOrThrow(flagDao.getFlagById(flagId), "Flag", flagId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        team.deleteFlagFromTeam(flag);
        teamDao.saveTeam(team);
        return team;
    }

    @Override
    @Transactional
    public Team getTeamByIdSite(long idSite) {
        return EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(idSite), "Team (by idSite)", idSite);
    }

    @Override
    @Transactional
    public List<InfoTeamResults> getInfoTeamResultsByTeam(long teamId) {
        List<InfoTeamResults> infoTeamResults = new ArrayList<>();
        Team team = getTeamById(teamId);
        List<FullResult> results = fullResultDao.getResultsForTeam(teamId);

        HashMap<Tournament, FullResult> tournamentsResults = new HashMap<>();
        for (FullResult result : results) {
            tournamentsResults.put(result.getTournament(), result);
        }

        HashMap<Tournament, List<Player>> tournamentsPlayers = getTournamentsPlayersByTeam(teamId);

        int counter = 1;
        for (Tournament tournament : tournamentsPlayers.keySet()) {
            InfoTeamResults row = new InfoTeamResults();
            row.setNumber(counter++);
            row.setPlayers(tournamentsPlayers.get(tournament));
            row.setTeam(team);
            row.setTournament(tournament);

            if (tournamentsResults.containsKey(tournament)) {
                setScoreDetails(row, tournamentsResults.get(tournament));
            }

            infoTeamResults.add(row);
        }
        return infoTeamResults;
    }

    private void setScoreDetails(InfoTeamResults row, FullResult result) {
        String maskResults = result.getMask_results();
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

        row.setAnswers(answers);
        row.setTotalScore(totalScore);
        row.setCountQuestions(maskResults.length());
        row.setQuestionNumbers(questionNumbers);
    }

    @Override
    @Transactional
    public HashMap<Tournament, List<Player>> getTournamentsPlayersByTeam(long teamId) {

        List<Tournament> tournaments = tournamentDao.tournamentsByTeam(teamId);
        HashMap<Tournament, List<Player>> tournamentsPlayers = new HashMap<>();
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        for (Tournament tournament : tournaments) {
            List<Player> players = tournament.getPlayers().stream().filter(x -> x.getTeams().contains(team)).toList();
            tournamentsPlayers.put(tournament, players);
        }
        return tournamentsPlayers;

    }

    public boolean existsByIdSite(long idSite){
        return teamDao.existsByIdSite(idSite);
    }

    /*@Override
    @Transactional
    public List<InfoTeamResults> getInfoTeamResultsByTeam(int team_id)
    {
        List<InfoTeamResults> infoTeamResults = new ArrayList<>();
        InfoTeamResults row = new InfoTeamResults();
        Team team = getTeamById(team_id);
        List<FullResult> results = fullResultDao.getResultsForTeam(team_id);
        HashMap<Tournament, FullResult> tournamentsResults = new HashMap<>();
        for (FullResult result: results)
        {
            tournamentsResults.put(result.getTournament(), result);
        }
        int counter = 1;
        HashMap<Tournament, List<Player>> tournamentsPlayers = getTournamentsPlayersByTeam(team_id);
        for (Tournament tournament:tournamentsPlayers.keySet())
        {
            row.setNumber(counter);
            counter+=1;
            row.setPlayers(tournamentsPlayers.get(tournament));
            row.setTeam(team);
            row.setTournament(tournament);
            if (tournamentsResults.containsKey(tournament))
            {
                FullResult result = tournamentsResults.get(tournament);
                String maskResults = result.getMask_results();
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
                row.setAnswers(answers);
                row.setTotalScore(totalScore);
                row.setCountQuestions(maskResults.length());
                row.setQuestionNumbers(questionNumbers);

            }
            infoTeamResults.add(row);
        }
        return infoTeamResults;
    }
*/


    @Override
    @Transactional
    public Team getTeamByPlayerIdAndLeagueId(long playerId, long leagueId) {
        return EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamPlayerByLeague(playerId, leagueId), "Team (by playerId and teamId)", playerId);
    }

}
