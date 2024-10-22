package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.implementations.FlagDaoImpl;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.repository.FlagRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.FlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FlagServiceImpl implements FlagService {

    @Autowired
    //private FlagDao flagRepository;
    private FlagRepository flagRepository;


    @Override
    @Transactional
    public List<Flag> getAllFlags() {
        return flagRepository.findAll();
    }

    @Override
    @Transactional
    public void saveFlag(Flag flag) {
        flagRepository.save(flag);

    }

    @Override
    @Transactional
    public Flag getFlagById(long id) {
        return EntityRetrievalUtils.getEntityOrThrow(flagRepository.findById(id), "Flag", id);
    }

    @Override
    @Transactional
    public void deleteFlag(long id) {
        flagRepository.deleteById(id);
    }
}
