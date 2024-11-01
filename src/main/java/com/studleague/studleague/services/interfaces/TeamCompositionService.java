package com.studleague.studleague.services.interfaces;


import com.studleague.studleague.dto.TeamCompositionDTO;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.entities.Player;
import java.util.List;
import java.util.Optional;

public interface TeamCompositionService {
    TeamComposition findById(Long id);

    void save(TeamComposition teamComposition);

    List<TeamComposition> findByTournamentId(Long tournamentId);

    List<TeamComposition> findByParentTeamId(Long parentTeamId);

    List<TeamComposition> findAll();

    void deleteAll();

    void deleteById(Long id);

    boolean isManager(Long userId, Long teamCompositionId);

    boolean isManager(Long userId, TeamCompositionDTO teamCompositionDTO);

}
