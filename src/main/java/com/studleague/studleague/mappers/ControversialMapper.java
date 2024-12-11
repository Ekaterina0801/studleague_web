package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControversialMapper implements DTOMapper<ControversialDTO, Controversial> {

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    @Autowired
    @Lazy
    private ResultMainInfoMapper resultMainInfoMapper;

    public Controversial mapToEntity(ControversialDTO controversialDTO) {
        return Controversial.builder()
                .id(controversialDTO.getId())
                .answer(controversialDTO.getAnswer())
                .appealJuryComment(controversialDTO.getAppealJuryComment())
                .comment(controversialDTO.getComment())
                .status(controversialDTO.getStatus())
                .fullResult(controversialDTO.getFullResult() != null ?
                        resultMainInfoMapper.mapToEntity(controversialDTO.getFullResult()) : null)
                .questionNumber(controversialDTO.getQuestionNumber())
                .issuedAt(controversialDTO.getIssuedAt())
                .resolvedAt(controversialDTO.getResolvedAt())
                .build();
    }

    public ControversialDTO mapToDto(Controversial controversial) {
        return ControversialDTO.builder()
                .id(controversial.getId())
                .answer(controversial.getAnswer())
                .appealJuryComment(controversial.getAppealJuryComment())
                .comment(controversial.getComment())
                .fullResult(controversial.getFullResult() != null ?
                        resultMainInfoMapper.mapToDto(controversial.getFullResult()) : null)
                .status(controversial.getStatus())
                .questionNumber(controversial.getQuestionNumber())
                .issuedAt(controversial.getIssuedAt())
                .resolvedAt(controversial.getResolvedAt())
                .build();
    }
}
