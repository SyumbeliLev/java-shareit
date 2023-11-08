package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionsHandlerTest {
    private final ExceptionsHandler exceptionsHandler = new ExceptionsHandler();

    @Test
    public void testHandleNotExistException() {
        ShareItException exception = new ShareItException("User not found");
        ErrorResponse result = exceptionsHandler.handleNotExistException(exception);
        assertEquals("User not found", result.getError());
    }

    @Test
    public void testHandleConflictException() {
        ShareItException exception = new ShareItException("Conflict");
        ErrorResponse result = exceptionsHandler.handleNotExistException(exception);
        assertEquals("Conflict", result.getError());
    }

    @Test
    public void testHandleValidationException() {
        ShareItException exception = new ShareItException("User not valid");
        ErrorResponse result = exceptionsHandler.handleNotExistException(exception);
        assertEquals("User not valid", result.getError());
    }

    @Test
    public void testHandleOtherException() {
        ShareItException exception = new ShareItException("INTERNAL_SERVER_ERROR");
        ErrorResponse result = exceptionsHandler.handleNotExistException(exception);
        assertEquals("INTERNAL_SERVER_ERROR", result.getError());
    }
}
