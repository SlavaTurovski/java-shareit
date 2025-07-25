package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item add(Long userId, Item newItem);

    Item update(Item updateItem);

    Item getById(Long itemsId);

    List<Item> getItemsByOwner(Long userId);

    List<Item> search(String text);

}