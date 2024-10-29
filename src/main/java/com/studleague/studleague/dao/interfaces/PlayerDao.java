package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Player;
import java.util.List;
import java.util.Optional;

public interface PlayerDao {
    Optional<Player> findById(Long id);

    List<Player> findAll();

    void save(Player player);

    void deleteById(Long id);

    Optional<Player> findByIdSite(Long idSite);

    boolean existsByIdSite(Long idSite);

    void deleteAll();
}
