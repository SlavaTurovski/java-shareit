package ru.practicum.shareit.booking.strategies;

import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public class DefaultBookingStrategy implements BookingQueryStrategy {
    private final BookingStorage bookingStorage;

    public DefaultBookingStrategy(BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findByBookerIdOrderByStartDesc(userId);
    }

}