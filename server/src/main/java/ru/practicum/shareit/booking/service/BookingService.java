package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut create(long userId, BookingDto dto);

    BookingDtoOut update(long userId, long bookingId, Boolean approved);

    BookingDtoOut findDetailsBooking(long userId, long bookingId);

    List<BookingDtoOut> findAll(long userId, String state, Pageable pageable);

    List<BookingDtoOut> findAllOwner(long userId, String state, Pageable pageable);
}
