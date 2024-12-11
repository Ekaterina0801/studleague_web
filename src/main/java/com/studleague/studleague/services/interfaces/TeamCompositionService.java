package com.studleague.studleague.services.interfaces;


import com.studleague.studleague.dto.TeamCompositionDTO;
import com.studleague.studleague.entities.TeamComposition;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

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

    List<TeamComposition> searchTeamCompositions(Long teamId, Long tournamentId, Sort sort);

    boolean existsByTeamAndTournament(Long tournamentId, Long teamId);

    List<TeamCompositionDTO> processTeamCompositions(List<Map<String, Object>> data, Long leagueId, Long tournamentId);

}
