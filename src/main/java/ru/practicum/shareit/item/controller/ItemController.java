package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@Valid @RequestBody RequestItemDto requestItemDto,
                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.trace("Добавление предмета (старт)");
        return itemService.add(userId, requestItemDto);
    }

    @PatchMapping("/{itemsId}")
    public ItemDto update(@PathVariable Long itemsId,
                          @RequestBody RequestItemDto requestItemDto,
                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.trace("Обновление предмета с id: {} (старт)", itemsId);
        return itemService.update(itemsId, requestItemDto, userId);
    }

    @GetMapping("/{itemsId}")
    public ItemDto getById(@PathVariable Long itemsId) {
        log.trace("Получение предмета по id: {} (старт)", itemsId);
        return itemService.getById(itemsId);
    }

    @GetMapping
    public Collection<ItemDto> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.trace("Получение предметов пользователя с id: {} (старт)", userId);
        return itemService.getItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        log.trace("Поиск предмета по введенному описанию: {} (старт)", text);
        return itemService.search(text);
    }

}