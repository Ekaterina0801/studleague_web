package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;

import java.util.List;

public interface PlayerDao {
    Player getPlayerById(int id);

    List<Player> getAllPlayers();

    void savePlayer(Player player);

    void updatePlayer(Player player, String[] params);

    void deletePlayer(int id);

    public Team getTeamPlayerByLeague(int playerId, int leagueId);
}
