package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.factory.PlayerFactory;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (idSite!=0)
        {
            if (playerRepository.existsByIdSite(idSite))
            {
                playerRepository.save(entityRetrievalUtils.getPlayerByIdSiteOrThrow(idSite));
            }
            else {
                playerRepository.save(player);
            }
        }
        else {
            playerRepository.save(player);
        }
    }

    @Override
    @Transactional
    public void deletePlayer(Long id) {
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
    public void deleteAllPlayers()
    {
        playerRepository.deleteAll();
    }

    @Override
    public boolean isManager(Long userId, Long playerId) {
        if (userId==null)
            return false;
        Player player = entityRetrievalUtils.getPlayerOrThrow(playerId);
        for (Team team:player.getTeams())
        {
            if (leagueService.isManager(userId, team.getLeague().getId()))
                return true;
        }
        return false;
    }

    @Override
    public boolean isManager(Long userId, PlayerDTO playerDTO) {
        if (userId==null)
            return false;
        Player player = playerFactory.toEntity(playerDTO);
        for (Team team:player.getTeams())
        {
            if (leagueService.isManager(userId, team.getLeague().getId()))
                return true;
        }
        return false;
    }


}
