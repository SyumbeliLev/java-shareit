package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;
    private static final String USER_ID = "X-Sharer-User-id";

    @PostMapping
    public ItemRequestDtoOut create(@RequestHeader(USER_ID) long userId, @Validated @RequestBody ItemRequestDto requestDto) {
        return service.create(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDtoOut> findUserRequest(@RequestHeader(USER_ID) long userId) {
        return service.findUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> findAllRequests(@RequestHeader(USER_ID) long userId,
                                                   @RequestParam(name = "from", defaultValue = "0") @Min(0) int from,
                                                   @RequestParam(value = "size", defaultValue = "10") @Min(1) int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("created")
                .descending());
        return service.findAllRequests(userId, pageable);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut findById(@RequestHeader(USER_ID) long userId, @PathVariable long requestId) {
        return service.findById(userId, requestId);
    }
}
