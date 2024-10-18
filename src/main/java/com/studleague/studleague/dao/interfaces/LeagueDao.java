package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.League;
import java.util.List;
import java.util.Optional;

public interface LeagueDao {
    Optional<League> getLeagueById(long id);

    List<League> getAllLeagues();

    void saveLeague(League league);

    void deleteLeague(long id);


}
