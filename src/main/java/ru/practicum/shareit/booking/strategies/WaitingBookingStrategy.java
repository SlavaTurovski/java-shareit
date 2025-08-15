package ru.practicum.shareit.booking.strategies;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public class WaitingBookingStrategy implements BookingQueryStrategy {
    private final BookingStorage bookingStorage;

    public WaitingBookingStrategy(BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findByBookerIdAndStatus(userId, Status.WAITING);
    }

}