package ru.practicum.shareit.booking.strategies;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FutureBookingStrategy extends BookingQueryStrategy {

    public FutureBookingStrategy(BookingStorage bookingStorage) {
        super(bookingStorage);
    }

    @Override
    public State getState() {
        return State.FUTURE;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findByBookerIdAndStartAfter(userId, LocalDateTime.now());

    }
}