package com.studleague.studleague.services.implementations;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.repository.LeagueRepository;
import com.studleague.studleague.repository.PlayerRepository;
import com.studleague.studleague.repository.TeamRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private LeagueRepository leagueRepository;


    @Override
    @Transactional
    public Player getPlayerById(Long id) {
        return EntityRetrievalUtils.getEntityOrThrow(playerRepository.findById(id), "Player", id);
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
                playerRepository.save(EntityRetrievalUtils.getEntityOrThrow(playerRepository.findByIdSite(idSite), "Player (by idSite)", idSite));
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
        return EntityRetrievalUtils.getEntityOrThrow(playerRepository.findByIdSite(idSite), "Player", idSite);
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



}
