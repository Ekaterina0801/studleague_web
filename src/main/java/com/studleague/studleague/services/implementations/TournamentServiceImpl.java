package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.TournamentDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.mappers.TournamentMapper;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TournamentService;
import com.studleague.studleague.specifications.TournamentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service("tournamentService")
public class TournamentServiceImpl implements TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentMapper tournamentMapper;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private CacheManager cacheManager;


    @Override
    @Transactional(readOnly = true)
    public Tournament getTournamentById(Long id) {
        return entityRetrievalUtils.getTournamentOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", key = "#tournament.leagues.isEmpty() ? null : #tournament.leagues[0].id")
    public void saveTournament(Tournament tournament) {
        Long idSite = tournament.getIdSite();
        Long id = tournament.getId();
        if (id != null) {
            if (tournamentRepository.existsById(id)) {
                Tournament existingTournament = entityRetrievalUtils.getTournamentOrThrow(id);
                updateTournament(existingTournament, tournament);
            } else if (tournamentRepository.existsByNameIgnoreCase(tournament.getName())) {
                Tournament existingTournament = entityRetrievalUtils.getTournamentByNameOrThrow(tournament.getName());
                updateTournament(existingTournament, tournament);
            }
        } else if (idSite != null) {
            if (tournamentRepository.existsByIdSite(idSite)) {
                Tournament existingTournament = entityRetrievalUtils.getTournamentByIdSiteOrThrow(idSite);
                updateTournament(existingTournament, tournament);
            } else {
                tournamentRepository.save(tournament);
            }
        } else {
            if (tournamentRepository.existsByNameIgnoreCase(tournament.getName())) {
                Tournament existingTournament = entityRetrievalUtils.getTournamentByNameOrThrow(tournament.getName());
                updateTournament(existingTournament, tournament);
            } else tournamentRepository.save(tournament);
        }
    }

    @Transactional
    private void updateTournament(Tournament existingTournament, Tournament tournament) {
        existingTournament.setIdSite(tournament.getIdSite());
        existingTournament.setName(tournament.getName());
        existingTournament.setDateOfEnd(tournament.getDateOfEnd());
        existingTournament.setDateOfStart(tournament.getDateOfStart());
        tournamentRepository.save(existingTournament);
    }

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", allEntries = true)
    public void deleteTournament(Long id) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(id);
        for (Team team : List.copyOf(tournament.getTeams())) {
            team.deleteTournamentFromTeam(tournament);
        }
        tournamentRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Tournament addResultToTournament(Long tournamentId, Long resultId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        FullResult fullResult = entityRetrievalUtils.getResultOrThrow(resultId);
        clearLeagueCaches(tournament);
        tournament.addResult(fullResult);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deleteResultFromTournament(Long tournamentId, Long resultId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        FullResult fullResult = entityRetrievalUtils.getResultOrThrow(resultId);
        clearLeagueCaches(tournament);
        tournament.deleteResult(fullResult);
        resultRepository.delete(fullResult);
        tournamentRepository.save(tournament);
        return tournament;
    }

    private void clearLeagueCaches(Tournament tournament) {
        if (tournament.getLeagues() != null) {
            for (League league : tournament.getLeagues()) {
                Long leagueId = league.getId();
                if (leagueId != null) {
                    if (cacheManager != null) {
                        Cache cache = cacheManager.getCache("leagueResults");
                        if (cache != null) {
                            cache.evict(leagueId);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public Tournament addPlayerToTournament(Long tournamentId, Long playerId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);
        tournament.addPlayer(player);
        player.addTournamentToPlayer(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deletePlayerFromTournament(Long tournamentId, Long playerId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);
        tournament.deletePlayer(player);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", key = "#tournamentId")
    public Tournament deleteTeamFromTournament(Long tournamentId, Long teamId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        tournament.deleteTeam(team);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", key = "#leagueId")
    public Tournament addTeamToTournament(Long tournamentId, Long teamId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        TeamComposition teamComposition =
                TeamComposition.builder().
                        parentTeam(team).
                        build();
        tournament.addTeam(team);
        tournament.addTeamComposition(teamComposition);
        team.addTournamentToTeam(tournament);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", key = "#leagueId")
    public Tournament addTeamAndPlayerToTournament(Long tournamentId, Long teamId, Long playerId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);

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
    @Transactional(readOnly = true)
    public Tournament getTournamentBySiteId(Long idSite) {
        return entityRetrievalUtils.getTournamentByIdSiteOrThrow(idSite);
    }


    @Override
    public boolean existsByIdSite(Long idSite) {
        return tournamentRepository.existsByIdSite(idSite);
    }

    @Override
    @Transactional
    @CacheEvict(value = "leagueResults", allEntries = true)
    public void deleteAllTournaments() {
        tournamentRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public HashMap<Team, List<Player>> getTeamsPlayersByTournamentId(Long tournamentId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        HashMap<Team, List<Player>> teamsPlayers = new HashMap<>();
        List<Team> teams = tournament.getTeams();
        for (Team team : teams) {
            List<Player> players = team.getPlayers();
            teamsPlayers.put(team, players);
        }

        return teamsPlayers;
    }

    @Override
    public boolean isManager(Long userId, Long tournamentId) {
        if (userId == null)
            return false;
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        for (League league : tournament.getLeagues())
            if (leagueService.isManager(userId, league.getId()))
                return true;
        return false;
    }

    @Override
    public boolean isManager(Long userId, TournamentDTO tournamentDTO) {
        if (userId == null)
            return false;
        Tournament tournament = tournamentMapper.mapToEntity(tournamentDTO);
        for (League league : tournament.getLeagues())
            if (leagueService.isManager(userId, league.getId()))
                return true;
        return false;
    }

    @Override
    public Page<Tournament> searchTournaments(String name, Long leagueId, LocalDateTime startDate, LocalDateTime endDate, Sort sort, Pageable pageable) {
        Specification<Tournament> spec = TournamentSpecification.searchTournaments(name, leagueId, startDate, endDate, sort);
        return tournamentRepository.findAll(spec, pageable);
    }


}
