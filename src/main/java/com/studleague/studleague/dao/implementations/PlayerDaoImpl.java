package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class PlayerDaoImpl implements PlayerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Player getPlayerById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Player player = session.get(Player.class, id);
        return player;
    }

    @Override
    public List<Player> getAllPlayers() {
        Session session = entityManager.unwrap(Session.class);
        Query<Player> query = session.createQuery("from Player", Player.class);
        List<Player> allPlayers= query.getResultList();
        return allPlayers;
    }

    @Override
    public void savePlayer(Player player) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(player);
    }

    @Override
    public void updatePlayer(Player player, String[] params) {

    }

    @Override
    public void deletePlayer(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Player where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public Team getTeamPlayerByLeague(int playerId, int leagueId){
        Session session = entityManager.unwrap(Session.class);
        String hql = "SELECT t FROM Player p " +
                      "JOIN p.teams t " +
                     "WHERE t.league.id = :leagueId AND p.id=:playerId";
        Query query = session.createQuery(hql);
        query.setParameter("playerId", playerId);
        query.setParameter("leagueId", leagueId);
        List<Team> teams = query.getResultList();
        Team team = new Team();
        if (!teams.isEmpty())
        {
            team = teams.get(0);
        }
        return team;
    }

}
