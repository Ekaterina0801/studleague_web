package com.studleague.studleague.services.implementations;

import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.repository.ControversialRepository;
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
    private ControversialRepository controversialRepository;


    @Override
    @Transactional
    public List<Controversial> getAllControversials() {
        return controversialRepository.findAll();
    }

    @Override
    @Transactional
    public void saveControversial(Controversial controversial) {
        Long resultId = controversial.getFullResult().getId();
        int questionNumber = controversial.getQuestionNumber();
        if (controversialRepository.existsByFullResultIdAndQuestionNumber(resultId,questionNumber))
        {

            controversialRepository.save(EntityRetrievalUtils.getEntityByTwoIdOrThrow(controversialRepository.findByFullResultIdAndQuestionNumber(resultId, questionNumber), "Controversial", resultId, questionNumber));
        }
        else
        {
            controversialRepository.save(controversial);
        }
    }

    @Override
    @Transactional
    public Controversial getControversialById(Long id) {
        return EntityRetrievalUtils.getEntityOrThrow(controversialRepository.findById(id),"Controversial", id);
    }

    @Override
    @Transactional
    public void deleteControversial(Long id) {
        controversialRepository.deleteById(id);
    }

    @Override
    @Transactional
    public HashMap<Integer, Controversial> getControversialsByTeamIdWithQuestionNumber(Long teamId) {

        List<Controversial> controversials = controversialRepository.findAllByTeamId(teamId);
        return mapControversialsByNumber(controversials);
    }

    @Override
    @Transactional
    public List<Controversial> getControversialsByTeamId(Long teamId) {
        return controversialRepository.findAllByTeamId(teamId);
    }

    @Override
    @Transactional
    public HashMap<Integer, Controversial> getControversialsByTournamentIdWithQuestionNumber(Long tournamentId) {
        List<Controversial> controversials = controversialRepository.findAllByTournamentId(tournamentId);
        return mapControversialsByNumber(controversials);
    }

    @Override
    @Transactional
    public List<Controversial> getControversialsByTournamentId(Long tournamentId) {
        return controversialRepository.findAllByTournamentId(tournamentId);
    }

    private HashMap<Integer, Controversial> mapControversialsByNumber(List<Controversial> controversials) {
        HashMap<Integer, Controversial> map = new HashMap<>();
        for (Controversial controversial : controversials) {
            map.put(controversial.getQuestionNumber(), controversial);
        }
        return map;
    }

    @Transactional
    public void deleteAllControversials()
    {
        controversialRepository.deleteAll();
    }

}
