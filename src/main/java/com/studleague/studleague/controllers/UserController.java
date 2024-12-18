package com.studleague.studleague.controllers;


import com.studleague.studleague.dto.league.LeagueMainInfoDTO;
import com.studleague.studleague.dto.security.UserMainInfoDTO;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.mappers.league.LeagueMainInfoMapper;
import com.studleague.studleague.mappers.user.UserMainInfoMapper;
import com.studleague.studleague.services.implementations.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final LeagueMainInfoMapper leagueMainInfoMapper;

    private final UserService userService;

    private final UserMainInfoMapper userMainInfoMapper;

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
            @RequestParam String username,
            @RequestParam(defaultValue = "fullname,asc") String sort
    ) {
        try {

            Sort sortOrder = Sort.by(Sort.Order.by(sort.split(",")[0]).with(Sort.Direction.fromString(sort.split(",")[1])));
            List<User> users = userService.searchUsers(username, sortOrder);
            return ResponseEntity.ok(users.stream().map(userMainInfoMapper::mapToDto).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
