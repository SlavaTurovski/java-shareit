package ru.practicum.shareit.booking.strategies;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Service
public class WaitingBookingStrategy extends BookingQueryStrategy {

    public WaitingBookingStrategy(BookingStorage bookingStorage) {
        super(bookingStorage);
    }

    @Override
    public State getState() {
        return State.WAITING;
    }

    @Override
    public List<Booking> getBookings(Long userId) {
        return bookingStorage.findByBookerIdAndStatus(userId, Status.WAITING);
    }

}