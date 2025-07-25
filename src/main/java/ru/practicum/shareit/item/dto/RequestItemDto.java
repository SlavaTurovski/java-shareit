package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestItemDto {

    @NotBlank(message = "Имя не может быть пустым!")
    private String name;

    @NotBlank(message = "Описание не может быть пустым!")
    private String description;

    @NotNull(message = "Статус не может быть пустым!")
    private Boolean available;

}