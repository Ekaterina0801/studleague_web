package com.studleague.studleague.dao.implementations;

import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.entities.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class TeamDaoImpl implements TeamDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Team getTeamById(int id) {
        Session session = entityManager.unwrap(Session.class);
        Team team= session.get(Team.class, id);
        return team;
    }

    @Override
    public List<Team> getAllTeams() {
        Session session = entityManager.unwrap(Session.class);
        Query<Team> query = session.createQuery("from Team", Team.class);
        List<Team> allTeams= query.getResultList();
        return allTeams;

    }

    @Override
    public void saveTeam(Team team) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(team);
    }

    @Override
    public void updateTeam(Team team, String[] params) {

    }

    @Override
    public void deleteTeam(int id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("delete from Team where id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Override
    public List<Team> teamsByLeague(int league_id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from Team t where t.league.id = :league_id");
        query.setParameter("league_id", league_id);
        List<Team> teams = query.getResultList();
        return teams;
    }

    @Override
    public List<Team> tournamentsByTeam(int team_id) {
        Session session = entityManager.unwrap(Session.class);
        Query query = session.createQuery("from FullResult ft where ft.team.id = :team_id");
        query.setParameter("team_id", team_id);
        List<Team> teams = query.getResultList();
        return teams;
    }




}
