package ru.practicum.shareit.booking.strategies;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public abstract class BookingQueryStrategy {

    protected final BookingStorage bookingStorage;

    protected BookingQueryStrategy(BookingStorage bookingStorage) {
        this.bookingStorage = bookingStorage;
    }

    public abstract State getState();

    public abstract List<Booking> getBookings(Long ownerId);

}