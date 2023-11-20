package ru.ithub.shareit.entity;

import ru.ithub.shareit.exception.ValidationException;

import java.util.Arrays;

public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static BookingState from(String outState) {
        return Arrays.stream(BookingState.values())
                .filter(bookingState -> bookingState.name()
                        .equals(outState))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Unknown state: UNSUPPORTED_STATUS"));
    }
}
