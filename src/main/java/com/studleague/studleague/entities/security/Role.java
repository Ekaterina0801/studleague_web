package com.studleague.studleague.entities.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
