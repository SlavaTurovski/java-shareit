package ru.practicum.shareit.booking.strategies;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingQueryStrategy {

    List<Booking> getBookings(Long userId);

}