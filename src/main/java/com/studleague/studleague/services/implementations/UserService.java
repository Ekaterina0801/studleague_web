package com.studleague.studleague.services.implementations;

import com.studleague.studleague.dto.RoleDTO;
import com.studleague.studleague.dto.UserDTO;
import com.studleague.studleague.entities.security.Role;
import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.repository.security.RoleRepository;
import com.studleague.studleague.repository.security.UserRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageSource messages;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User createUser(UserDTO userDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
        if (optionalUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = User.builder()
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .username(userDto.getUsername())
                .build();
        user.setRoles(List.of(roleRepository.findByNameIgnoreCase("ROLE_EDITOR").orElseThrow()));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User findUserByUsername(String username) throws NotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        return optionalUser.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return EntityRetrievalUtils.getEntityByNameOrThrow(userRepository.findByUsername(username), "User", username);
    }

    public void addRole(Long userId, RoleDTO roleDto) {
        User user = EntityRetrievalUtils.getEntityOrThrow(userRepository.findById(userId), "User", userId);
        Role role = EntityRetrievalUtils.getEntityByNameOrThrow(roleRepository.findByNameIgnoreCase(roleDto.getName()), "User", roleDto.getName());
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public User getCurrentUser() throws NotFoundException{
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findUserByUsername(username);
    }
}
