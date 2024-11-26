package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.factory.PlayerFactory;
import com.studleague.studleague.repository.*;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.PlayerService;
import com.studleague.studleague.specifications.PlayerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service("playerService")
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private PlayerFactory playerFactory;

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;

    @Autowired
    private TournamentRepository tournamentRepository;


    @Override
    @Transactional
    public Player getPlayerById(Long id) {
        return entityRetrievalUtils.getPlayerOrThrow(id);
    }

    @Override
    @Transactional
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    @Transactional
    public void savePlayer(Player player) {
        Long idSite = player.getIdSite();
        Long id = player.getId();
        if (id != null) {
            if (playerRepository.existsById(id)) {
                Player existingPlayer = entityRetrievalUtils.getPlayerOrThrow(id);
                updatePlayer(existingPlayer, player);
            }
        } else if (idSite != null) {
            if (playerRepository.existsByIdSite(idSite)) {
                Player existingPlayer = entityRetrievalUtils.getPlayerByIdSiteOrThrow(idSite);
                updatePlayer(existingPlayer, player);
            } else {
                playerRepository.save(player);
            }
        } else {
            playerRepository.save(player);
        }
    }

    @Transactional
    private void updatePlayer(Player existingPlayer, Player newPlayer) {
        existingPlayer.setName(newPlayer.getName());
        existingPlayer.setSurname(newPlayer.getSurname());
        existingPlayer.setPatronymic(newPlayer.getPatronymic());
        existingPlayer.setTransfers(newPlayer.getTransfers());
        existingPlayer.setDateOfBirth(newPlayer.getDateOfBirth());
        existingPlayer.setTeamsCompositions(newPlayer.getTeamsCompositions());
        existingPlayer.setTournaments(newPlayer.getTournaments());
        existingPlayer.setUniversity(newPlayer.getUniversity());
        existingPlayer.setIdSite(newPlayer.getIdSite());
        playerRepository.save(existingPlayer);
    }

    @Override
    @Transactional
    public void deletePlayer(Long id) {
        Player player = entityRetrievalUtils.getPlayerOrThrow(id);
        for (TeamComposition teamComposition:List.copyOf(player.getTeamsCompositions()))
        {
            teamComposition.deletePlayer(player);
        }
        for (Tournament tournament: player.getTournaments())
        {
            tournament.deletePlayer(player);
        }
        for (Team team: player.getTeams())
        {
            team.deletePlayerFromTeam(player);
        }
        playerRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Player getPlayerByIdSite(Long idSite) {
        return entityRetrievalUtils.getPlayerByIdSiteOrThrow(idSite);
    }


    @Override
    @Transactional
    public boolean existsByIdSite(Long idSite) {
        return playerRepository.existsByIdSite(idSite);
    }

    @Override
    @Transactional
    public void deleteAllPlayers() {
        playerRepository.deleteAll();
    }

    @Override
    public boolean isManager(Long userId, Long playerId) {
        if (userId == null)
            return false;
        Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);
        for (Team team : player.getTeams()) {
            if (leagueService.isManager(userId, team.getLeague().getId()))
                return true;
        }
        return false;
    }

    @Override
    public boolean isManager(Long userId, PlayerDTO playerDTO) {
        if (userId == null)
            return false;
        Player player = playerFactory.mapToEntity(playerDTO);
        for (Team team : player.getTeams()) {
            if (leagueService.isManager(userId, team.getLeague().getId()))
                return true;
        }
        return false;
    }

    @Override
    public Page<Player> searchPlayers(String name, String surname, Long teamId, LocalDate bornBefore, LocalDate bornAfter, Sort sort, Pageable pageable) {
        Specification<Player> spec = PlayerSpecification.searchPlayers(name, surname, teamId, bornBefore, bornAfter, sort);
        return playerRepository.findAll(spec, pageable);
    }


}
