package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerDao playerDao;

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private LeagueDao leagueDao;


    @Override
    @Transactional
    public Player getPlayerById(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(playerDao.getPlayerById(id), "Player", id);
    }

    @Override
    @Transactional
    public List<Player> getAllPlayers() {
        return playerDao.getAllPlayers();
    }

    @Override
    @Transactional
    public void savePlayer(Player player) {
        playerDao.savePlayer(player);
        for (Team team : player.getTeams()) {
            team.addPlayerToTeam(player);
            teamDao.saveTeam(team);
        }
    }

    @Override
    @Transactional
    public void deletePlayer(long id) {
        playerDao.deletePlayer(id);
    }


    @Override
    @Transactional
    public Player getPlayerByIdSite(long idSite) {
        return EntityRetrievalUtils.getEntityOrThrow(playerDao.getPlayerByIdSite(idSite), "Player", idSite);
    }
}
