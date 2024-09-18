package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Player;

import java.util.List;

public interface PlayerService {
    Player getPlayerById(int id);

    List<Player> getAllPlayers();

    void savePlayer(Player player);

    void updatePlayer(Player player, String[] params);

    void deletePlayer(int id);
}
