package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.LeagueDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.factory.LeagueFactory;
import com.studleague.studleague.repository.*;
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
public class LeagueServiceImpl implements LeagueService {

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

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private SystemResultRepository systemResultRepository;

    @Autowired
    private TeamRepository teamRepository;


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
        Long id = league.getId();
        if (id != null) {
            if (leagueRepository.existsById(id)) {
                League existingLeague = entityRetrievalUtils.getLeagueOrThrow(id);
                updateLeague(existingLeague, league);
            }
        } else if (leagueRepository.existsByNameIgnoreCase(name)) {
            League existingLeague = entityRetrievalUtils.getLeagueByNameOrThrow(name);
            updateLeague(existingLeague, league);
        } else {
            leagueRepository.save(league);
        }
    }

    @Transactional
    private void updateLeague(League existingLeague, League newLeague) {
        existingLeague.setName(newLeague.getName());
        existingLeague.setSystemResult(newLeague.getSystemResult());
        existingLeague.setTeams(newLeague.getTeams());
        existingLeague.setTournaments(newLeague.getTournaments());
        existingLeague.setCreatedBy(newLeague.getCreatedBy());
        existingLeague.setManagers(newLeague.getManagers());
        leagueRepository.save(existingLeague);
    }

    @Override
    @Transactional
    public void deleteLeague(Long id) {
        League league  = entityRetrievalUtils.getLeagueOrThrow(id);
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
    public boolean isManager(Long userId, Long leagueId) {
        if (userId == null)
            return false;
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        User user = entityRetrievalUtils.getUserOrThrow(userId);
        return league.getManagers().contains(user);
    }

    @Override
    public boolean isManager(Long userId, LeagueDTO leagueDTO) {
        if (userId == null)
            return false;
        League league = leagueFactory.mapToEntity(leagueDTO);
        User user = entityRetrievalUtils.getUserOrThrow(userId);
        return league.getManagers().contains(user);
    }

    @Override
    public League getLeagueWithResults(Long leagueId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        for (Team team : league.getTeams()) {
            List<FullResult> results = resultRepository.findAllByTeamId(team.getId());
            team.setResults(results);
        }

        return league;
    }

    @Override
    public League changeSystemResultOfLeague(Long leagueId, Long systemResultId) {
        League league = getLeagueById(leagueId);
        if (systemResultRepository.existsById(systemResultId)) {

            SystemResult systemResult = entityRetrievalUtils.getSystemResultOrThrow(systemResultId);
            league.setSystemResult(systemResult);
            saveLeague(league);
        }
        return league;
    }

}
