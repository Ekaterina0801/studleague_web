package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.ControversialDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Controversial;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ControversialDaoImpl implements ControversialDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Controversial getControversialById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Controversial controversial = session.get(Controversial.class, id);
        return controversial;
    }

    @Override
    public List<Controversial> getAllControversials() {
        Session session = entityManager.unwrap(Session.class);
        Query<Controversial> query = session.createQuery("from Controversial", Controversial.class);
        List<Controversial> allControversials= query.getResultList();
        return allControversials;
    }

    @Override
    public void saveControversial(Controversial controversial) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(controversial);
    }

    @Override
    public void updateControversial(Controversial controversial, String[] params) {

    }

    @Override
    public void deleteControversial(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Controversial where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public HashMap<Integer, Controversial> getControversialByTeamId(int team_id){
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Controversial where team_id=:team_id");
        query.setParameter("team_id", team_id);
        List<Controversial> controversials = query.getResultList();
        HashMap<Integer, Controversial> controversialsByNumber = new HashMap<>();
        for (Controversial controversial: controversials){
            controversialsByNumber.put(controversial.getQuestionNumber(), controversial);
        }
        return controversialsByNumber;
    }

    @Override
    public HashMap<Integer, Controversial> getControversialByTournamentId(int tournament_id){
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("SELECT c FROM Controversial c WHERE c.fullResult.tournament.id=:tournament_id");
        query.setParameter("tournament_id", tournament_id);
        List<Controversial> controversials = query.getResultList();
        HashMap<Integer, Controversial> controversialsByNumber = new HashMap<>();
        for (Controversial controversial: controversials){
            controversialsByNumber.put(controversial.getQuestionNumber(), controversial);
        }
        return controversialsByNumber;
    }
}
