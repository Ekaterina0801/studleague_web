package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.TournamentDTO;
import com.studleague.studleague.entities.*;
import com.studleague.studleague.factory.TournamentFactory;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TournamentService;
import com.studleague.studleague.specifications.TournamentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TournamentFactory tournamentFactory;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;


    @Override
    @Transactional
    public Tournament getTournamentById(Long id) {
        return entityRetrievalUtils.getTournamentOrThrow(id);
    }

    @Override
    @Transactional
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    @Override
    @Transactional
    public void saveTournament(Tournament tournament) {

        Long idSite = tournament.getIdSite();
        Long id = tournament.getId();
        if (id != null) {
            if (tournamentRepository.existsById(id)) {
                Tournament existingTournament = entityRetrievalUtils.getTournamentOrThrow(id);
                updateTournament(existingTournament, tournament);
            }
        } else if (idSite != 0) {
            if (tournamentRepository.existsByIdSite(idSite)) {
                Tournament existingTournament = entityRetrievalUtils.getTournamentByIdSiteOrThrow(idSite);
                updateTournament(existingTournament, tournament);

            } else {
                tournamentRepository.save(tournament);
            }
        } else {
            tournamentRepository.save(tournament);
        }
    }

    @Transactional
    private void updateTournament(Tournament existingTournament, Tournament tournament) {
        existingTournament.setIdSite(tournament.getIdSite());
        existingTournament.setPlayers(tournament.getPlayers());
        existingTournament.setTeams(tournament.getTeams());
        existingTournament.setResults(tournament.getResults());
        existingTournament.setName(tournament.getName());
        existingTournament.setLeagues(tournament.getLeagues());
        existingTournament.setDateOfEnd(tournament.getDateOfEnd());
        existingTournament.setDateOfStart(tournament.getDateOfStart());
        tournamentRepository.save(existingTournament);
    }

    @Override
    @Transactional
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
        tournament.addResult(fullResult);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament deleteResultFromTournament(Long tournamentId, Long resultId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        FullResult fullResult = entityRetrievalUtils.getResultOrThrow(resultId);
        tournament.deleteResult(fullResult);
        tournamentRepository.save(tournament);
        return tournament;
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
    public Tournament deleteTeamFromTournament(Long tournamentId, Long teamId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        tournament.deleteTeam(team);
        tournamentRepository.save(tournament);
        return tournament;
    }

    @Override
    @Transactional
    public Tournament addTeamToTournament(Long tournamentId, Long teamId) {
        Tournament tournament = entityRetrievalUtils.getTournamentOrThrow(tournamentId);
        Team team = entityRetrievalUtils.getTeamOrThrow(teamId);
        tournament.addTeam(team);
        team.addTournamentToTeam(tournament);
        return tournament;
    }

    @Override
    @Transactional
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
    public Tournament getTournamentBySiteId(Long idSite) {
        return entityRetrievalUtils.getTournamentByIdSiteOrThrow(idSite);
    }


    @Override
    public boolean existsByIdSite(Long idSite) {
        return tournamentRepository.existsByIdSite(idSite);
    }

    @Override
    @Transactional
    public void deleteAllTournaments() {
        tournamentRepository.deleteAll();
    }

    @Override
    @Transactional
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
        Tournament tournament = tournamentFactory.mapToEntity(tournamentDTO);
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
