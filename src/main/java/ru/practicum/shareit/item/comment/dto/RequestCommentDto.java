package ru.practicum.shareit.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestCommentDto {

    @NotBlank(message = "Комментарий обязательное поле!")
    private String text;

}