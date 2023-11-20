package ru.ithub.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.ithub.shareit.entity.BookingState;
import ru.ithub.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingStateTest {

    @Test
    void checkExistTest() {
        Assertions.assertEquals(BookingState.ALL, BookingState.from(String.valueOf(BookingState.ALL)));
        assertEquals(BookingState.CURRENT, BookingState.from(String.valueOf(BookingState.CURRENT)));
        assertEquals(BookingState.PAST, BookingState.from(String.valueOf(BookingState.PAST)));
        assertEquals(BookingState.FUTURE, BookingState.from(String.valueOf(BookingState.FUTURE)));
        assertEquals(BookingState.WAITING, BookingState.from(String.valueOf(BookingState.WAITING)));
        assertEquals(BookingState.REJECTED, BookingState.from(String.valueOf(BookingState.REJECTED)));
    }

    @Test
    void checkExistExceptionTest() {
        Exception exception = assertThrows(ValidationException.class, () -> BookingState.from("NotExist"));

        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }
}
