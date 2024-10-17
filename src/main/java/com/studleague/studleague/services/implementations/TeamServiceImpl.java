package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.*;
import com.studleague.studleague.dto.InfoTeamResults;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.services.interfaces.FullResultService;
import com.studleague.studleague.services.interfaces.TeamService;
import com.studleague.studleague.services.interfaces.TournamentService;
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
    private TournamentService tournamentService;



    @Override
    @Transactional
    public Team getTeamById(int id) {
        Team team  = teamDao.getTeamById(id);
        return team;
    }

    @Override
    @Transactional
    public List<Team> getAllTeams() {
        List<Team> teams = teamDao.getAllTeams();
        return teams;
    }

    @Override
    @Transactional
    public void saveTeam(Team team) {
        teamDao.saveTeam(team);

    }

    @Override
    @Transactional
    public void updateTeam(Team team, String[] params) {
        teamDao.updateTeam(team,params);
    }

    @Override
    @Transactional
    public void deleteTeam(int id) {
        teamDao.deleteTeam(id);
    }

    @Override
    @Transactional
    public List<Team> teamsByLeague(int league_id) {
        List<Team> teamsByLeague = teamDao.teamsByLeague(league_id);
        return teamsByLeague;
    }

    @Override
    @Transactional
    public Team addPlayerToTeam(int team_id, int player_id) {
        Player player = playerDao.getPlayerById(player_id);
        Team team = teamDao.getTeamById(team_id);
        team.addPlayerToTeam(player);
        //player.addTeamToPlayer(team);
        teamDao.saveTeam(team);
        //playerDao.savePlayer(player);
        return team;
    }

    @Override
    @Transactional
    public Team deletePlayerFromTeam(int team_id, int player_id) {
        Player player = playerDao.getPlayerById(player_id);
        Team team = teamDao.getTeamById(team_id);
        team.deletePlayerFromTeam(player);
        //player.addTeamToPlayer(team);
        teamDao.saveTeam(team);
        //playerDao.savePlayer(player);
        return team;
    }

    @Override
    @Transactional
    public Team addFlagToTeam(int team_id, int flag_id) {
        Flag flag = flagDao.getFlagById(flag_id);
        Team team = teamDao.getTeamById(team_id);
        team.addFlagToTeam(flag);
        //player.addTeamToPlayer(team);
        teamDao.saveTeam(team);
        //playerDao.savePlayer(player);
        return team;
    }

    @Override
    @Transactional
    public Team addLeagueToTeam(int team_id, int league_id) {
        League league = leagueDao.getLeagueById(league_id);
        Team team = teamDao.getTeamById(team_id);
        team.setLeague(league);
        teamDao.saveTeam(team);
        return team;
    }

    @Override
    @Transactional
    public Team deleteFlagFromTeam(int team_id, int flag_id) {
        Flag flag = flagDao.getFlagById(flag_id);
        Team team = teamDao.getTeamById(team_id);
        team.deleteFlagFromTeam(flag);
        //player.addTeamToPlayer(team);
        teamDao.saveTeam(team);
        //playerDao.savePlayer(player);
        return team;
    }

    @Override
    @Transactional
    public Team getTeamByIdSite(String idSite)
    {
        return teamDao.getTeamByIdSite(idSite);
    }

    @Override
    @Transactional
    public HashMap<Tournament, List<Player>> getTournamentsPlayersByTeam(int team_id)
    {
        List<Tournament> tournaments = teamDao.tournamentsByTeam(team_id);
        HashMap<Tournament,List<Player>> tournamentsPlayers = new HashMap<>();
        Team team = teamDao.getTeamById(team_id);

        for (Tournament tournament: tournaments)
        {
            List<Player> players = tournament.getPlayers().stream().filter(x->x.getTeams().contains(team)).toList();
            tournamentsPlayers.put(tournament,players);
        }
        return tournamentsPlayers;

    }

    @Override
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
}
