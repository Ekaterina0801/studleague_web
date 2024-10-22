package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.LeagueDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.League;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class LeagueDaoImpl implements LeagueDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<League> findById(long id) {
        League league = getCurrentSession().get(League.class, id);
        return Optional.ofNullable(league);
    }

    @Override
    public List<League> findAll() {
        Query<League> query = getCurrentSession().createQuery("FROM League", League.class);
        return query.getResultList();
    }

    @Override
    public void save(League league) {
        Session session = getCurrentSession();
        if (Objects.isNull(session.find(League.class, league.getId()))) {
            session.persist(league);
        } else {
            session.merge(league);
        }
    }


    @Override
    public void deleteById(long id) {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM League WHERE id = :id", League.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public void deleteAll() {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM League");
        query.executeUpdate();
    }

}
