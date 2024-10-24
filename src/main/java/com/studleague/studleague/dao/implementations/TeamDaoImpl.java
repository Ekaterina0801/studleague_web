package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamDaoImpl implements TeamDao {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    public Optional<Team> findById(long id) {
        Team team = getCurrentSession().get(Team.class, id);
        return Optional.ofNullable(team);
    }

    @Override
    public List<Team> findAll() {
        Query<Team> query = getCurrentSession().createQuery("from Team", Team.class);
        return query.getResultList();
    }


    //TODO: change save and update to persist and merge
    @Override
    public void save(Team team) {
        Session session = getCurrentSession();
        long idSite = team.getIdSite();

        if (!existsByIdSite(idSite)) {
            session.save(team);
        } else {
            Query<Team> query = session.createQuery("from Team t where t.idSite = :idSite", Team.class);
            query.setParameter("idSite", idSite);
            Team existingTeam = query.getSingleResult();
            updateExistingTeam(existingTeam, team);
            session.update(existingTeam);
        }
    }
    private void updateExistingTeam(Team existingTeam, Team newTeam) {
        if (newTeam.getTeamName() != null && !newTeam.getTeamName().isEmpty()) {
            existingTeam.setTeamName(newTeam.getTeamName());
        }
        if (newTeam.getPlayers() != null && !newTeam.getPlayers().isEmpty()) {
            existingTeam.setPlayers(new ArrayList<>(newTeam.getPlayers()));
        }
        if (newTeam.getLeague() != null) {
            existingTeam.setLeague(newTeam.getLeague());
        }
        if (newTeam.getFlags() != null && !newTeam.getFlags().isEmpty()) {
            existingTeam.setFlags(newTeam.getFlags());
        }
        if (newTeam.getUniversity() != null) {
            existingTeam.setUniversity(newTeam.getUniversity());
        }
        if (newTeam.getIdSite() != 0) {
            existingTeam.setIdSite(newTeam.getIdSite());
        }
        if (newTeam.getTournaments() != null && !newTeam.getTournaments().isEmpty()) {
            existingTeam.setTournaments(new ArrayList<>(newTeam.getTournaments()));
        }
    }

    @Override
    public void deleteById(long id) {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM Team WHERE id = :id", Controversial.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Team> findAllByLeagueId(long leagueId) {
        Query<Team> query = getCurrentSession().createQuery("from Team t where t.league.id = :league_id", Team.class);
        query.setParameter("league_id", leagueId);
        return query.getResultList();
    }



    @Override
    public Optional<Team> findByIdSite(long idSite){
        Query<Team> query = getCurrentSession().createQuery("from Team t where t.idSite =: idSite", Team.class);
        query.setParameter("idSite", idSite);
        try {
            Team team = query.getSingleResult();
            return Optional.of(team);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("Multiple players found for idSite: " + idSite);
        }
    }


    @Override
    public Optional<Team> findByPlayerIdAndLeagueId(long playerId, long leagueId) {
        String hql = "SELECT t FROM Player p " +
                "JOIN p.teams t " +
                "WHERE t.league.id = :leagueId AND p.id = :playerId";
        Query<Team> query = getCurrentSession().createQuery(hql, Team.class);
        query.setParameter("playerId", playerId);
        query.setParameter("leagueId", leagueId);
        try {
            Team team = query.getSingleResult();
            return Optional.of(team);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("Multiple teams found for playerId: " + playerId + " and leagueId: " + leagueId);
        }
    }

    @Transactional
    @Override
    public boolean existsByIdSite(long idSite) {
        Query<Long> query = getCurrentSession().createQuery("SELECT count(p.id) FROM Team p WHERE p.idSite = :idSite", Long.class);
        query.setParameter("idSite", idSite);
        Long count = query.getSingleResult();
        return count != null && count > 0;
    }

    @Override
    public void deleteAll() {
        Query<?> query = getCurrentSession().createQuery("DELETE FROM Team");
        query.executeUpdate();
    }

}
