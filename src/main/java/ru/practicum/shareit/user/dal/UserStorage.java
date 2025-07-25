package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.model.User;

public interface UserStorage {

    User add(User user);

    User update(User user);

    void delete(Long userId);

    User getById(Long userId);

}