package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.entities.Tournament;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
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
    public Optional<Tournament> getTournamentById(long id) {
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
        Session session = getCurrentSession();
        Long idSite = tournament.getIdSite();

        Tournament existingTournament = session.createQuery("from Tournament t where t.idSite = :idSite", Tournament.class)
                .setParameter("idSite", idSite)
                .uniqueResult();

        if (existingTournament == null) {
            session.persist(tournament);
        } else {
            updateExistingTournament(existingTournament, tournament);
            session.merge(existingTournament);
        }
    }

    private void updateExistingTournament(Tournament existingTournament, Tournament newTournament) {
        if (newTournament.getName() != null && !newTournament.getName().isEmpty()) {
            existingTournament.setName(newTournament.getName());
        }
        if (newTournament.getDateOfStart() != null) {
            existingTournament.setDateOfStart(newTournament.getDateOfStart());
        }
        if (newTournament.getDateOfEnd() != null) {
            existingTournament.setDateOfEnd(newTournament.getDateOfEnd());
        }
        if (newTournament.getLeagues() != null) {
            existingTournament.setLeagues(new ArrayList<>(newTournament.getLeagues()));
        }
        if (newTournament.getPlayers() != null) {
            existingTournament.setPlayers(new ArrayList<>(newTournament.getPlayers()));
        }
        if (newTournament.getResults() != null) {
            existingTournament.setResults(new ArrayList<>(newTournament.getResults()));
        }
        if (newTournament.getTeams() != null) {
            existingTournament.setTeams(new ArrayList<>(newTournament.getTeams()));
        }
        if (newTournament.getIdSite() != 0) {
            existingTournament.setIdSite(newTournament.getIdSite());
        }
    }



    @Override
    public void deleteTournament(long id) {
        Session session = entityManager.unwrap(Session.class);
        Query<?> query = session.createQuery("delete from Tournament where id=:id", Tournament.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public Optional<Tournament> getTournamentBySiteId(long idSite)
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
    public List<Tournament> tournamentsByTeam(long teamId) {
        Query<Tournament> query = getCurrentSession().createQuery("SELECT t FROM Tournament t JOIN t.teams te WHERE te.id = :teamId", Tournament.class);
        query.setParameter("teamId", teamId);
        return query.getResultList();
    }


}
