package com.studleague.studleague.mappers.user;


import com.studleague.studleague.dto.security.UserMainInfoDTO;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.mappers.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoleMapper.class})
public interface UserMainInfoMapper {

    @Mapping(target = "role", source = "role")
    User mapToEntity(UserMainInfoDTO userDTO);

    @Mapping(target = "role", source = "role")
    UserMainInfoDTO mapToDto(User user);
}
