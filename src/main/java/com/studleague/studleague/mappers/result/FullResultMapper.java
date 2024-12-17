package com.studleague.studleague.mappers.result;

import com.studleague.studleague.dto.result.FullResultDTO;
import com.studleague.studleague.entities.FullResult;
import com.studleague.studleague.mappers.MappingUtils;
import com.studleague.studleague.mappers.controversial.ControversialMapper;
import com.studleague.studleague.mappers.team.TeamMainInfoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ControversialMapper.class, TeamMainInfoMapper.class, MappingUtils.class})
public interface FullResultMapper {


    @Mapping(target = "mask_results", source = "maskResults")
    @Mapping(target = "tournament", source = "tournamentId", qualifiedByName = "tournamentIdToTournament")
    @Mapping(target = "controversials", source = "controversials")
    FullResult mapToEntity(FullResultDTO fullResultDTO);

    @Mapping(target = "maskResults", source = "mask_results")
    @Mapping(target = "tournamentId", source = "tournament.id")
    @Mapping(target = "controversials", source = "controversials")
    FullResultDTO mapToDto(FullResult fullResult);


    List<FullResultDTO> toFullResultDTOList(List<FullResultDTO> results);
}
