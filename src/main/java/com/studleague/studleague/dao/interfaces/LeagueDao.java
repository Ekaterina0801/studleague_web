package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import java.util.List;

public interface LeagueDao {
    League getLeagueById(int id);

    List<League> getAllLeagues();

    void saveLeague(League league);

    void updateLeague(League league, String[] params);

    void deleteLeague(int id);

    List<Team> getAllTeamsByLeague();

}
