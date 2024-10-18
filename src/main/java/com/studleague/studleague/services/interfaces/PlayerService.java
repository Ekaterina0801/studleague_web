package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;

import java.util.List;

public interface PlayerService {
    Player getPlayerById(int id);

    List<Player> getAllPlayers();

    void savePlayer(Player player);

    void deletePlayer(int id);

    Player getPlayerByIdSite(String idSite);
}
