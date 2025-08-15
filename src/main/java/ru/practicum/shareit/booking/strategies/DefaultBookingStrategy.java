package ru.practicum.shareit.booking.strategies;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collections;
import java.util.List;

@Service
public class DefaultBookingStrategy extends BookingQueryStrategy {

    public DefaultBookingStrategy(BookingStorage bookingStorage) {
        super(bookingStorage);
    }

    @Override
    public State getState() {
        return State.ALL;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findByBookerIdOrderByStartDesc(userId);
    }

}