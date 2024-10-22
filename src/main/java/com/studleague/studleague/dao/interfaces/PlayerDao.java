package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Player;
import java.util.List;
import java.util.Optional;

public interface PlayerDao {
    Optional<Player> findById(long id);

    List<Player> findAll();

    void save(Player player);

    void deleteById(long id);

    Optional<Player> findByIdSite(long idSite);

    boolean existsByIdSite(long idSite);

    void deleteAll();
}
