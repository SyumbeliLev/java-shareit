package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ValidationException;
import java.time.LocalDateTime;

public class BookingValidator {
    public static void  validateBookingData(BookItemRequestDto bookingDto) {
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("Booking: Dates are null!");
        }
        if (bookingDto.getEnd()
                .isBefore(bookingDto.getStart()) || bookingDto.getStart()
                .isEqual(bookingDto.getEnd())
                || bookingDto.getEnd()
                .isBefore(LocalDateTime.now()) || bookingDto.getStart()
                .isBefore(LocalDateTime.now())) {
            throw new ValidationException("Booking: Problem in dates");
        }
    }
}
