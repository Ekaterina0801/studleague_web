package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PlayerDaoImpl implements PlayerDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<Player> findById(Long id) {
        Player player = getCurrentSession().get(Player.class, id);
        return Optional.ofNullable(player);
    }

    @Override
    public List<Player> findAll() {
        Query<Player> query = getCurrentSession().createQuery("FROM Player", Player.class);
        return query.getResultList();
    }

    @Override
    public void save(Player player) {
        Session session = getCurrentSession();
        if (Objects.isNull(session.find(Player.class, player.getId()))) {
            session.persist(player);
        } else {
            session.merge(player);
        }
    }

    @Override
    public void deleteById(Long id) {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM Player WHERE id = :id", Player.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }


    @Override
    public Optional<Player> findByIdSite(Long idSite) {
        Query<Player> query = getCurrentSession().createQuery("from Player p where p.idSite = :idSite", Player.class);
        query.setParameter("idSite", idSite);

        try {
            Player player = query.getSingleResult();
            return Optional.of(player);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("Multiple players found for idSite: " + idSite);
        }
    }

    @Transactional
    @Override
    public boolean existsByIdSite(Long idSite) {
        Query<Long> query = getCurrentSession().createQuery("SELECT count(p.id) FROM Player p WHERE p.idSite = :idSite", Long.class);
        query.setParameter("idSite", idSite);
        Long count = query.getSingleResult();
        return count != null && count > 0;
    }

    @Override
    public void deleteAll() {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM Player");
        query.executeUpdate();
    }


}
