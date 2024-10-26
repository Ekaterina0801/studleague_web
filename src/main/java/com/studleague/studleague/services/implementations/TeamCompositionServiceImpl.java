package com.studleague.studleague.services.implementations;
import com.studleague.studleague.entities.TeamComposition;
import com.studleague.studleague.repository.TeamCompositionRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.TeamCompositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamCompositionServiceImpl implements TeamCompositionService {

    @Autowired
    TeamCompositionRepository teamCompositionRepository;

    @Override
    @Transactional
    public TeamComposition findById(Long id) {
        return EntityRetrievalUtils.getEntityOrThrow(teamCompositionRepository.findById(id), "TeamComposition", id);
    }

    @Override
    public void save(TeamComposition teamComposition) {
        Long tournamentId = teamComposition.getTournament().getId();
        Long parentTeamId = teamComposition.getParentTeam().getId();
        if (!teamCompositionRepository.findByTournamentIdAndParentTeamId(tournamentId,parentTeamId).isEmpty())
        {
            TeamComposition teamCompositionExisting = EntityRetrievalUtils.getEntityByTwoIdOrThrow(teamCompositionRepository.findByTournamentIdAndParentTeamId(tournamentId,parentTeamId),"TeamComposition", tournamentId,parentTeamId);
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

}
