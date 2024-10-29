package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Controversial;
import java.util.List;
import java.util.Optional;

public interface ControversialDao {

        Optional<Controversial> findById(Long id);

        List<Controversial> findAll();

        void save(Controversial controversial);

        void deleteById(Long id);

        List<Controversial> findAllByTeamId(Long teamId);

        List<Controversial> findAllByTournamentId(Long tournamentId);

        void deleteAll();

}
