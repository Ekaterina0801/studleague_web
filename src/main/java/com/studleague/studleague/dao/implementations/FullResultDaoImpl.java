package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.FullResultDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.FullResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class FullResultDaoImpl implements FullResultDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public FullResult getFullResultById(int id) {
        Session session = entityManager.unwrap(Session.class);
        FullResult fullResult = session.get(FullResult.class, id);
        return fullResult;
    }

    @Override
    public List<FullResult> getAllFullResults() {
        Session session = entityManager.unwrap(Session.class);
        Query<FullResult> query = session.createQuery("from FullResult", FullResult.class);
        List<FullResult> allResults= query.getResultList();
        return allResults;
    }

    @Override
    public void saveFullResult(FullResult fullResult) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(fullResult);
    }

    @Override
    public void updateFullResult(FullResult fullResult, String[] params) {

    }

    @Override
    public void deleteFullResult(int id) {
        FullResult fullResult = entityManager.find(FullResult.class, id);
        if (fullResult != null) {
            fullResult.getControversials().clear();
            entityManager.remove(fullResult);
        }
    }

    @Override
    public List<FullResult> getResultsForTeam(int team_id)
    {
        Session session = entityManager.unwrap(Session.class);
        Query<FullResult> query = session.createQuery("from FullResult where team.id=:team_id", FullResult.class);
        query.setParameter("team_id",team_id);
        List<FullResult> allResults= query.getResultList();
        return allResults;
    }


}
