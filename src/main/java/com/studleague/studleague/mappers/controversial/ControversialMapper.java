package com.studleague.studleague.mappers.controversial;

import com.studleague.studleague.dto.controversial.ControversialDTO;
import com.studleague.studleague.entities.Controversial;
import com.studleague.studleague.mappers.MappingUtils;
import com.studleague.studleague.mappers.result.ResultMainInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ResultMainInfoMapper.class, MappingUtils.class})
public interface ControversialMapper {

    @Mapping(target = "fullResult", source = "fullResult")
    Controversial mapToEntity(ControversialDTO controversialDTO);

    ControversialDTO mapToDto(Controversial controversial);


}
