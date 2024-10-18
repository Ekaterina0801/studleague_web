package com.studleague.studleague.mappings;

import com.studleague.studleague.dao.interfaces.FullResultDao;
import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ControversialMapper {

    private final FullResultDao fullResultDao;

    public Controversial toEntity(ControversialDTO controversialDTO) {
        long resultId = controversialDTO.getFullResultId();
        FullResult fullResult = EntityRetrievalUtils.getEntityOrThrow(fullResultDao.getFullResultById(resultId), "FullResult", resultId);
        return Controversial.builder()
                .id(controversialDTO.getId())
                .answer(controversialDTO.getAnswer())
                .appealJuryComment(controversialDTO.getAppealJuryComment())
                .comment(controversialDTO.getComment())
                .fullResult(fullResult)
                .status(controversialDTO.getStatus())
                .questionNumber(controversialDTO.getQuestionNumber())
                .issuedAt(controversialDTO.getIssuedAt())
                .resolvedAt(controversialDTO.getResolvedAt())
                .build();
    }

    public ControversialDTO toDto(Controversial controversial) {
        return ControversialDTO.builder()
                .id(controversial.getId())
                .answer(controversial.getAnswer())
                .appealJuryComment(controversial.getAppealJuryComment())
                .comment(controversial.getComment())
                .fullResultId(controversial.getFullResult().getId())
                .status(controversial.getStatus())
                .questionNumber(controversial.getQuestionNumber())
                .issuedAt(controversial.getIssuedAt())
                .resolvedAt(controversial.getResolvedAt())
                .build();
    }

    public List<Controversial> convertDTOListToEntityList(List<ControversialDTO> dtos) {
        List<Controversial> controversies = new ArrayList<>();

        for (ControversialDTO dto : dtos) {
            Controversial controversial = toEntity(dto);
            controversies.add(controversial);
        }
        return controversies;
    }

    public List<ControversialDTO> convertEntityListToDTOList(List<Controversial> entityList) {
        List<ControversialDTO> controversialDTOS = new ArrayList<>();
        for (Controversial entity : entityList) {
            ControversialDTO controversialDTO =  toDto(entity);
            controversialDTOS.add(controversialDTO);
        }
        return controversialDTOS;
    }
}
