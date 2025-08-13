package ru.practicum.shareit.booking.controller;

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
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.HeaderConstants.HEADER_USER_ID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody RequestBookingDto requestBookingDto,
                                    @RequestHeader(HEADER_USER_ID) Long userId) {
        log.trace("Создать бронирование (старт)");
        return bookingService.addBooking(requestBookingDto, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Long bookingId, @RequestHeader(HEADER_USER_ID) Long userId) {
        log.trace("Получить бронирование по id: {} (старт)", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId, @RequestParam Boolean approved,
                                     @RequestHeader(HEADER_USER_ID) Long ownerId) {
        log.trace("Подтвердить бронирование id: {} (старт)", bookingId);
        return bookingService.approveBooking(bookingId, approved, ownerId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader(HEADER_USER_ID) Long userId,
                                            @RequestParam(defaultValue = "ALL") State state) {
        log.trace("Получение бронирований пользователя с id: {} (старт)", userId);
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                             @RequestParam(defaultValue = "ALL") State state) {
        log.trace("Получение бронирований владельца с id: {} (старт)", + ownerId);
        return bookingService.getOwnerBookings(ownerId, state);
    }

    @GetMapping("/all")
    public List<BookingDto> getAllBookings() {
        log.trace("Получение всех бронирований (старт)");
        return bookingService.getAllBookings();
    }

}