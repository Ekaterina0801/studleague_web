package com.studleague.studleague.mappers.role;

import com.studleague.studleague.dto.security.RoleDTO;
import com.studleague.studleague.entities.security.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {


    Role mapToEntity(RoleDTO roleDTO);


    RoleDTO mapToDto(Role role);
}
