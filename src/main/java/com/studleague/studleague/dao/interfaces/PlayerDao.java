package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Player;
import java.util.List;
import java.util.Optional;

public interface PlayerDao {
    Optional<Player> getPlayerById(long id);

    List<Player> getAllPlayers();

    void savePlayer(Player player);

    void deletePlayer(long id);

    Optional<Player> getPlayerByIdSite(long idSite);
}
