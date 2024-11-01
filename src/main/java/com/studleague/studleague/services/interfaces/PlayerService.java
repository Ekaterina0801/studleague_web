package com.studleague.studleague.services.interfaces;

import com.studleague.studleague.dto.PlayerDTO;
import com.studleague.studleague.entities.Player;

import java.util.List;

public interface PlayerService {
    Player getPlayerById(Long id);

    List<Player> getAllPlayers();

    void savePlayer(Player player);

    void deletePlayer(Long id);

    Player getPlayerByIdSite(Long idSite);

    boolean existsByIdSite(Long idSite);

    void deleteAllPlayers();

    boolean isManager(Long userId, Long playerId);

    boolean isManager(Long userId, PlayerDTO playerDTO);
}
