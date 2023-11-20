package ru.ithub.shareit.exception;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

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
        DataIntegrityViolationException exception = new DataIntegrityViolationException("Такой email, уже зарегистрирован!");
        ErrorResponse result = exceptionsHandler.handleConflictException(exception);
        assertEquals("Такой email, уже зарегистрирован!", result.getError());
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
