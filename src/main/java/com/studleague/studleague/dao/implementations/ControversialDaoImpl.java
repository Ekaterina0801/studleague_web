package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.ControversialDao;
import com.studleague.studleague.entities.Controversial;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class ControversialDaoImpl implements ControversialDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<Controversial> getControversialById(long id) {
        Controversial controversial = getCurrentSession().get(Controversial.class, id);
        return Optional.ofNullable(controversial);
    }

    @Override
    public List<Controversial> getAllControversials() {
        Query<Controversial> query = getCurrentSession().createQuery("FROM Controversial", Controversial.class);
        return query.getResultList();
    }

    @Override
    public void saveControversial(Controversial controversial) {
        Session session = getCurrentSession();
        if (Objects.isNull(session.find(Controversial.class, controversial.getId()))) {
            session.persist(controversial);
        } else {
            session.merge(controversial);
        }
    }

    @Override
    public void deleteControversial(long id) {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM Controversial WHERE id = :id", Controversial.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Controversial> getControversialByTeamId(long teamId) {
        Query<Controversial> query = getCurrentSession()
                .createQuery("FROM Controversial WHERE team_id = :teamId", Controversial.class);
        query.setParameter("teamId", teamId);
        return query.getResultList();
    }

    @Override
    public List<Controversial> getControversialByTournamentId(long tournamentId) {
        Query<Controversial> query = getCurrentSession()
                .createQuery("FROM Controversial c WHERE c.fullResult.tournament.id = :tournamentId", Controversial.class);
        query.setParameter("tournamentId", tournamentId);
        return query.getResultList();
    }


}
