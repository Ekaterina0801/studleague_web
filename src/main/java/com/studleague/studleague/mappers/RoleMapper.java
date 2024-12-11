package com.studleague.studleague.mappers;

import com.studleague.studleague.dto.RoleDTO;
import com.studleague.studleague.entities.security.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements DTOMapper<RoleDTO, Role> {

    public Role mapToEntity(RoleDTO roleDTO)
    {
        return Role.builder()
                .name(roleDTO.getName())
                .build();
    }

    public RoleDTO mapToDto(Role role)
    {
        return RoleDTO.builder()
                .name(role.getName())
                .build();
    }
}
