package com.studleague.studleague.mappers.league;

import com.studleague.studleague.dto.league.LeagueMainInfoDTO;
import com.studleague.studleague.entities.League;
import com.studleague.studleague.mappers.MappingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = MappingUtils.class)
public interface LeagueMainInfoMapper {

    @Mapping(target = "systemResult", source = "systemResultId", qualifiedByName = "systemResultIdToSystemResult")
    @Mapping(target = "createdBy", source = "createdById", qualifiedByName = "userIdToUser")
    League mapToEntity(LeagueMainInfoDTO dto);

    @Mapping(target = "systemResultId", source = "systemResult.id")
    @Mapping(target = "createdById", source = "createdBy.id")
    LeagueMainInfoDTO mapToDto(League entity);


}

