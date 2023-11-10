package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionsHandlerTest {
    private final ExceptionsHandler exceptionsHandler = new ExceptionsHandler();

    @Test
    public void testHandleNotExistException() {
        NotFoundException exception = new NotFoundException("User not found");
        ErrorResponse result = exceptionsHandler.handleNotExistException(exception);
        assertEquals("User not found", result.getError());
    }

    @Test
    public void handleConflictExceptionTest() {
        DuplicateEmailException exception = new DuplicateEmailException("Conflict");
        ErrorResponse result = exceptionsHandler.handleNotExistException(exception);
        assertEquals("Conflict", result.getError());
    }

    @Test
    public void handleValidationExceptionTest() {
        ValidationException exception = new ValidationException("User not valid");
        ErrorResponse result = exceptionsHandler.handleNotExistException(exception);
        assertEquals("User not valid", result.getError());
    }

    @Test
    public void handleOtherExceptionTest() {
        ShareItException exception = new ShareItException("INTERNAL_SERVER_ERROR");
        ErrorResponse result = exceptionsHandler.handleNotExistException(exception);
        assertEquals("INTERNAL_SERVER_ERROR", result.getError());
    }
}
