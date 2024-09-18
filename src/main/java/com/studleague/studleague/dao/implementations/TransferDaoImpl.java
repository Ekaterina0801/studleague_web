package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.FlagDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.Transfer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class TransferDaoImpl implements TransferDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Transfer getTransferById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Transfer transfer = session.get(Transfer.class,id);
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers() {
        Session session = entityManager.unwrap(Session.class);
        Query<Transfer> query = session.createQuery("from Transfer", Transfer.class);
        List<Transfer> allTransfers= query.getResultList();
        return allTransfers;
    }

    @Override
    public void saveTransfer(Transfer transfer) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(transfer);
        /*if (transfer.getId()==0){
            session.persist(transfer); //session.save();
        }
        else{
            session.merge(transfer);
        }*/
    }

    @Override
    public void updateTransfer(Transfer transfer, String[] params) {

    }

    @Override
    public void deleteTransfer(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Transfer where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Transfer> getTransfersForPlayer(int player_id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Transfer where player.id=:player_id");
        query.setParameter("player_id",player_id);
        List<Transfer> transfers = query.getResultList();
        return transfers;

    }

    @Override
    public List<Transfer> getTransfersForTeam(int team_id) {
        Session session = entityManager.unwrap(Session.class);
        //Query query = session.createQuery("from Transfer where oldTeam.id=:team_id or newTeam.id=:team_id");
        Query query = session.createQuery("select t from Transfer t left join fetch t.oldTeam o left join fetch t.newTeam n where o.id=:team_id or n.id=:team_id");
        query.setParameter("team_id",team_id);
        List<Transfer> transfers = query.getResultList();
        return transfers;
    }
}
