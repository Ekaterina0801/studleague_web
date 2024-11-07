package com.studleague.studleague.services.implementations;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.SystemResult;
import com.studleague.studleague.repository.SystemResultRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.SystemResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class SystemResultServiceImpl implements SystemResultService {

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private SystemResultRepository systemResultRepository;

    @Override
    public SystemResult findById(Long id) {
        return entityRetrievalUtils.getSystemResultOrThrow(id);
    }

    @Override
    public List<SystemResult> findAll()
    {
        return systemResultRepository.findAll();
    }

    public void deleteAll()
    {
        systemResultRepository.deleteAll();
    }

    @Override
    public SystemResult findByName(String name) {
        return entityRetrievalUtils.getSystemResultByNameOrThrow(name);
    }

    @Transactional
    public void save(SystemResult systemResult) {
        if (systemResultRepository.findByNameIgnoreCase(systemResult.getName()).isPresent())
        {
            SystemResult systemResultExisting = entityRetrievalUtils.getSystemResultByNameOrThrow(systemResult.getName());
            systemResultExisting.setLeagues(systemResult.getLeagues());
        }
        else {
            systemResultRepository.save(systemResult);
        }
    }

    @Transactional
    public void deleteById(Long id)
    {
        systemResultRepository.deleteById(id);
    }

    public SystemResult addLeagueToSystemResult(Long systemResultId, Long leagueId)
    {
        SystemResult systemResult = entityRetrievalUtils.getSystemResultOrThrow(systemResultId);
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        if (!systemResult.getLeagues().contains(league))
        {
            systemResult.getLeagues().add(league);
        }
        return systemResult;

    }
}
