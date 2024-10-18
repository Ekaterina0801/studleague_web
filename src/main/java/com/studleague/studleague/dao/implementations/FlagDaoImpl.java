package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.FlagDao;
import com.studleague.studleague.entities.Flag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class FlagDaoImpl implements FlagDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<Flag> getFlagById(long id) {
        Flag flag = getCurrentSession().get(Flag.class, id);
        return Optional.ofNullable(flag);
    }

    @Override
    public List<Flag> getAllFlags() {
        Query<Flag> query = getCurrentSession().createQuery("FROM Flag", Flag.class);
        return query.getResultList();
    }

    @Override
    public void saveFlag(Flag flag) {
        Session session = getCurrentSession();
        if (Objects.isNull(session.find(Flag.class, flag.getId()))) {
            session.persist(flag);
        } else {
            session.merge(flag);
        }
    }

    @Override
    public void deleteFlag(long id) {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM Flag WHERE id = :id", Flag.class);
        query.setParameter("id", id);
        query.executeUpdate();

    }
}
