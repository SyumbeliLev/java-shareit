package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.ValidationGroups;

import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService service;
    private static final String USER_ID = "X-Sharer-User-id";

    @PostMapping
    public BookingDtoOut create(@RequestHeader(USER_ID) long userId, @Validated(ValidationGroups.Create.class) @RequestBody BookingDto bookingDto) {
        log.info("POST запрос на создание нового бронирования вещи от пользователя c id: {} ", userId);
        return service.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut update(@RequestHeader(USER_ID) long userId, @PathVariable("bookingId") long bookingId, @RequestParam(name = "approved") Boolean approved) {
        log.info("PATCH запрос на обновление статуса бронирования вещи от владельца с id: {}", userId);
        return service.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut findDetailsBooking(@RequestHeader(USER_ID) long userId, @PathVariable("bookingId") long bookingId) {
        log.info("GET запрос на получение данных о  бронировании от пользователя с id: {}", userId);
        return service.findDetailsBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> findAll(@RequestHeader(USER_ID) long userId,
                                       @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                       @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                       @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("GET запрос на получение списка всех бронирований текущего пользователя с id: {} и статусом {}", userId, bookingState);
        Pageable pageable = PageRequest.of(from / size, size);
        return service.findAll(userId, bookingState, pageable);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllOwner(@RequestHeader(USER_ID) long ownerId, @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                           @RequestParam(value = "from", defaultValue = "0") @Min(0) int from,
                                           @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        log.info("GET запрос на получение списка всех бронирований текущего владельца с id: {} и статусом {}", ownerId, bookingState);
        Pageable pageable = PageRequest.of(from / size, size);
        return service.findAllOwner(ownerId, bookingState, pageable);
    }
}
