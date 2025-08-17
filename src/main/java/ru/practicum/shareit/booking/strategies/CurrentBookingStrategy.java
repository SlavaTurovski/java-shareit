package ru.practicum.shareit.booking.strategies;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrentBookingStrategy extends BookingQueryStrategy {

    public CurrentBookingStrategy(BookingStorage bookingStorage) {
        super(bookingStorage);
    }

    @Override
    public State getState() {
        return State.CURRENT;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findCurrentByBookerId(userId, LocalDateTime.now());
    }

}