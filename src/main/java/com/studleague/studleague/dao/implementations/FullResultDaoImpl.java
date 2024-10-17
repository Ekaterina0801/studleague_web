package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.FullResultDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

import java.util.HashMap;
import java.util.List;

@Repository
public class FullResultDaoImpl implements FullResultDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {

        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<FullResult> getFullResultById(int id) {
        FullResult fullResult = getCurrentSession().get(FullResult.class, id);
        return Optional.ofNullable(fullResult);
    }

    @Override
    public List<FullResult> getAllFullResults() {
        Query<FullResult> query = getCurrentSession().createQuery("FROM FullResult", FullResult.class);
        return query.getResultList();
    }

    @Override
    public void saveFullResult(FullResult fullResult) {
        Session session = getCurrentSession();
        if (Objects.isNull(session.find(FullResult.class, fullResult.getId()))) {
            session.persist(fullResult);
        } else {
            session.merge(fullResult);
        }
    }


    @Override
    public void deleteFullResult(int id) {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM FullResult WHERE id = :id", Controversial.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<FullResult> getResultsForTeam(int team_id) {
        Session session = getCurrentSession();
        Query<FullResult> query = session.createQuery("from FullResult where team.id=:team_id", FullResult.class);
        query.setParameter("team_id", team_id);
        return query.getResultList();
    }


}
