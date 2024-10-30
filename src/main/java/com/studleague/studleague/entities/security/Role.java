package com.studleague.studleague.entities.security;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Override
    public String getAuthority() {
        return getName();
    }

    public static Role adminRole() {
        return new Role(1L, RoleName.ADMIN.getRole());
    }

    public static Role editorRole() {
        return new Role(2L, RoleName.EDITOR.getRole());
    }
}
