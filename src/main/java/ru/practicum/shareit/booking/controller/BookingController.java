package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.ValidationGroups;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;
    private static final String USER_ID = "X-Sharer-User-id";

    @PostMapping
    public BookingDtoOut create(@RequestHeader(USER_ID) long userId,
                                @Validated(ValidationGroups.Create.class) @RequestBody BookingDto bookingDto) {
        log.info("POST запрос на создание нового бронирования вещи от пользователя c id: {} ", userId);
        return service.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut update(@RequestHeader(USER_ID) long userId,
                                @PathVariable("bookingId")
                                long bookingId,
                                @RequestParam(name = "approved") Boolean approved) {
        log.info("PATCH запрос на обновление статуса бронирования вещи от владельца с id: {}", userId);
        return service.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut findDetailsBooking(@RequestHeader(USER_ID) long userId,
                                            @PathVariable("bookingId")
                                            long bookingId) {
        log.info("GET запрос на получение данных о  бронировании от пользователя с id: {}", userId);
        return service.findDetailsBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> findAll(@RequestHeader(USER_ID) long userId,
                                       @RequestParam(value = "state", defaultValue = "ALL") String bookingState) {
        log.info("GET запрос на получение списка всех бронирований текущего пользователя с id: {} и статусом {}", userId, bookingState);
        return service.findAll(userId, bookingState);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwner(@RequestHeader(USER_ID) long ownerId,
                                           @RequestParam(value = "state", defaultValue = "ALL") String bookingState) {
        log.info("GET запрос на получение списка всех бронирований текущего владельца с id: {} и статусом {}", ownerId, bookingState);
        return service.findAllOwner(ownerId, bookingState);
    }
}
