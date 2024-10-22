package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentDao {
    Optional<Tournament> findById(long id);

    List<Tournament> findAll();

    void save(Tournament tournament);

    void deleteByTournamentId(long id);

    Optional<Tournament> findByIdSite(long idSite);

    List<Tournament> findAllByTeamId(long teamId);

    public boolean existsByIdSite(long idSite);

    void deleteAll();

}
