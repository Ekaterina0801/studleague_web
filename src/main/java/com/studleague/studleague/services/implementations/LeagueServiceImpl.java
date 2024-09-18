package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.implementations.LeagueDaoImpl;
import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
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
    public League getLeagueById(int id) {
        League league = leagueDao.getLeagueById(id);
        return league;
    }

    @Override
    @Transactional
    public List<League> getAllLeagues() {
        List<League> leagues = leagueDao.getAllLeagues();
        return leagues;
    }

    @Override
    @Transactional
    public void saveLeague(League league) {
        leagueDao.saveLeague(league);
    }

    @Override
    @Transactional
    public void updateLeague(League league, String[] params) {
        leagueDao.updateLeague(league, params);
    }

    @Override
    @Transactional
    public void deleteLeague(int id) {
        leagueDao.deleteLeague(id);
    }

    @Override
    @Transactional
    public League addTournamentToLeague(int league_id, int tournament_id) {
        League league = leagueDao.getLeagueById(league_id);
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        league.addTournamentToLeague(tournament);
        //player.addTeamToPlayer(team);
        leagueDao.saveLeague(league);
        //playerDao.savePlayer(player);
        return league;
    }

    @Override
    @Transactional
    public League deleteTournamentToLeague(int league_id, int tournament_id) {
        League league = leagueDao.getLeagueById(league_id);
        Tournament tournament = tournamentDao.getTournamentById(tournament_id);
        league.deleteTournamentFromLeague(tournament);
        //player.addTeamToPlayer(team);
        leagueDao.saveLeague(league);
        //playerDao.savePlayer(player);
        return league;
    }
}
