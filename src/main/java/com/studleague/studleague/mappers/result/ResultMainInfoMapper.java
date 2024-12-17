package com.studleague.studleague.mappers.result;

import com.studleague.studleague.dto.result.ResultMainInfoDTO;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.mappers.team.TeamMainInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = TeamMainInfoMapper.class)
public interface ResultMainInfoMapper {

    @Mapping(target = "tournament", ignore = true)
    @Mapping(target = "totalScore", ignore = true)
    @Mapping(target = "mask_results", ignore = true)
    @Mapping(target = "controversials", ignore = true)
    FullResult mapToEntity(ResultMainInfoDTO dto);

    ResultMainInfoDTO mapToDto(FullResult entity);
}
