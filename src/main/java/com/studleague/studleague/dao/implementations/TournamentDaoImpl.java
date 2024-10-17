package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.dao.interfaces.TransferDao;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.Tournament;
import com.studleague.studleague.services.interfaces.TournamentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TournamentDaoImpl implements TournamentDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }


    @Override
    public Optional<Tournament> getTournamentById(int id) {
        Tournament tournament = getCurrentSession().get(Tournament.class, id);
        return Optional.ofNullable(tournament);
    }

    @Override
    public List<Tournament> getAllTournaments() {
        Query<Tournament> query = getCurrentSession().createQuery("from Tournament", Tournament.class);
        return query.getResultList();
    }

    @Override
    public void saveTournament(Tournament tournament) {

        Session session = entityManager.unwrap(Session.class);
        session.merge(tournament);
        System.out.println(entityManager.contains(tournament));
        String idSite = tournament.getIdSite();
        Query<Tournament> query = session.createQuery("from Tournament t where t.idSite = :idSite", Tournament.class);
        query.setParameter("idSite", idSite);
        System.out.println(tournament);
        List<Tournament> tournaments = query.getResultList();

        if (tournaments.isEmpty()) {
            session.merge(tournament);
        } else {
            Tournament existingTournament = tournaments.get(0);
            existingTournament.setName(tournament.getName());
            existingTournament.setDateOfStart(tournament.getDateOfStart());
            existingTournament.setDateOfEnd(tournament.getDateOfEnd());
            existingTournament.setLeagues(tournament.getLeagues());
            existingTournament.setPlayers(tournament.getPlayers());
            existingTournament.setResults(tournament.getResults());
            existingTournament.setTeams(tournament.getTeams());
            session.update(existingTournament);
        }
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

    @Override
    public Optional<Tournament> getTournamentBySiteId(String idSite)
    {
        Query<Tournament> query = getCurrentSession().createQuery("from Tournament where idSite=:idSite", Tournament.class);
        query.setParameter("idSite", idSite);
        try {
            Tournament tournament = query.getSingleResult();
            return Optional.of(tournament);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("Multiple tournaments found for idSite: " + idSite);
        }
    }

    @Override
    public List<Tournament> tournamentsByTeam(int team_id) {
        Session session = entityManager.unwrap(Session.class);
        Query<Tournament> query = getCurrentSession().createQuery("SELECT t FROM Tournament t JOIN t.teams te WHERE te.id = :teamId", Tournament.class);
        query.setParameter("teamId", team_id);
        return query.getResultList();
    }


}
