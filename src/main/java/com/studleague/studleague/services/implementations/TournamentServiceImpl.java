package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.*;
import com.studleague.studleague.dto.TournamentDto;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappings.TournamentMapper;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    FullResultDao fullResultDao;

    @Autowired
    PlayerDao playerDao;

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private LeagueDao leagueDao;


    @Override
    @Transactional
    public Tournament getTournamentById(int id) {
        Tournament tournament = tournamentDao.getTournamentById(id);
        return tournament;
    }

    @Override
    @Transactional
    public List<Tournament> getAllTournaments() {
        List<Tournament> tournaments = tournamentDao.getAllTournaments();
        return tournaments;
    }

    @Override
    @Transactional
    public void saveTournament(Tournament tournament) {
        tournamentDao.saveTournament(tournament); // Сохраняем турнир
    }


    @Override
    @Transactional
    public void updateTournament(Tournament tournament, String[] params) {
        tournamentDao.updateTournament(tournament, params);
    }

    @Override
    @Transactional
    public void deleteTournament(int id) {
        tournamentDao.deleteTournament(id);
    }

    @Override
    @Transactional
    public Tournament addResultToTournament(int tournament_id, int result_id) {
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        FullResult fullResult = fullResultDao.getFullResultById(result_id);
        tournament.addResult(fullResult);
        //player.addTeamToPlayer(team);
        tournamentDao.saveTournament(tournament);
        //playerDao.savePlayer(player);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deleteResultFromTournament(int tournament_id, int result_id) {
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        FullResult fullResult = fullResultDao.getFullResultById(result_id);
        tournament.deleteResult(fullResult);
        //player.addTeamToPlayer(team);
        tournamentDao.saveTournament(tournament);
        //playerDao.savePlayer(player);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addPlayerToTournament(int tournament_id, int player_id){
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        Player player = playerDao.getPlayerById(player_id);
        tournament.addPlayer(player);
        player.addTournamentToPlayer(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deletePlayerFromTournament(int tournament_id, int player_id) {
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        Player player = playerDao.getPlayerById(player_id);
        tournament.deletePlayer(player);
        //player.addTeamToPlayer(team);
        tournamentDao.saveTournament(tournament);
        //playerDao.savePlayer(player);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deleteTeamFromTournament(int tournament_id, int team_id) {
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        Team team  = teamDao.getTeamById(team_id);
        tournament.deleteTeam(team);
        //player.addTeamToPlayer(team);
        tournamentDao.saveTournament(tournament);
        //playerDao.savePlayer(player);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addTeamToTournament(int tournament_id, int team_id) {
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        Team team  = teamDao.getTeamById(team_id);
        tournament.addTeam(team);
        team.addTournamentToTeam(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addTeamAndPlayerToTournament(int tournament_id, int team_id, int player_id) {
        // Retrieve the tournament, team, and player
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        Team team = teamDao.getTeamById(team_id);
        Player player = playerDao.getPlayerById(player_id);

        if (!tournament.getTeams().contains(team)) {
            tournament.addTeam(team);
            team.addTournamentToTeam(tournament);
        }

        if (!team.getPlayers().contains(player)) {
            team.addPlayerToTeam(player);
            player.addTeamToPlayer(team);
        }

        if (!tournament.getPlayers().contains(player)) {
            tournament.addPlayer(player);
            player.addTournamentToPlayer(tournament);
        }

        tournamentDao.saveTournament(tournament);

        return tournament;
    }

    public Tournament getTournamentBySiteId(String idSite)
    {
        return tournamentDao.getTournamentBySiteId(idSite);
    }


}
