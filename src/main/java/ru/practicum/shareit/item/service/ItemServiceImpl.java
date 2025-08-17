package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BookingTimeException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.dal.CommentStorage;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.RequestCommentDto;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.dal.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.RequestItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;

    @Override
    public ItemDto add(Long userId, RequestItemDto requestItemDto) {
        User user = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id: " + userId));
        Item item = ItemMapper.mapToItem(requestItemDto);
        item.setOwner(user);
        item = itemStorage.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto update(Long itemsId, RequestItemDto requestItemDto, Long userId) {
        Item existingItem = itemStorage.findById(itemsId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден id: " + itemsId));

        if (!existingItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Вы не можете редактировать чужую вещь!");
        }

        ItemMapper.updateItemFromRequest(existingItem, requestItemDto);
        existingItem = itemStorage.save(existingItem);
        return ItemMapper.mapToItemDto(existingItem);
    }

    @Override
    public ItemDto getById(Long itemsId) {
        Item item = itemStorage.findById(itemsId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден id: " + itemsId));
        ItemDto itemDto = ItemMapper.mapToItemDto(item);
        List<Comment> comments = commentStorage.findByItemId(itemsId);
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .collect(Collectors.toList());
        itemDto.setComments(commentDtos);
        return itemDto;
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long userId) {
        List<Item> items = itemStorage.findByOwnerId(userId);
        if (items.isEmpty()) {
            return List.of();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        Map<Long, List<Comment>> commentsByItem = commentStorage.findAllByItemIdIn(itemIds).stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId()));

        List<Booking> bookings = bookingStorage.findAllByItemIdIn(itemIds);
        LocalDateTime now = LocalDateTime.now();

        List<Booking> approvedBookings = bookings.stream()
                .filter(b -> b.getStatus() == Status.APPROVED)
                .toList();

        Map<Long, BookingDto> lastBookings = new HashMap<>();
        Map<Long, BookingDto> nextBookings = new HashMap<>();

        for (Long itemId : itemIds) {
            List<Booking> itemBookings = approvedBookings.stream()
                    .filter(b -> b.getItem().getId().equals(itemId))
                    .toList();

            BookingDto lastBookingDto = null;
            LocalDateTime maxEndBeforeNow = null;

            for (Booking b : itemBookings) {
                if (b.getEnd() != null && b.getEnd().isBefore(now)) {
                    if (maxEndBeforeNow == null || b.getEnd().isAfter(maxEndBeforeNow)) {
                        maxEndBeforeNow = b.getEnd();
                        lastBookingDto = BookingMapper.mapToBookingDto(b);
                    }
                } else if ((b.getEnd() == null || !b.getEnd().isBefore(now)) && b.getStart() != null && b.getStart().isBefore(now)) {
                    if (maxEndBeforeNow == null || b.getStart().isAfter(maxEndBeforeNow)) {
                        maxEndBeforeNow = b.getStart();
                        lastBookingDto = BookingMapper.mapToBookingDto(b);
                    }
                }
            }
            lastBookings.put(itemId, lastBookingDto);

            BookingDto nextBookingDto = null;
            LocalDateTime minStartAfterNow = null;

            for (Booking b : itemBookings) {
                if (b.getStart() != null && b.getStart().isAfter(now)) {
                    if (minStartAfterNow == null || b.getStart().isBefore(minStartAfterNow)) {
                        minStartAfterNow = b.getStart();
                        nextBookingDto = BookingMapper.mapToBookingDto(b);
                    }
                }
            }
            nextBookings.put(itemId, nextBookingDto);
        }

        return items.stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.mapToItemDto(item);
                    List<Comment> itemComments = commentsByItem.getOrDefault(item.getId(), Collections.emptyList());
                    itemDto.setComments(
                            itemComments.stream()
                                    .map(CommentMapper::mapToCommentDto)
                                    .collect(Collectors.toList())
                    );
                    itemDto.setLastBooking(lastBookings.get(item.getId()));
                    itemDto.setNextBooking(nextBookings.get(item.getId()));
                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {

        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemStorage.searchByText(text).stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long itemId, RequestCommentDto requestCommentDto, Long userId) {
        User author = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден id: " + userId));

        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден id: " + itemId));

        boolean hasPastBooking = bookingStorage.existsPastBookingsByBookerIdAndItemId(userId, itemId, LocalDateTime.now());

        if (!hasPastBooking) {
            throw new BookingTimeException("Нет законченных бронирований!");
        }

        Comment comment = CommentMapper.mapToComment(requestCommentDto, item, author);
        comment = commentStorage.save(comment);
        return CommentMapper.mapToCommentDto(comment);
    }

}