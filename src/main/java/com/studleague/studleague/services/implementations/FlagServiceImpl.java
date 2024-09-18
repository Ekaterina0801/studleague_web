package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.implementations.FlagDaoImpl;
import com.studleague.studleague.entities.Flag;
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
        List<Flag> allFlags = flagDao.getAllFlags();
        return allFlags;
    }

    @Override
    @Transactional
    public void saveFlag(Flag flag) {
        flagDao.saveFlag(flag);

    }

    @Override
    @Transactional
    public Flag getFlagById(int id) {
        Flag flag = flagDao.getFlagById(id);
        return flag;
    }

    @Override
    @Transactional
    public void updateFlag(Flag flag, String[] params) {
        flagDao.updateFlag(flag,params);
    }

    @Override
    @Transactional
    public void deleteFlag(int id) {
        flagDao.deleteFlag(id);
    }

/*    @Override
    public void getFlagsForTeam(int team_id) {

    }

    @Override
    public void setFlagForTeam(int team_id) {

    }*/
}
