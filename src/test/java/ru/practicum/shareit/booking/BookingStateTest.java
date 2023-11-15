package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.exception.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingStateTest {

    @Test
    void checkExistTest() {
        assertEquals(BookingState.ALL, BookingState.checkExist(String.valueOf(BookingState.ALL)));
        assertEquals(BookingState.CURRENT, BookingState.checkExist(String.valueOf(BookingState.CURRENT)));
        assertEquals(BookingState.PAST, BookingState.checkExist(String.valueOf(BookingState.PAST)));
        assertEquals(BookingState.FUTURE, BookingState.checkExist(String.valueOf(BookingState.FUTURE)));
        assertEquals(BookingState.WAITING, BookingState.checkExist(String.valueOf(BookingState.WAITING)));
        assertEquals(BookingState.REJECTED, BookingState.checkExist(String.valueOf(BookingState.REJECTED)));
    }

    @Test
    void checkExistExceptionTest() {
        Exception exception = assertThrows(ValidationException.class, () -> BookingState.checkExist("NotExist"));

        assertEquals("Unknown state: UNSUPPORTED_STATUS", exception.getMessage());
    }
}
