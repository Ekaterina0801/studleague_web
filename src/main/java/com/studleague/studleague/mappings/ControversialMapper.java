package com.studleague.studleague.mappings;

import com.studleague.studleague.dto.ControversialDTO;
import com.studleague.studleague.entities.Controversial;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ControversialMapper {

    public Controversial toEntity(ControversialDTO controversialDTO) {
        Controversial controversial = new Controversial(controversialDTO.getId(), controversialDTO.getQuestionNumber(), controversialDTO.getAnswer(), controversialDTO.getIssuedAt(), controversialDTO.getComment(), controversialDTO.getStatus(), controversialDTO.getResolvedAt(), controversialDTO.getAppealJuryComment());
        return controversial;
    }

    public List<Controversial> convertDTOListToEntityList(List<ControversialDTO> dtos) {
        List<Controversial> controversies = new ArrayList<>();

        for (ControversialDTO dto : dtos) {
            Controversial controversial = new Controversial();
            controversial.setId(dto.getId());
            controversial.setQuestionNumber(dto.getQuestionNumber());
            controversial.setAnswer(dto.getAnswer());
            controversial.setIssuedAt(dto.getIssuedAt());
            controversial.setStatus(dto.getStatus());
            controversial.setComment(dto.getComment());
            controversial.setResolvedAt(dto.getResolvedAt());
            controversial.setAppealJuryComment(dto.getAppealJuryComment());

            controversies.add(controversial);
        }
        return controversies;
    }

    public List<ControversialDTO> convertEntityListToDTOList(List<Controversial> entityList)
    {
        List<ControversialDTO> controversialDTOS = new ArrayList<>();
        for (Controversial entity:entityList){
            ControversialDTO controversialDTO = new ControversialDTO(entity.getId(),entity.getQuestionNumber(),entity.getAnswer(),entity.getIssuedAt(),entity.getStatus(), entity.getComment(), entity.getResolvedAt(),entity.getAppealJuryComment(),entity.getFullResult().getId());
            controversialDTOS.add(controversialDTO);
        }
        return controversialDTOS;
    }
}
