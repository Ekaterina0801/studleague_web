package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.entities.Player;

import java.util.List;

public interface PlayerService {
    Player getPlayerById(long id);

    List<Player> getAllPlayers();

    void savePlayer(Player player);

    void deletePlayer(long id);

    Player getPlayerByIdSite(long idSite);

    boolean existsByIdSite(long idSite);
}
