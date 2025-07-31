package ru.practicum.shareit.user.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class UserMemoryStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, Long> emailToId = new HashMap<>();
    private Long counterId = 0L;

    public void validateEmail(User newUser) {
        Long existingUserId = emailToId.get(newUser.getEmail());
        if (existingUserId != null && !existingUserId.equals(newUser.getId())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }
    }

    @Override
    public User add(User user) {
        log.info("Создание нового пользователя [{}]", user);
        try {
            validateEmail(user);
            user.setId(++counterId);
            users.put(user.getId(), user);
            emailToId.put(user.getEmail(), user.getId());
            log.info("Пользователь создан [{}], присвоен id [{}]", user, user.getId());
            return user;
        } catch (InternalServerException ex) {
            throw new RuntimeException("Не удалось создать пользователя!");
        }
    }

    @Override
    public User update(User user) {
        log.info("Обновление пользователя с id [{}], данные [{}]", user.getId(), user);
        if (user.getId() == null) {
            throw new ValidationException("Для обновления пользователя необходимо указать id!");
        }
        User existingUser = users.get(user.getId());
        if (existingUser == null) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден!");
        }
        String oldEmail = existingUser.getEmail();
        String newEmail = user.getEmail();
        validateEmail(user);
        users.put(user.getId(), user);
        if (!oldEmail.equals(newEmail)) {
            emailToId.remove(oldEmail);
            emailToId.put(newEmail, user.getId());
        }
        log.info("Пользователь с id [{}] успешно обновлён: [{}]", user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long userId) {
        User removedUser = users.remove(userId);
        if (removedUser != null) {
            emailToId.remove(removedUser.getEmail());
        }
    }

    @Override
    public User getById(Long userId) {
        Optional<User> user = Optional.ofNullable(users.get(userId));
        if (user.isEmpty()) {
            throw new NotFoundException("Пользователь с данным id не найден!");
        }
        return user.get();
    }

}