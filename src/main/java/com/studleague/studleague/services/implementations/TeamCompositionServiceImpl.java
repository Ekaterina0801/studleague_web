package com.studleague.studleague.services.implementations;
import com.studleague.studleague.dto.TeamCompositionDTO;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.factory.TeamCompositionFactory;
import com.studleague.studleague.repository.TeamCompositionRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import com.studleague.studleague.specifications.TeamCompositionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("teamCompositionService")
public class TeamCompositionServiceImpl implements TeamCompositionService {

    @Autowired
    private TeamCompositionRepository teamCompositionRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private TeamCompositionFactory teamCompositionFactory;

    @Override
    @Transactional(readOnly = true)
    public TeamComposition findById(Long id) {
        return entityRetrievalUtils.getTeamCompositionOrThrow(id);
    }

    @Transactional
    @Override
    public void save(TeamComposition teamComposition) {
        Long tournamentId = teamComposition.getTournament().getId();
        Long parentTeamId = teamComposition.getParentTeam().getId();
        Long id = teamComposition.getId();
        Optional<TeamComposition> existingOpt =
                id != null ? teamCompositionRepository.findById(id)
                        : teamCompositionRepository.findByTournamentIdAndParentTeamId(tournamentId, parentTeamId);

        if (existingOpt.isPresent()) {
            TeamComposition existingTeamComposition = existingOpt.get();
            update(existingTeamComposition, teamComposition);
            teamCompositionRepository.saveAndFlush(existingTeamComposition);
        } else {
            teamCompositionRepository.save(teamComposition);
        }
    }





    private void update(TeamComposition existingTeamComposition, TeamComposition teamComposition)
    {
        existingTeamComposition.setParentTeam(teamComposition.getParentTeam());
        existingTeamComposition.setTournament(teamComposition.getTournament());
        existingTeamComposition.setPlayers(teamComposition.getPlayers());
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeamComposition> findByTournamentId(Long tournamentId) {
        return teamCompositionRepository.findAllByTournamentId(tournamentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeamComposition> findByParentTeamId(Long parentTeamId) {
        return teamCompositionRepository.findAllByParentTeamId(parentTeamId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TeamComposition> findAll() {
        return teamCompositionRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteAll() {
        teamCompositionRepository.deleteAll();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        teamCompositionRepository.deleteById(id);
    }

    @Override
    public boolean isManager(Long userId, Long teamCompositionId) {
        if (userId==null)
            return false;
        TeamComposition teamComposition = entityRetrievalUtils.getTeamCompositionOrThrow(teamCompositionId);
        Long leagueId = teamComposition.getParentTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public boolean isManager(Long userId, TeamCompositionDTO teamCompositionDTO) {
        if (userId==null)
            return false;
        TeamComposition teamComposition = teamCompositionFactory.mapToEntity(teamCompositionDTO);
        Long leagueId = teamComposition.getParentTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    public List<TeamComposition> searchTeamCompositions(Long teamId, Long tournamentId, Sort sort) {
        Specification<TeamComposition> spec = TeamCompositionSpecification.searchTeamCompositions(teamId, tournamentId, sort);
        return teamCompositionRepository.findAll(spec);
    }

}
