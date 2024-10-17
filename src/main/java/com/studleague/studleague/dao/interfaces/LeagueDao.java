package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import java.util.List;
import java.util.Optional;

public interface LeagueDao {
    Optional<League> getLeagueById(int id);

    List<League> getAllLeagues();

    void saveLeague(League league);

    void deleteLeague(int id);


}
