package com.studleague.studleague.services.implementations;

import com.studleague.studleague.entities.security.Role;
import com.studleague.studleague.repository.security.RoleRepository;
import com.studleague.studleague.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("roleService")
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
