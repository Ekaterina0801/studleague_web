package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.controversial.ControversialDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.mappers.controversial.ControversialMapper;
import com.studleague.studleague.repository.ControversialRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import com.studleague.studleague.services.interfaces.ControversialService;
import com.studleague.studleague.services.interfaces.LeagueService;
import com.studleague.studleague.specifications.ControversialSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service("controversialService")
public class ControversialServiceImpl implements ControversialService {

    @Autowired
    private ControversialRepository controversialRepository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    private LeagueService leagueService;


    @Autowired
    private ControversialMapper controversialMapper;


    @Override
    @Transactional(readOnly = true)
    public List<Controversial> getAllControversials() {
        return controversialRepository.findAll();
    }

    @Override
    @Transactional
    public void saveControversial(Controversial controversial) {
        Long resultId = controversial.getFullResult().getId();
        int questionNumber = controversial.getQuestionNumber();
        if (controversial.getId() != null) {
            if (controversialRepository.existsById(controversial.getId())) {
                Controversial existingControversial = entityRetrievalUtils.getControversialOrThrow(controversial.getId());
                updateControversial(existingControversial, controversial);
            }
            else{
                controversialRepository.save(controversial);
            }
        } else if (controversialRepository.existsByFullResultIdAndQuestionNumber(resultId, questionNumber)) {
            Controversial existingControversial = entityRetrievalUtils.getControversialByResultIdAndQuestionNumberOrThrow(resultId, questionNumber);
            updateControversial(existingControversial, controversial);
        } else {
            controversialRepository.save(controversial);
        }
    }

    @Transactional
    private void updateControversial(Controversial existingControversial, Controversial controversial) {
        existingControversial.setAnswer(controversial.getAnswer());
        existingControversial.setComment(controversial.getComment());
        existingControversial.setAppealJuryComment(controversial.getAppealJuryComment());
        existingControversial.setIssuedAt(controversial.getIssuedAt());
        existingControversial.setResolvedAt(controversial.getResolvedAt());
        existingControversial.setStatus(controversial.getStatus());
        existingControversial.setFullResult(controversial.getFullResult());

        if (existingControversial.getQuestionNumber() != controversial.getQuestionNumber()) {
            existingControversial.setQuestionNumber(controversial.getQuestionNumber());
        }

        controversialRepository.save(existingControversial);
    }


    @Override
    @Transactional(readOnly = true)
    public Controversial getControversialById(Long id) {
        return entityRetrievalUtils.getControversialOrThrow(id);
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
    @Transactional(readOnly = true)
    public List<Controversial> getControversialsByTeamId(Long teamId) {
        return controversialRepository.findAllByTeamId(teamId);
    }

    @Override
    @Transactional(readOnly = true)
    public HashMap<Integer, Controversial> getControversialsByTournamentIdWithQuestionNumber(Long tournamentId) {
        List<Controversial> controversials = controversialRepository.findAllByTournamentId(tournamentId);
        return mapControversialsByNumber(controversials);
    }

    @Override
    @Transactional(readOnly = true)
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
    public void deleteAllControversials() {
        controversialRepository.deleteAll();
    }


    @Override
    public boolean isManager(Long userId, Long controversialId) {
        if (userId == null)
            return false;
        Controversial controversial = entityRetrievalUtils.getControversialOrThrow(controversialId);
        Long leagueId = controversial.getFullResult().getTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    public boolean isManager(Long userId, ControversialDTO controversialDTO) {
        if (userId == null)
            return false;
        Controversial controversial = controversialMapper.mapToEntity(controversialDTO);
        Long leagueId = controversial.getFullResult().getTeam().getLeague().getId();
        return leagueService.isManager(userId, leagueId);
    }

    @Override
    @Transactional
    public List<Controversial> searchControversials(Integer questionNumber, List<String> statuses, LocalDateTime startDate, LocalDateTime endDate, Long fullResultId, List<String> sortBy, List<String> sortOrder) {
        Specification<Controversial> spec = ControversialSpecification.searchControversials(questionNumber, statuses, startDate, endDate, fullResultId);
        Sort sort = ControversialSpecification.sortBy(sortBy, sortOrder);
        return controversialRepository.findAll(spec, sort);
    }

}
