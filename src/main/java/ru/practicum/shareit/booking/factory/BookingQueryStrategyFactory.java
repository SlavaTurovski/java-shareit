package ru.practicum.shareit.booking.factory;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.strategies.BookingQueryStrategy;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;

@Service
public class BookingQueryStrategyFactory {

    private final List<BookingQueryStrategy> strategies;

    public BookingQueryStrategyFactory(List<BookingQueryStrategy> strategies) {
        this.strategies = strategies;
    }

    public BookingQueryStrategy getStrategyByState(State state) {
        return strategies.stream()
                .filter(s -> s.getState() == state)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Стратегия не найдена!" + state));
    }

}