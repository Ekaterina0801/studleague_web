package com.studleague.studleague.factory;

import com.studleague.studleague.dto.UserDTO;
import com.studleague.studleague.entities.security.Role;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.repository.security.RoleRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final RoleRepository roleRepository;

    public User toEntity(UserDTO userDTO)
    {
        List<Role> roles = new ArrayList<>();
        for (Long id: userDTO.getRolesIds())
            roles.add(EntityRetrievalUtils.getEntityOrThrow(roleRepository.findById(id), "Role", id));
        return User.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .fullname(userDTO.getFullname())
                .roles(roles)
                .build();
    }

    public UserDTO toDTO(User user)
    {
        return UserDTO.builder()
                .email(user.getEmail())
                .fullname(user.getFullname())
                .username(user.getUsername())
                .rolesIds(user.getRoles().stream().map(Role::getId).toList())
                .build();
    }
}
