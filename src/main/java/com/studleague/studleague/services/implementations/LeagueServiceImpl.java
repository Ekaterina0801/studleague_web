package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.league.LeagueDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.mappers.league.LeagueMapper;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.repository.security.UserRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private LeagueMapper leagueMapper;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private SystemResultRepository systemResultRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(readOnly = true)
    public League getLeagueById(Long id) {
        return entityRetrievalUtils.getLeagueOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "leagues")
    public List<League> getAllLeagues() {
        return leagueRepository.findAll();
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "leagues", allEntries = true),
    })
    public void saveLeague(League league) {

        String name = league.getName();
        Long id = league.getId();
        for (User user : league.getManagers()) {
            user.addLeague(league);
        }
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
    @Caching(evict = {
            @CacheEvict(value = "leagues", allEntries = true),
            @CacheEvict(value = "leagueResults", key = "#leagueId")
    })
    public void deleteLeague(Long leagueId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        for (User user : new ArrayList<>(league.getManagers())) {
            league.deleteManager(user);
        }
        for (Flag flag : new ArrayList<>(league.getFlags())) {
            flag.getTeams().clear();
        }

        for (Tournament tournament : new ArrayList<>(league.getTournaments())) {
            deleteTournamentFromLeague(leagueId, tournament.getId());
        }
        for (Team team : new ArrayList<>(league.getTeams())) {
            team.getTeamCompositions().clear();
            for (Flag flag : new ArrayList<>(team.getFlags())) {
                team.deleteFlagFromTeam(flag);
            }
        }
        league.getFlags().clear();
        leagueRepository.delete(league);
    }



    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", key = "#leagueId")
    public League addTournamentToLeague(Long leagueId, Long tournamentId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        league.addTournamentToLeague(tournament);
        leagueRepository.save(league);
        return league;
    }

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", key = "#leagueId")
    public League deleteTournamentFromLeague(Long leagueId, Long tournamentId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        List<Team> teams = new ArrayList<>(tournament.getTeams());
        tournament.getTeamCompositions().clear();
        tournament.getResults().clear();
        tournament.getTeamCompositions().clear();
        for (Team team : teams) {
            tournament.deleteTeam(team);
            league.deleteTeamFromLeague(team);
        }
        league.deleteTournamentFromLeague(tournament);
        leagueRepository.save(league);
        return league;
    }


    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", key = "#leagueId")
    public League deleteTeamFromLeague(Long leagueId, Long teamId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        List<Tournament> tournaments = new ArrayList<>(team.getTournaments());
        for (Tournament tournament : tournaments) {
            team.deleteTournamentFromTeam(tournament);
        }
        team.getTeamCompositions().clear();
        team.getResults().clear();
        league.deleteTeamFromLeague(team);
        leagueRepository.save(league);
        return league;
    }


    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "leagues", allEntries = true),
            @CacheEvict(value = "leagueResults", allEntries = true)
    })
    public void deleteAllLeagues() {
        leagueRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
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
        League league = leagueMapper.mapToEntity(leagueDTO);
        User user = entityRetrievalUtils.getUserOrThrow(userId);
        return league.getManagers().contains(user);
    }

    @Override
    @Transactional(readOnly = true)
    public League getLeagueWithResults(Long leagueId) {
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        for (Team team : league.getTeams()) {
            List<FullResult> results = resultRepository.findAllByTeamId(team.getId());
            team.setResults(results);
        }

        return league;
    }

    @Override
    @CacheEvict(value = "leagueResults", key = "#leagueId")
    public League changeSystemResultOfLeague(Long leagueId, Long systemResultId) {
        League league = getLeagueById(leagueId);
        if (systemResultRepository.existsById(systemResultId)) {

            SystemResult systemResult = entityRetrievalUtils.getSystemResultOrThrow(systemResultId);
            league.setSystemResult(systemResult);
            saveLeague(league);
        }
        return league;
    }

    @Override
    @CacheEvict(value = "leagueResults", key = "#leagueId")
    public League changeCountExcludedGamesOfLeague(Long leagueId, Integer countGames) {
        League league = getLeagueById(leagueId);
        league.setCountExcludedGames(countGames);
        saveLeague(league);
        return league;
    }

    @Override
    public void addManager(Long leagueId, Long managerId) {
        User user = entityRetrievalUtils.getUserOrThrow(managerId);
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        league.addManager(user);
        saveLeague(league);
    }

    @Override
    public void deleteManager(Long leagueId, Long managerId) {
        User user = entityRetrievalUtils.getUserOrThrow(managerId);
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        league.deleteManager(user);
        saveLeague(league);
    }





}
