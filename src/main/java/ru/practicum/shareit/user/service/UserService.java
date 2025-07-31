package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto add(RequestUserDto requestUserDto);

    UserDto update(Long userId, RequestUserDto requestUserDto);

    void delete(Long userId);

    UserDto getById(Long userId);

}