package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeagueServiceImpl implements LeagueService {

    @Autowired
    //private LeagueDao leagueRepository;
    private LeagueRepository leagueRepository;

    @Autowired
    //private TournamentDao tournamentRepository;
    private TournamentRepository tournamentRepository;


    @Override
    @Transactional
    public League getLeagueById(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(leagueRepository.findById(id), "League", id);
    }

    @Override
    @Transactional
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    @Override
    @Transactional
    public void saveLeague(League league) {

        String name = league.getName();
        if (leagueRepository.existsByNameIgnoreCase(name))
        {
            leagueRepository.save(EntityRetrievalUtils.getEntityByNameOrThrow(leagueRepository.findByNameIgnoreCase(name), "Flag", name));
        }
        else {
            leagueRepository.save(league);
        }
    }

    @Override
    @Transactional
    public void deleteLeague(long id) {
        leagueRepository.deleteById(id);
    }

    @Override
    @Transactional
    public League addTournamentToLeague(long leagueId, long tournamentId) {
        League league = EntityRetrievalUtils.getEntityOrThrow(leagueRepository.findById(leagueId), "League", leagueId);
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(tournamentId), "Tournament", tournamentId);
        league.addTournamentToLeague(tournament);
        tournamentRepository.save(tournament);
        leagueRepository.save(league);
        return league;
    }

    @Override
    @Transactional
    public League deleteTournamentToLeague(long leagueId, long tournamentId) {
        League league = EntityRetrievalUtils.getEntityOrThrow(leagueRepository.findById(leagueId), "League", leagueId);
        Tournament tournament = EntityRetrievalUtils.getEntityOrThrow(tournamentRepository.findById(leagueId), "Tournament", tournamentId);
        league.deleteTournamentFromLeague(tournament);
        leagueRepository.save(league);
        return league;
    }

    @Override
    @Transactional
    public void deleteAllLeagues()
    {
        leagueRepository.deleteAll();
    }
}
