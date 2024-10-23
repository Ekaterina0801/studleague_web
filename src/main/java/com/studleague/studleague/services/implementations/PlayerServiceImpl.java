package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
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
    //private PlayerDao playerRepository;
    private PlayerRepository playerRepository;

    @Autowired
    //private TeamDao teamRepository;
    private TeamRepository teamRepository;

    @Autowired
    //private LeagueDao leagueRepository;
    private LeagueRepository leagueRepository;


    @Override
    @Transactional
    public Player getPlayerById(long id) {
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
        playerRepository.save(player);
        if (player.getTeams() != null) {
            for (Team team : player.getTeams()) {
                if (!teamRepository.existsByIdSite(team.getIdSite())) {
                    team.addPlayerToTeam(player);
                    teamRepository.save(team);
                }
            }
        }
    }

    @Override
    @Transactional
    public void deletePlayer(long id) {
        playerRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Player getPlayerByIdSite(long idSite) {
        return EntityRetrievalUtils.getEntityOrThrow(playerRepository.findByIdSite(idSite), "Player", idSite);
    }


    @Override
    @Transactional
    public boolean existsByIdSite(long idSite) {
        return playerRepository.existsByIdSite(idSite);
    }

    @Override
    @Transactional
    public void deleteAllPlayers()
    {
        playerRepository.deleteAll();
    }


}
