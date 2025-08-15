package ru.practicum.shareit.booking.strategies;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public class PastBookingStrategy implements BookingQueryStrategy {
    private final BookingStorage bookingStorage;

    public PastBookingStrategy(BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findByBookerIdAndStatusAndEndBefore(userId, Status.APPROVED, LocalDateTime.now());
    }

}