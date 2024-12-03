package com.studleague.studleague.factory;

import com.studleague.studleague.dto.ResultMainInfoDTO;
import com.studleague.studleague.entities.FullResult;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ResultMainInfoFactory implements DTOFactory<ResultMainInfoDTO, FullResult> {

    @Autowired
    private TeamMainInfoFactory teamMainInfoFactory;

    @Override
    public FullResult mapToEntity(ResultMainInfoDTO dto) {
        return FullResult.builder()
                .team(teamMainInfoFactory.mapToEntity(dto.getTeam()))
                .build();
    }

    @Override
    public ResultMainInfoDTO mapToDto(FullResult entity) {
        return ResultMainInfoDTO.builder().team(teamMainInfoFactory.mapToDto(entity.getTeam())).build();
    }
}
