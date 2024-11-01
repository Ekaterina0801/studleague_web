package com.studleague.studleague.services.implementations;
import com.studleague.studleague.dto.TeamCompositionDTO;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.factory.TeamCompositionFactory;
import com.studleague.studleague.repository.TeamCompositionRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Transactional
    public TeamComposition findById(Long id) {
        return entityRetrievalUtils.getTeamCompositionOrThrow(id);
    }

    @Override
    public void save(TeamComposition teamComposition) {
        Long tournamentId = teamComposition.getTournament().getId();
        Long parentTeamId = teamComposition.getParentTeam().getId();
        if (teamCompositionRepository.findByTournamentIdAndParentTeamId(tournamentId,parentTeamId).isPresent())
        {
            TeamComposition teamCompositionExisting = entityRetrievalUtils.getTeamCompositionByTournamentIdAndParentTeamIdOrThrow(tournamentId, parentTeamId);
            teamCompositionRepository.save(teamCompositionExisting);
        }
        else {
            teamCompositionRepository.save(teamComposition);
        }
    }

    @Transactional
    @Override
    public List<TeamComposition> findByTournamentId(Long tournamentId) {
        return teamCompositionRepository.findAllByTournamentId(tournamentId);
    }

    @Transactional
    @Override
    public List<TeamComposition> findByParentTeamId(Long parentTeamId) {
        return teamCompositionRepository.findAllByParentTeamId(parentTeamId);
    }

    @Transactional
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
        TeamComposition teamComposition = teamCompositionFactory.toEntity(teamCompositionDTO);
        Long leagueId = teamComposition.getParentTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

}
