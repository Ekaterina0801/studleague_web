package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.League;
import java.util.List;
import java.util.Optional;

public interface LeagueDao {
    Optional<League> findById(Long id);

    List<League> findAll();

    void save(League league);

    void deleteById(Long id);

    void deleteAll();


}
