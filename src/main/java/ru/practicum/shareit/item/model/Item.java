package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {

    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;

}