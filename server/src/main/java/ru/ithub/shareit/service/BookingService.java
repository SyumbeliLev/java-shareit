package ru.ithub.shareit.service;

import org.springframework.data.domain.Pageable;
import ru.ithub.shareit.dto.BookingDto;
import ru.ithub.shareit.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut create(long userId, BookingDto dto);

    BookingDtoOut update(long userId, long bookingId, Boolean approved);

    BookingDtoOut findDetailsBooking(long userId, long bookingId);

    List<BookingDtoOut> findAll(long userId, String state, Pageable pageable);

    List<BookingDtoOut> findAllOwner(long userId, String state, Pageable pageable);
}
