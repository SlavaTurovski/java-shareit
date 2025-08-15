package ru.practicum.shareit.booking.strategies;

import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public class FutureBookingStrategy implements BookingQueryStrategy {
    private final BookingStorage bookingStorage;

    public FutureBookingStrategy(BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findByBookerIdAndStartAfter(userId, LocalDateTime.now());

    }
}