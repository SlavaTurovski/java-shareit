package ru.practicum.shareit.user.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserMemoryStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long counterId = 0L;

    public void validateEmail(User newUser) {
        if (emails.contains(newUser.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }
    }

    @Override
    public User add(User user) {
        validateEmail(user);
        user.setId(++counterId);
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        User existingUser = users.get(user.getId());
        if (existingUser == null) {
            throw new NotFoundException("Пользователь не найден!");
        }
        if (!existingUser.getEmail().equals(user.getEmail())) {
            validateEmail(user);
            emails.remove(existingUser.getEmail());
            emails.add(user.getEmail());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
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