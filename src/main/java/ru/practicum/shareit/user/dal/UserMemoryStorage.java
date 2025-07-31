package ru.practicum.shareit.user.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
public class UserMemoryStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long counterId = 0L;

    private User findUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public void validateEmail(User newUser) {
        if (emails.contains(newUser.getEmail())) {
            User existingUser = findUserByEmail(newUser.getEmail());
            if (existingUser != null && !existingUser.getId().equals(newUser.getId())) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует!");
            }
        }
    }

    @Override
    public User add(User user) {
        log.info("Создание нового пользователя [{}]", user);
        try {
            validateEmail(user);
            user.setId(++counterId);
            users.put(user.getId(), user);
            emails.add(user.getEmail());
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
        User existingUser = getById(user.getId());
        if (!existingUser.getEmail().equals(user.getEmail())) {
            if (emails.contains(user.getEmail())) {
                throw new IllegalArgumentException("Пользователь с таким email уже существует!");
            }
            emails.remove(existingUser.getEmail());
            emails.add(user.getEmail());
        }
        validateEmail(user);
        users.put(user.getId(), user);
        log.info("Пользователь с id [{}] успешно обновлён: [{}]", user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long userId) {
        User removedUser = users.remove(userId);
        if (removedUser != null) {
            emails.remove(removedUser.getEmail()); // удаляем email из Set при удалении пользователя
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