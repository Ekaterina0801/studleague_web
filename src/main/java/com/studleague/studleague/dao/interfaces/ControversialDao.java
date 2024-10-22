package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Controversial;
import java.util.List;
import java.util.Optional;

public interface ControversialDao {

        Optional<Controversial> findById(long id);

        List<Controversial> findAll();

        void save(Controversial controversial);

        void deleteById(long id);

        List<Controversial> findAllByTeamId(long teamId);

        List<Controversial> findAllByTournamentId(long tournamentId);

        void deleteAll();

}
