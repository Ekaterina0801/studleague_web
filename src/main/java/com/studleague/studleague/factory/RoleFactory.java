package com.studleague.studleague.factory;

import com.studleague.studleague.dto.RoleDTO;
import com.studleague.studleague.entities.security.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {

    public Role toEntity(RoleDTO roleDTO)
    {
        return Role.builder()
                .name(roleDTO.getName())
                .build();
    }

    public RoleDTO toDTO(Role role)
    {
        return RoleDTO.builder()
                .name(role.getName())
                .build();
    }
}
