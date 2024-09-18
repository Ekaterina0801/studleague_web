package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class LeagueDaoImpl implements LeagueDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public League getLeagueById(int id) {
        Session session = entityManager.unwrap(Session.class);
        League league = session.get(League.class, id);
        return league;
    }

    @Override
    public List<League> getAllLeagues() {
        Session session = entityManager.unwrap(Session.class);
        Query<League> query = session.createQuery("from League", League.class);
        List<League> allLeagues= query.getResultList();
        return allLeagues;
    }

    @Override
    public void saveLeague(League league) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(league);
    }

    @Override
    public void updateLeague(League league, String[] params) {

    }

    @Override
    public void deleteLeague(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from League where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Team> getAllTeamsByLeague() {
        Session session = entityManager.unwrap(Session.class);

        return List.of();
    }

}
