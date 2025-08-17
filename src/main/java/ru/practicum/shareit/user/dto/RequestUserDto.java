package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestUserDto {

    private String name;

    @Email(message = "Ошибка при вводе email!")
    @NotBlank(message = "Email обязательное поле!")
    private String email;

}