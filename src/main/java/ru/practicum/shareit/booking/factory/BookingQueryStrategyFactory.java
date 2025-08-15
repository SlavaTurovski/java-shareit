package ru.practicum.shareit.booking.factory;

import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dal.BookingStorage;
import ru.practicum.shareit.booking.strategies.BookingQueryStrategy;
import ru.practicum.shareit.booking.strategies.CurrentBookingStrategy;
import ru.practicum.shareit.booking.strategies.DefaultBookingStrategy;
import ru.practicum.shareit.booking.strategies.FutureBookingStrategy;
import ru.practicum.shareit.booking.strategies.PastBookingStrategy;
import ru.practicum.shareit.booking.strategies.RejectedBookingStrategy;
import ru.practicum.shareit.booking.strategies.WaitingBookingStrategy;

public class BookingQueryStrategyFactory {

    public static BookingQueryStrategy create(State state, BookingStorage storage) {
        return switch (state) {
            case CURRENT -> new CurrentBookingStrategy(storage);
            case WAITING -> new WaitingBookingStrategy(storage);
            case PAST -> new PastBookingStrategy(storage);
            case REJECTED -> new RejectedBookingStrategy(storage);
            case FUTURE -> new FutureBookingStrategy(storage);
            default -> new DefaultBookingStrategy(storage);
        };
    }

}