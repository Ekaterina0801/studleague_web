package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.implementations.FlagDaoImpl;
import com.studleague.studleague.entities.Flag;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.FlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FlagServiceImpl implements FlagService {

    @Autowired
    private FlagDaoImpl flagDao;


    @Override
    @Transactional
    public List<Flag> getAllFlags() {
        return flagDao.getAllFlags();
    }

    @Override
    @Transactional
    public void saveFlag(Flag flag) {
        flagDao.saveFlag(flag);

    }

    @Override
    @Transactional
    public Flag getFlagById(int id) {
        return EntityRetrievalUtils.getEntityOrThrow(flagDao.getFlagById(id), "Flag", id);
    }

    @Override
    @Transactional
    public void deleteFlag(int id) {
        flagDao.deleteFlag(id);
    }
}
