package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.strategies.BookingQueryStrategy;
import ru.practicum.shareit.booking.factory.BookingQueryStrategyFactory;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dal.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dal.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public BookingDto addBooking(RequestBookingDto requestBookingDto, Long userId) {
        User booker = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));
        Item item = itemStorage.findById(requestBookingDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет не найден: " + requestBookingDto.getItemId()));
        User owner = item.getOwner();
        if (owner.equals(booker)) {
            throw new ValidationException("Владелец не может бронировать свою вещь!");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Предмет не доступен к брони!");
        }
        if (bookingStorage.timeCrossingCheck(item.getId(), requestBookingDto.getStart(), requestBookingDto.getEnd())) {
            throw new ValidationException("Пересечение по времени с уже подтвержденным бронированием!");
        }
        Booking booking = BookingMapper.mapToBooking(requestBookingDto, item, booker);
        booking = bookingStorage.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено: " + bookingId));
        User owner = booking.getItem().getOwner();
        User booker = booking.getBooker();

        if (!booker.getId().equals(userId) && !owner.getId().equals(userId)) {
            throw new ValidationException("Просмотр бронирования доступен только владельцу предмета!");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Boolean approved, Long ownerId) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено: " + bookingId));
        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new ValidationException("Подтверждать бронирование может только владелец предмета!");
        }
        userStorage.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + ownerId));
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidationException("Бронирование не в режиме ожидания!");
        }
        if (approved != null && approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        bookingStorage.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, State state) {
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));
        BookingQueryStrategy strategy =
                BookingQueryStrategyFactory.create(state, bookingStorage);
        List<Booking> userBookings = strategy.getBookings(userId);
        return userBookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, State state) {
        userStorage.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + ownerId));
        if (itemStorage.findByOwnerId(ownerId).isEmpty()) {
            throw new NotFoundException("У данного пользователя нет предметов!");
        }
        BookingQueryStrategy strategy =
                BookingQueryStrategyFactory.create(state, bookingStorage);
        List<Booking> ownerBookings = strategy.getBookings(ownerId);
        return ownerBookings.stream()
                .map(BookingMapper::mapToBookingDto )
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookings() {
        List<Booking> bookings = bookingStorage.findAll();
        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

}