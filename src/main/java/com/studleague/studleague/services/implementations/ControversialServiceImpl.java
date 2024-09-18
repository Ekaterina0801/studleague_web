package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.ControversialDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.services.interfaces.ControversialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
public class ControversialServiceImpl implements ControversialService {

    @Autowired
    private ControversialDao controversialDao;

    @Override
    @Transactional
    public List<Controversial> getAllControversials() {
        List<Controversial> allControversials = controversialDao.getAllControversials();
        return allControversials;
    }

    @Override
    @Transactional
    public void saveControversial(Controversial Controversial) {
        controversialDao.saveControversial(Controversial);

    }

    @Override
    @Transactional
    public Controversial getControversialById(int id) {
        Controversial controversial = controversialDao.getControversialById(id);
        return controversial;
    }

    @Override
    @Transactional
    public void updateControversial(Controversial controversial, String[] params) {
        controversialDao.updateControversial(controversial,params);
    }

    @Override
    @Transactional
    public void deleteControversial(int id) {
        controversialDao.deleteControversial(id);
    }

    @Override
    @Transactional
    public HashMap<Integer, Controversial> getControversialByTeamId(int team_id){
        return controversialDao.getControversialByTeamId(team_id);
    }

}
