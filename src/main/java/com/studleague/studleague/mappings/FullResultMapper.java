package com.studleague.studleague.mappings;

import com.studleague.studleague.dao.interfaces.FullResultDao;
import com.studleague.studleague.dao.interfaces.TeamDao;
import com.studleague.studleague.dao.interfaces.TournamentDao;
import com.studleague.studleague.dto.FullResultDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FullResultMapper {

    @Autowired
    FullResultDao fullResultDao;

    @Autowired
    TeamDao teamDao;

    @Autowired
    TournamentDao tournamentDao;

    @Autowired
    ControversialMapper controversialMapper;

    FullResultMapper(){}

    public FullResult toEntity(FullResultDTO fullResultDTO){
        FullResult fullResult = new FullResult();
        fullResult.setId(fullResultDTO.getId());
        fullResult.setTeam(teamDao.getTeamById(fullResultDTO.getTeam_id()));
        fullResult.setTournament(tournamentDao.getTournamentById(fullResultDTO.getTournament_id()));
        fullResult.setMask_results(fullResultDTO.getMask_results());
        List<Controversial> controversialList = controversialMapper.convertDTOListToEntityList(fullResultDTO.getControversials());
        fullResult.setControversials(controversialList);
        return fullResult;
    }

    public FullResultDTO toDTO(FullResult fullResult){
        FullResultDTO fullResultDTO = new FullResultDTO();
        fullResultDTO.setId(fullResult.getId());
        fullResultDTO.setMask_results(fullResult.getMask_results());
        fullResultDTO.setTeam_id(fullResult.getTeam().getId());
        fullResultDTO.setTournament_id(fullResult.getTournament().getId());
        fullResultDTO.setControversials(controversialMapper.convertEntityListToDTOList(fullResult.getControversials()));
        return fullResultDTO;
    }

}
