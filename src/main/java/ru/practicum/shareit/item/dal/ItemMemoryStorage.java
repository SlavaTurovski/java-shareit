package ru.practicum.shareit.item.dal;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ItemMemoryStorage implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private Long counterId = 0L;


    @Override
    public Item add(Long userId, Item item) {
        item.setId(++counterId);
        item.setOwner(userId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        return items.put(item.getId(), item);
    }

    @Override
    public Item getById(Long itemsId) {
        Optional<Item> item = Optional.ofNullable(items.get(itemsId));
        if (item.isEmpty()) {
            throw new NotFoundException("Item с таким id нет");
        }
        return item.get();
    }

    @Override
    public List<Item> getItemsByOwner(Long userId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        String lowerText = text.trim().toLowerCase();
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(lowerText)
                        || item.getDescription().toLowerCase().contains(lowerText))
                .collect(Collectors.toList());
    }

}