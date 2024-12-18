package com.studleague.studleague.mappers.user;


import com.studleague.studleague.dto.security.UserMainInfoDTO;
import com.studleague.studleague.entities.security.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMainInfoMapper {

    User mapToEntity(UserMainInfoDTO userDTO);

    UserMainInfoDTO mapToDto(User user);
}
