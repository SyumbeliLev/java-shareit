package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut create(Long userId, BookingDto dto);

    BookingDtoOut update(Long userId, Long bookingId, Boolean approved);

    BookingDtoOut findDetailsBooking(Long userId, Long bookingId);

    List<BookingDtoOut> findAll(Long userId, String state);

    List<BookingDtoOut> findAllOwner(Long userId, String state);
}
