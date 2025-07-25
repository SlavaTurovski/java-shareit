package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.RequestUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @PostMapping
    public UserDto add(@Valid @RequestBody RequestUserDto requestUserDto) {
        log.trace("Добавление пользователя (старт)");
        return userServiceImpl.add(requestUserDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                              @RequestBody RequestUserDto requestUserDto) {
        log.trace("Обновление пользователя с id: {} (старт)", userId);
        return userServiceImpl.update(userId, requestUserDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.trace("Удаление пользователя с id: {} (старт)", userId);
        userServiceImpl.delete(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Long userId) {
        log.trace("Поиск пользователя с id: {} (старт)", userId);
        return userServiceImpl.getById(userId);
    }

}