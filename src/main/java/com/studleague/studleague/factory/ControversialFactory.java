package com.studleague.studleague.factory;

import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class ControversialFactory implements DTOFactory<ControversialDTO, Controversial>{

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    public Controversial mapToEntity(ControversialDTO controversialDTO) {
        FullResult fullResult = null;
        Long resultId = controversialDTO.getFullResultId();

        if (resultId != null) {
            fullResult = entityRetrievalUtils.getResultOrThrow(resultId);
        }

        return Controversial.builder()
                .id(controversialDTO.getSiteId())
                .answer(controversialDTO.getAnswer())
                .appealJuryComment(controversialDTO.getAppealJuryComment())
                .comment(controversialDTO.getComment())
                .status(controversialDTO.getStatus())
                .fullResult(fullResult)
                .questionNumber(controversialDTO.getQuestionNumber())
                .issuedAt(controversialDTO.getIssuedAt())
                .resolvedAt(controversialDTO.getResolvedAt())
                .build();
    }


    public ControversialDTO mapToDto(Controversial controversial) {
        return ControversialDTO.builder()
                .siteId(controversial.getId())
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
}
