package com.studleague.studleague.entities.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleName {
    ADMIN("ROLE_ADMIN"),
    EDITOR("ROLE_EDITOR");

    private final String role;

}

