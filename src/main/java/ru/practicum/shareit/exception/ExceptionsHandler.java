package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotExistException(final ShareItException e) {
        log.warn("Получен статус 404 NOT_FOUND {}", e.getMessage(), e);
        return new ErrorResponse(
                String.format(e.getMessage())
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ShareItException e) {
        log.warn("Получен статус 409 CONFLICT {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final MethodArgumentNotValidException e) {
        String error = e.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();
        log.warn("Получен статус 400 BAD_REQUEST {}", error, e);
        return new ErrorResponse(
                String.format("Ошибка валидации: %s", error)
        );
    }

    @ExceptionHandler({ValidationException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception e) {
        log.warn("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return new ErrorResponse(
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherException(final Throwable e) {
        log.warn("Получен статус 500 SERVER_ERROR {}", e.getMessage(), e);
        return new ErrorResponse(
                e.getMessage()
        );
    }
}
