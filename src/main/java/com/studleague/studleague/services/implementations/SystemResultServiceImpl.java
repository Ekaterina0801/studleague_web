package com.studleague.studleague.services.implementations;

import com.studleague.studleague.entities.League;
import com.studleague.studleague.entities.SystemResult;
import com.studleague.studleague.repository.SystemResultRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.SystemResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemResultServiceImpl implements SystemResultService {

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private SystemResultRepository systemResultRepository;

    @Override
    @Transactional(readOnly = true)
    public SystemResult findById(Long id) {
        return entityRetrievalUtils.getSystemResultOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemResult> findAll() {
        return systemResultRepository.findAll();
    }

    public void deleteAll() {
        systemResultRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SystemResult findByName(String name) {
        return entityRetrievalUtils.getSystemResultByNameOrThrow(name);
    }

    @Override
    @Transactional
    public void save(SystemResult systemResult) {
        Long id = systemResult.getId();
        if (id != null) {
            if (systemResultRepository.existsById(id)) {
                SystemResult existingSystemResult = entityRetrievalUtils.getSystemResultOrThrow(id);
                update(existingSystemResult, systemResult);
            }
        } else if (systemResultRepository.existsByNameIgnoreCase(systemResult.getName())) {
            SystemResult systemResultExisting = entityRetrievalUtils.getSystemResultByNameOrThrow(systemResult.getName());
            update(systemResultExisting, systemResult);
        } else {
            systemResultRepository.save(systemResult);
        }
    }

    @Transactional
    private void update(SystemResult systemResultExisting, SystemResult systemResult) {
        systemResultExisting.setLeagues(systemResult.getLeagues());
        systemResultExisting.setName(systemResult.getName());
        systemResultExisting.setDescription(systemResult.getDescription());
        systemResultExisting.setCountNotIncludedGames(systemResult.getCountNotIncludedGames());
        systemResultRepository.save(systemResultExisting);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        systemResultRepository.deleteById(id);
    }

    @Override
    public SystemResult addLeagueToSystemResult(Long systemResultId, Long leagueId) {
        SystemResult systemResult = entityRetrievalUtils.getSystemResultOrThrow(systemResultId);
        League league = entityRetrievalUtils.getLeagueOrThrow(leagueId);
        if (!systemResult.getLeagues().contains(league)) {
            systemResult.getLeagues().add(league);
        }
        return systemResult;

    }
}
