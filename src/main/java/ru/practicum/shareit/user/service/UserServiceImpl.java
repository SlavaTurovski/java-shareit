package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dal.UserStorage;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto add(RequestUserDto requestUserDto) {
        User user = UserMapper.mapToUser(requestUserDto);
        user = userStorage.add(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(Long userId, RequestUserDto requestUserDto) {
        User existingUser = userStorage.getById(userId);
        UserMapper.updateUserFromRequest(existingUser, requestUserDto);
        existingUser = userStorage.update(existingUser);
        return UserMapper.mapToUserDto(existingUser);
    }

    @Override
    public void delete(Long userId) {
        userStorage.getById(userId);
        userStorage.delete(userId);
    }

    @Override
    public UserDto getById(Long userId) {
        User user = userStorage.getById(userId);
        return UserMapper.mapToUserDto(user);
    }

}