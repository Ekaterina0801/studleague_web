package com.studleague.studleague.services.implementations.security;

import com.studleague.studleague.entities.security.User;
import com.studleague.studleague.repository.security.UserRepository;
import com.studleague.studleague.services.EntityRetrievalUtils;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Autowired
    private EntityRetrievalUtils entityRetrievalUtils;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }

    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public User create(User user) throws NotFoundException {
        if (repository.existsByUsername(user.getUsername())) {
            throw new NotFoundException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new NotFoundException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public User getByUsername(String username) throws NotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    /**
     * Получение пользователя для Spring Security
     *
     * @return метод получения пользователя по имени
     */
    public User findUserByUsername(String username) {
        return entityRetrievalUtils.getUserByUsernameOrThrow(username);
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() throws NotFoundException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotFoundException("Пользователь не аутентифицирован");
        }
        var username = authentication.getName();
        return getByUsername(username);
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getId();
        }
        return null;
    }


}
