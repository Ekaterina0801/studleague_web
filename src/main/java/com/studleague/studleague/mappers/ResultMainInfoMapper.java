package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.ResultMainInfoDTO;
import com.studleague.studleague.entities.FullResult;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ResultMainInfoMapper implements DTOMapper<ResultMainInfoDTO, FullResult> {

    @Autowired
    private TeamMainInfoMapper teamMainInfoMapper;

    @Override
    public FullResult mapToEntity(ResultMainInfoDTO dto) {
        return FullResult.builder()
                .team(teamMainInfoMapper.mapToEntity(dto.getTeam()))
                .build();
    }

    @Override
    public ResultMainInfoDTO mapToDto(FullResult entity) {
        return ResultMainInfoDTO.builder().team(teamMainInfoMapper.mapToDto(entity.getTeam())).build();
    }
}
