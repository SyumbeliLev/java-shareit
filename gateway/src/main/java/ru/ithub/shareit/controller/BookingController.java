package ru.ithub.shareit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ithub.shareit.client.BookingClient;
import ru.ithub.shareit.dto.BookItemRequestDto;
import ru.ithub.shareit.dto.BookingState;
import ru.ithub.shareit.util.BookingValidator;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.ithub.shareit.util.Constraint.HEADER_USER_ID;

@Slf4j
@Validated
@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HEADER_USER_ID) long userId,
                                         @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        BookingValidator.validateBookingData(requestDto);
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @PathVariable("bookingId") Long bookingId,
                                         @RequestParam("approved") Boolean approved) {
        log.info("PATCH request to update the reservation status of an item: {} from the owner with id: {}", bookingId, userId);
        return bookingClient.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                              @RequestParam(name = "state", defaultValue = "all") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }


    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwner(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                              @RequestParam(value = "state", defaultValue = "ALL") String bookingState,
                                              @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        BookingState state = BookingState.from(bookingState)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookingState));
        log.info("GET list of all reservations with state {}, userId={}, from={}, size={}", bookingState, ownerId, from, size);
        return bookingClient.getAllOwner(ownerId, state, from, size);
    }


}
