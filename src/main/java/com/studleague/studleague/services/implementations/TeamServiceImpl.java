package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.FlagDao;
import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
