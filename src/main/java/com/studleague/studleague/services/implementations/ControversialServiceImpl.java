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
        return controversialDao.getAllControversials();
    }

    @Override
    @Transactional
    public void saveControversial(Controversial Controversial) {
        controversialDao.saveControversial(Controversial);

    }

    @Override
    @Transactional
    public Controversial getControversialById(int id) {
        return controversialDao.getControversialById(id).orElse(new Controversial());
    }

    @Override
    @Transactional
    public void deleteControversial(int id) {
        controversialDao.deleteControversial(id);
    }

    @Override
    @Transactional
    public HashMap<Integer, Controversial> getControversialsByTeamIdWithQuestionNumber(int teamId){

        List<Controversial> controversials = controversialDao.getControversialByTeamId(teamId);
        return mapControversialsByNumber(controversials);
    }

    @Override
    @Transactional
    public List<Controversial> getControversialsByTeamId(int teamId)
    {
        return controversialDao.getControversialByTeamId(teamId);
    }

    @Override
    @Transactional
    public HashMap<Integer, Controversial> getControversialByTournamentIdWithQuestionNumber(int tournamentId)
    {
        List<Controversial> controversials = controversialDao.getControversialByTournamentId(tournamentId)
        return mapControversialsByNumber(controversials);
    }

    @Override
    @Transactional
    public List<Controversial> getControversialByTournamentList(int tournament_id)
    {
        return controversialDao.getControversialByTournamentId(tournament_id).values().stream().toList();
    }

    private HashMap<Integer, Controversial> mapControversialsByNumber(List<Controversial> controversials) {
        HashMap<Integer, Controversial> map = new HashMap<>();
        for (Controversial controversial : controversials) {
            map.put(controversial.getQuestionNumber(), controversial);
        }
        return map;
    }

}
