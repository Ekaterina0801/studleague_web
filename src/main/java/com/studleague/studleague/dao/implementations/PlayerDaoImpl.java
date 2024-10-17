package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.PlayerDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Player;
import com.studleague.studleague.entities.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


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
    public Optional<Player> getPlayerById(int id) {
        Player player = getCurrentSession().get(Player.class, id);
        return Optional.ofNullable(player);
    }

    @Override
    public List<Player> getAllPlayers() {
        Query<Player> query = getCurrentSession().createQuery("FROM Player", Player.class);
        return query.getResultList();
    }

    @Override
    public void savePlayer(Player player) {
        Session session = getCurrentSession();
        if (Objects.isNull(session.find(Player.class, player.getId()))) {
            session.persist(player);
        } else {
            session.merge(player);
        }
    }

    @Override
    public void deletePlayer(int id) {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM Player WHERE id = :id", Player.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }


    @Override
    public Optional<Player> getPlayerByIdSite(String idSite) {
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



}
