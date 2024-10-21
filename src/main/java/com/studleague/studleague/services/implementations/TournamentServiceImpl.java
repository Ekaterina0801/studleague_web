package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappings.TournamentMapper;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private LeagueDao leagueDao;


    @Override
    @Transactional
    public Tournament getTournamentById(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(id), "Tournament", id);
    }

    @Override
    @Transactional
    public List<Tournament> getAllTournaments() {
        return tournamentDao.getAllTournaments();
    }

    @Override
    @Transactional
    public void saveTournament(Tournament tournament) {
        tournamentDao.saveTournament(tournament);
    }

    @Override
    @Transactional
    public void deleteTournament(long id) {
        tournamentDao.deleteTournament(id);
    }

    @Override
    @Transactional
    public Tournament addResultToTournament(long tournamentId, long resultId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(tournamentId), "Tournament", tournamentId);
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(fullResultDao.getFullResultById(resultId), "FullResult", resultId);
        tournament.addResult(fullResult);
        tournamentDao.saveTournament(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deleteResultFromTournament(long tournamentId, long resultId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(tournamentId), "Tournament", tournamentId);
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(fullResultDao.getFullResultById(resultId), "FullResult", resultId);
        tournament.deleteResult(fullResult);
        tournamentDao.saveTournament(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addPlayerToTournament(long tournamentId, long playerId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(tournamentId), "Tournament", tournamentId);
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerDao.getPlayerById(playerId), "Player", playerId);
        tournament.addPlayer(player);
        player.addTournamentToPlayer(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deletePlayerFromTournament(long tournamentId, long playerId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(tournamentId), "Tournament", tournamentId);
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerDao.getPlayerById(playerId), "Player", playerId);
        tournament.deletePlayer(player);
        tournamentDao.saveTournament(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deleteTeamFromTournament(long tournamentId, long teamId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(tournamentId), "Tournament", tournamentId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        tournament.deleteTeam(team);
        tournamentDao.saveTournament(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addTeamToTournament(long tournamentId, long teamId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(tournamentId), "Tournament", tournamentId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        tournament.addTeam(team);
        team.addTournamentToTeam(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addTeamAndPlayerToTournament(long tournamentId, long teamId, long playerId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(tournamentId), "Tournament", tournamentId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamDao.getTeamById(teamId), "Team", teamId);
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerDao.getPlayerById(playerId), "Player", playerId);

        addTeamToTournament(tournament, team);
        addPlayerToTeam(team, player);
        addPlayerToTournament(tournament, player);

        tournamentDao.saveTournament(tournament);
        return tournament;
    }

    private void addTeamToTournament(Tournament tournament, Team team) {
        if (!tournament.getTeams().contains(team)) {
            tournament.addTeam(team);
            team.addTournamentToTeam(tournament);
        }
    }

    private void addPlayerToTeam(Team team, Player player) {
        if (!team.getPlayers().contains(player)) {
            team.addPlayerToTeam(player);
            player.addTeamToPlayer(team);
        }
    }

    private void addPlayerToTournament(Tournament tournament, Player player) {
        if (!tournament.getPlayers().contains(player)) {
            tournament.addPlayer(player);
            player.addTournamentToPlayer(tournament);
        }
    }

    @Override
    public Tournament getTournamentBySiteId(long idSite) {
        return EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentBySiteId(idSite), "Tournament", idSite);
    }

    @Override
    public boolean existsByIdSite(long idSite)
    {
        return tournamentDao.existsByIdSite(idSite);
    }

}
