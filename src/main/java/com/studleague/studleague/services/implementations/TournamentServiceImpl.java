package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.*;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappings.TournamentMapper;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private TournamentDao tournamentRepository;

    @Autowired
    private ResultDao resultRepository;

    @Autowired
    private PlayerDao playerRepository;

    @Autowired
    private TeamDao teamRepository;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private LeagueDao leagueRepository;


    @Override
    @Transactional
    public Tournament getTournamentById(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(id), "Tournament", id);
    }

    @Override
    @Transactional
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    @Override
    @Transactional
    public void saveTournament(Tournament tournament) {

        long idSite = tournament.getIdSite();
        if (tournamentRepository.existsByIdSite(idSite))
        {
            tournamentRepository.save(EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findByIdSite(idSite), "Tournament", idSite));
        }
        else {
            tournamentRepository.save(tournament);
        }
    }

    @Override
    @Transactional
    public void deleteTournament(long id) {
        tournamentRepository.deleteByTournamentId(id);
    }

    @Override
    @Transactional
    public Tournament addResultToTournament(long tournamentId, long resultId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(resultRepository.findById(resultId), "FullResult", resultId);
        tournament.addResult(fullResult);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deleteResultFromTournament(long tournamentId, long resultId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(resultRepository.findById(resultId), "FullResult", resultId);
        tournament.deleteResult(fullResult);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addPlayerToTournament(long tournamentId, long playerId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerRepository.findById(playerId), "Player", playerId);
        tournament.addPlayer(player);
        player.addTournamentToPlayer(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deletePlayerFromTournament(long tournamentId, long playerId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerRepository.findById(playerId), "Player", playerId);
        tournament.deletePlayer(player);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deleteTeamFromTournament(long tournamentId, long teamId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        tournament.deleteTeam(team);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addTeamToTournament(long tournamentId, long teamId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        tournament.addTeam(team);
        team.addTournamentToTeam(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addTeamAndPlayerToTournament(long tournamentId, long teamId, long playerId) {
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
        Team team = EntityRetrievalUtils.getEntityOrThrow(teamRepository.findById(teamId), "Team", teamId);
        Player player = EntityRetrievalUtils.getEntityOrThrow(playerRepository.findById(playerId), "Player", playerId);

        addTeamToTournament(tournament, team);
        addPlayerToTeam(team, player);
        addPlayerToTournament(tournament, player);

        tournamentRepository.save(tournament);
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
        return EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findByIdSite(idSite), "Tournament", idSite);
    }

    @Override
    public boolean existsByIdSite(long idSite)
    {
        return tournamentRepository.existsByIdSite(idSite);
    }

}
