package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeagueServiceImpl implements LeagueService {

    @Autowired
    LeagueDao leagueDao;

    @Autowired
    TournamentDao tournamentDao;


    @Override
    @Transactional
    public League getLeagueById(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(leagueDao.getLeagueById(id), "League", id);
    }

    @Override
    @Transactional
    public List<League> getAllLeagues() {
        return leagueDao.getAllLeagues();
    }

    @Override
    @Transactional
    public void saveLeague(League league) {
        leagueDao.saveLeague(league);
    }

    @Override
    @Transactional
    public void deleteLeague(long id) {
        leagueDao.deleteLeague(id);
    }

    @Override
    @Transactional
    public League addTournamentToLeague(long leagueId, long tournamentId) {
        League league = EntityRetrievalUtils.getEntityOrThrow(leagueDao.getLeagueById(leagueId), "League", leagueId);
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(tournamentId), "Tournament", tournamentId);
        league.addTournamentToLeague(tournament);
        tournamentDao.saveTournament(tournament);
        leagueDao.saveLeague(league);
        return league;
    }

    @Override
    @Transactional
    public League deleteTournamentToLeague(long leagueId, long tournamentId) {
        League league = EntityRetrievalUtils.getEntityOrThrow(leagueDao.getLeagueById(leagueId), "League", leagueId);
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentDao.getTournamentById(leagueId), "Tournament", tournamentId);
        league.deleteTournamentFromLeague(tournament);
        leagueDao.saveLeague(league);
        return league;
    }
}
