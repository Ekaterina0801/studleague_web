package com.studleague.studleague.controllers;


import com.studleague.studleague.dto.league.LeagueMainInfoDTO;
import com.studleague.studleague.dto.security.RoleDTO;
import com.studleague.studleague.dto.security.UserMainInfoDTO;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.mappers.league.LeagueMainInfoMapper;
import com.studleague.studleague.mappers.role.RoleMapper;
import com.studleague.studleague.mappers.user.UserMainInfoMapper;
import com.studleague.studleague.services.implementations.security.UserService;
import com.studleague.studleague.services.interfaces.RoleService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final LeagueMainInfoMapper leagueMainInfoMapper;

    private final UserService userService;

    private final UserMainInfoMapper userMainInfoMapper;

    private final RoleService roleService;

    private final RoleMapper roleMapper;

    @GetMapping("/leagues")
    public ResponseEntity<List<LeagueMainInfoDTO>> getLeaguesByUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.ok(null);
        }
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(user.getLeagues().stream().map(leagueMainInfoMapper::mapToDto).collect(Collectors.toList()));
    }

    @GetMapping()
    public ResponseEntity<List<UserMainInfoDTO>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "fullname,asc") String sort
    ) {
        try {
            Sort sortOrder = Sort.by(Sort.Order.by(sort.split(",")[0]).with(Sort.Direction.fromString(sort.split(",")[1])));
            List<User> users;

            if (username == null || username.trim().isEmpty()) {
                users = userService.findAll(sortOrder);
            } else {
                users = userService.searchUsers(username, sortOrder);
            }

            return ResponseEntity.ok(users.stream().map(userMainInfoMapper::mapToDto).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserMainInfoDTO> updateUser(@PathVariable Long id, @RequestBody UserMainInfoDTO userDto) {
        try {
            User user = userMainInfoMapper.mapToEntity(userDto);
            user.setId(id);
            User updatedUser = userService.update(user);
            return ResponseEntity.ok(userMainInfoMapper.mapToDto(updatedUser));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getRoles() {
        try {
            List<RoleDTO> roles = roleService.getAllRoles().stream().map(roleMapper::mapToDto).collect(Collectors.toList());
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserMainInfoDTO> getCurrentUser() {
        try {
            User currentUser = userService.getCurrentUser();
            UserMainInfoDTO userDto = userMainInfoMapper.mapToDto(currentUser);
            return ResponseEntity.ok(userDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


}
