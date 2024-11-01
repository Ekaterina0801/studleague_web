package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.LeagueDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.factory.LeagueFactory;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.TournamentRepository;
import com.studleague.studleague.repository.security.UserRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("leagueService")
public class LeagueServiceImpl implements LeagueService{

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueFactory leagueFactory;


    @Override
    @Transactional
    public League getLeagueById(Long id) {
        return entityRetrievalUtils.getLeagueOrThrow(id);
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
        if (leagueRepository.existsByNameIgnoreCase(name)) {
            leagueRepository.save(entityRetrievalUtils.getLeagueByNameOrThrow(name));
        } else {
            leagueRepository.save(league);
        }
    }

    @Override
    @Transactional
    public void deleteLeague(Long id) {
        leagueRepository.deleteById(id);
    }

    @Override
    @Transactional
    public League addTournamentToLeague(Long leagueId, Long tournamentId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        league.addTournamentToLeague(tournament);
        tournamentRepository.save(tournament);
        leagueRepository.save(league);
        return league;
    }

    @Override
    @Transactional
    public League deleteTournamentToLeague(Long leagueId, Long tournamentId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        league.deleteTournamentFromLeague(tournament);
        leagueRepository.save(league);
        return league;
    }

    @Override
    @Transactional
    public void deleteAllLeagues() {
        leagueRepository.deleteAll();
    }

    @Override
    @Transactional
    public List<League> getLeaguesForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return leagueRepository.findAllByCreatedById(currentUser.getId());
    }

    @Override
    public boolean isManager(Long userId, Long leagueId)
    {
        if (userId==null)
            return false;
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        User user = entityRetrievalUtils.getUserOrThrow(userId);
        return league.getManagers().contains(user);
    }

    @Override
    public boolean isManager(Long userId, LeagueDTO leagueDTO)
    {
        if (userId==null)
            return false;
        League league = leagueFactory.toEntity(leagueDTO);
        User user = entityRetrievalUtils.getUserOrThrow(userId);
        return league.getManagers().contains(user);
    }

}
