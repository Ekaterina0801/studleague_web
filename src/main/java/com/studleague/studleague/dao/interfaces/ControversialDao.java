package com.studleague.studleague.dao.interfaces;

import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.Flag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ControversialDao {

        Optional<Controversial> getControversialById(int id);

        List<Controversial> getAllControversials();

        void saveControversial(Controversial controversial);

        void deleteControversial(int id);

        List<Controversial> getControversialByTeamId(int team_id);

        List<Controversial> getControversialByTournamentId(int tournament_id);

}
