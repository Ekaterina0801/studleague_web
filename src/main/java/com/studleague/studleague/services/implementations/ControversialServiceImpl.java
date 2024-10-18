package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dao.interfaces.ControversialDao;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.services.EntityRetrievalUtils;
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
        return controversialDao.getAllControversials();
    }

    @Override
    @Transactional
    public void saveControversial(Controversial Controversial) {
        controversialDao.saveControversial(Controversial);

    }

    @Override
    @Transactional
    public Controversial getControversialById(Long id) {
        return EntityRetrievalUtils.getEntityOrThrow(controversialDao.getControversialById(id),"Controversial", id);
    }

    @Override
    @Transactional
    public void deleteControversial(Long id) {
        controversialDao.deleteControversial(id);
    }

    @Override
    @Transactional
    public HashMap<Integer, Controversial> getControversialsByTeamIdWithQuestionNumber(Long teamId) {

        List<Controversial> controversials = controversialDao.getControversialByTeamId(teamId);
        return mapControversialsByNumber(controversials);
    }

    @Override
    @Transactional
    public List<Controversial> getControversialsByTeamId(Long teamId) {
        return controversialDao.getControversialByTeamId(teamId);
    }

    @Override
    @Transactional
    public HashMap<Integer, Controversial> getControversialsByTournamentIdWithQuestionNumber(Long tournamentId) {
        List<Controversial> controversials = controversialDao.getControversialByTournamentId(tournamentId);
        return mapControversialsByNumber(controversials);
    }

    @Override
    @Transactional
    public List<Controversial> getControversialsByTournamentId(Long tournamentId) {
        return controversialDao.getControversialByTournamentId(tournamentId);
    }

    private HashMap<Integer, Controversial> mapControversialsByNumber(List<Controversial> controversials) {
        HashMap<Integer, Controversial> map = new HashMap<>();
        for (Controversial controversial : controversials) {
            map.put(controversial.getQuestionNumber(), controversial);
        }
        return map;
    }

}
