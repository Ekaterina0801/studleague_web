package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.FlagDTO;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.entities.Team;
import com.studleague.studleague.mappers.FlagMapper;
import com.studleague.studleague.repository.FlagRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.FlagService;
import com.studleague.studleague.services.interfaces.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("flagService")
public class FlagServiceImpl implements FlagService {

    @Autowired
    private FlagRepository flagRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private FlagMapper flagMapper;


    @Override
    @Transactional(readOnly = true)
    public List<Flag> getAllFlags() {
        return flagRepository.findAll();
    }

    @Override
    @Transactional
    public void saveFlag(Flag flag) {
        String name = flag.getName();
        Long id = flag.getId();

        if (id != null) {
            if (flagRepository.existsById(id)) {
                Flag existingFlag = entityRetrievalUtils.getFlagOrThrow(id);
                updateFlag(existingFlag, flag);
            }
        } else if (flagRepository.existsByNameIgnoreCase(name)) {
            Flag existingFlag = entityRetrievalUtils.getFlagByNameOrThrow(name);
            updateFlag(existingFlag, flag);
        } else {

            for (Team team : flag.getTeams()) {
                team.addFlagToTeam(flag);
            }
            flagRepository.save(flag);
        }
    }
    @Transactional
    private void updateFlag(Flag existingFlag, Flag newFlag) {
        existingFlag.setName(newFlag.getName());
        existingFlag.setLeague(newFlag.getLeague());

        for (Team team : newFlag.getTeams()) {
            if (!existingFlag.getTeams().contains(team)) {
                existingFlag.addTeamToFlag(team);
                team.addFlagToTeam(existingFlag);
            }
        }

        flagRepository.save(existingFlag);
    }


    @Override
    @Transactional(readOnly = true)
    public Flag getFlagById(Long id) {
        return entityRetrievalUtils.getFlagOrThrow(id);
    }

    @Override
    @Transactional
    public void deleteFlag(Long id) {
        flagRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllFlags() {
        flagRepository.deleteAll();
    }

    @Override
    public boolean isManager(Long userId, Long flagId) {
        if (userId == null)
            return false;
        Flag flag = entityRetrievalUtils.getFlagOrThrow(flagId);
        Long leagueId = flag.getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public boolean isManager(Long userId, FlagDTO flagDTO) {
        if (userId == null)
            return false;
        Flag flag = flagMapper.mapToEntity(flagDTO);
        Long leagueId = flag.getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }
}
