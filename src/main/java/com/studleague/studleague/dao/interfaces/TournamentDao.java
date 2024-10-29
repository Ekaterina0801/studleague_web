package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentDao {
    Optional<Tournament> findById(Long id);

    List<Tournament> findAll();

    void save(Tournament tournament);

    void deleteByTournamentId(Long id);

    Optional<Tournament> findByIdSite(Long idSite);

    List<Tournament> findAllByTeamId(Long teamId);

    boolean existsByIdSite(Long idSite);

    void deleteAll();

}
