package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerDao {
    Optional<Player> getPlayerById(int id);

    List<Player> getAllPlayers();

    void savePlayer(Player player);

    void deletePlayer(int id);

    Optional<Player> getPlayerByIdSite(String idSite);
}
