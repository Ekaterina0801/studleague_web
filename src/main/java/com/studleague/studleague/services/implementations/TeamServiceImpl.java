package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.*;
import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.repository.*;
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
    //private TeamDao teamRepository;
    private TeamRepository teamRepository;

    @Autowired
    //private PlayerDao playerRepository;
    private PlayerRepository playerRepository;

    @Autowired
    //private FlagDao flagRepository;
    private FlagRepository flagRepository;

    @Autowired
    //private LeagueDao leagueRepository;
    private LeagueRepository leagueRepository;

    @Autowired
    //private ResultDao resultRepository;
    private ResultRepository resultRepository;

    @Autowired
    //private TournamentDao tournamentRepository;
    private TournamentRepository tournamentRepository;



    @Override
    @Transactional
    public Team getTeamById(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(id), "Team", id);
    }

    @Override
    @Transactional
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    @Transactional
    public void saveTeam(Team team) {
        teamRepository.save(team);
    }

    @Override
    @Transactional
    public void deleteTeam(long id) {
        teamRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<Team> teamsByLeague(long leagueId) {
        return teamRepository.findAllByLeagueId(leagueId);
    }

    @Override
    @Transactional
    public Team addPlayerToTeam(long teamId, long playerId) {
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerRepository.findById(playerId), "Player", playerId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        team.addPlayerToTeam(player);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team deletePlayerFromTeam(long teamId, long playerId) {
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerRepository.findById(playerId), "Player", playerId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        team.deletePlayerFromTeam(player);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team addFlagToTeam(long teamId, long flagId) {
        Flag flag = EntityRetrievalUtils.getEntityOrThrow(flagRepository.findById(flagId), "Flag", flagId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        team.addFlagToTeam(flag);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team addLeagueToTeam(long teamId, long leagueId) {
        League league = EntityRetrievalUtils.getEntityOrThrow(leagueRepository.findById(leagueId), "League", leagueId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        team.setLeague(league);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team deleteFlagFromTeam(long teamId, long flagId) {
        Flag flag = EntityRetrievalUtils.getEntityOrThrow(flagRepository.findById(flagId), "Flag", flagId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        team.deleteFlagFromTeam(flag);
        teamRepository.save(team);
        return team;
    }

    @Override
    @Transactional
    public Team getTeamByIdSite(long idSite) {
        return EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(idSite), "Team (by idSite)", idSite);
    }

    @Override
    @Transactional
    public List<InfoTeamResults> getInfoTeamResultsByTeam(long teamId) {
        List<InfoTeamResults> infoTeamResults = new ArrayList<>();
        Team team = getTeamById(teamId);
        List<FullResult> results = resultRepository.findAllByTeamId(teamId);

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

        List<Tournament> tournaments = tournamentRepository.findAllByTeamId(teamId);
        HashMap<Tournament, List<Player>> tournamentsPlayers = new HashMap<>();
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        for (Tournament tournament : tournaments) {
            List<Player> players = tournament.getPlayers().stream().filter(x -> x.getTeams().contains(team)).toList();
            tournamentsPlayers.put(tournament, players);
        }
        return tournamentsPlayers;

    }

    public boolean existsByIdSite(long idSite){
        return teamRepository.existsByIdSite(idSite);
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
        return EntityRetrievalUtils.getEntityOrThrow(teamRepository.findByPlayerIdAndLeagueId(playerId, leagueId), "Team (by playerId and teamId)", playerId);
    }

}
