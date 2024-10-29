package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.ResultDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

import java.util.List;

@Repository
public class ResultDaoImpl implements ResultDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {

        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<FullResult> findById(Long id) {
        FullResult fullResult = getCurrentSession().get(FullResult.class, id);
        return Optional.ofNullable(fullResult);
    }

    @Override
    public List<FullResult> findAll() {
        Query<FullResult> query = getCurrentSession().createQuery("FROM FullResult", FullResult.class);
        return query.getResultList();
    }

    @Override
    public void save(FullResult fullResult) {
        Session session = getCurrentSession();
        if (Objects.isNull(session.find(FullResult.class, fullResult.getId()))) {
            session.persist(fullResult);
        } else {
            session.merge(fullResult);
        }
    }


    @Override
    public void deleteById(Long id) {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM FullResult WHERE id = :id", Controversial.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<FullResult> findAllByTeamId(Long teamId) {
        Session session = getCurrentSession();
        Query<FullResult> query = session.createQuery("from FullResult where team.id=:team_id", FullResult.class);
        query.setParameter("team_id", teamId);
        return query.getResultList();
    }

    @Override
    public void deleteAll() {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM FullResult");
        query.executeUpdate();
    }

}
