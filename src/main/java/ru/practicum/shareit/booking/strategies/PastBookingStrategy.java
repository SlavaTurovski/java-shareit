package ru.practicum.shareit.booking.strategies;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PastBookingStrategy extends BookingQueryStrategy {

    public PastBookingStrategy(BookingStorage bookingStorage) {
        super(bookingStorage);
    }

    @Override
    public State getState() {
        return State.PAST;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findByBookerIdAndStatusAndEndBefore(userId, Status.APPROVED, LocalDateTime.now());
    }

}