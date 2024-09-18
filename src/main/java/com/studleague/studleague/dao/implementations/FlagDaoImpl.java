package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.FlagDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.Flag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class FlagDaoImpl implements FlagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Flag getFlagById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Flag flag = session.get(Flag.class, id);
        return flag;
    }

    @Override
    public List<Flag> getAllFlags() {
        Session session = entityManager.unwrap(Session.class);
        Query<Flag> query = session.createQuery("from Flag", Flag.class);
        List<Flag> allFlags= query.getResultList();
        return allFlags;
    }

    @Override
    public void saveFlag(Flag flag) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(flag);
    }

    @Override
    public void updateFlag(Flag flag, String[] params) {

    }

    @Override
    public void deleteFlag(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Flag where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
