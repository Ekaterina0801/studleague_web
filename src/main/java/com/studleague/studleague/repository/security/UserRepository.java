package com.studleague.studleague.repository.security;

import com.studleague.studleague.entities.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
