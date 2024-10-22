package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Transfer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class TransferDaoImpl implements TransferDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<Transfer> findById(long id) {
        Transfer transfer = getCurrentSession().get(Transfer.class,id);
        return Optional.ofNullable(transfer);
    }

    @Override
    public List<Transfer> findAll() {
        Query<Transfer> query = getCurrentSession().createQuery("from Transfer", Transfer.class);
        return query.getResultList();
    }

    @Override
    public void save(Transfer transfer) {
        Session session = getCurrentSession();
        if (Objects.isNull(session.find(Controversial.class, transfer.getId()))) {
            session.persist(transfer);
        } else {
            session.merge(transfer);
        }
    }

    @Override
    public void deleteById(long id) {
        Query<?> query = getCurrentSession().createQuery("delete from Transfer where id=:id", Transfer.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Transfer> findAllByPlayerId(long playerId) {
        Query<Transfer> query = getCurrentSession().createQuery("from Transfer where player.id=:player_id", Transfer.class);
        query.setParameter("player_id",playerId);
        return query.getResultList();

    }

    @Override
    public List<Transfer> findAllByTeamId(long teamId) {
        Query<Transfer> query = getCurrentSession().createQuery("select t from Transfer t left join fetch t.oldTeam o left join fetch t.newTeam n where o.id=:team_id or n.id=:team_id", Transfer.class);
        query.setParameter("team_id",teamId);
        return query.getResultList();
    }

    @Override
    public void deleteAll() {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM Transfer");
        query.executeUpdate();
    }
}
