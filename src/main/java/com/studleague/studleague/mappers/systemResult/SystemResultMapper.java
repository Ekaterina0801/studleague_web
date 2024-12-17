package com.studleague.studleague.mappers.systemResult;

import com.studleague.studleague.dto.systemResult.SystemResultDTO;
import com.studleague.studleague.entities.SystemResult;
import com.studleague.studleague.mappers.MappingUtils;
import com.studleague.studleague.mappers.league.LeagueMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {LeagueMapper.class, MappingUtils.class})
public interface SystemResultMapper {


    @Mapping(target = "leaguesIds", source = "leagues", qualifiedByName = "leaguesToIds")
    SystemResultDTO mapToDto(SystemResult systemResult);


    @Mapping(target = "leagues", source = "leaguesIds", qualifiedByName = "leaguesIdsToEntities")
    SystemResult mapToEntity(SystemResultDTO systemResultDTO);


}
