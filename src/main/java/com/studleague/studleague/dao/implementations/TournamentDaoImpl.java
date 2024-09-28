package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Tournament;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class TournamentDaoImpl implements TournamentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Tournament getTournamentById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Tournament tournament = session.get(Tournament.class, id);
        return tournament;
    }

    @Override
    public List<Tournament> getAllTournaments() {
        Session session = entityManager.unwrap(Session.class);
        Query<Tournament> query = session.createQuery("from Tournament", Tournament.class);
        List<Tournament> allTransfers= query.getResultList();
        return allTransfers;
    }

    @Override
    public void saveTournament(Tournament tournament) {
        Session session = entityManager.unwrap(Session.class);
        session.merge(tournament);
    }

    @Override
    public void updateTournament(Tournament tournament, String[] params) {

    }

    @Override
    public void deleteTournament(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Tournament where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }


}
