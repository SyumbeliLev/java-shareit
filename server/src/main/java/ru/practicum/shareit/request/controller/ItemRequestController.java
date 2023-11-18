package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.constraint.HEADER_USER_ID;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDtoOut create(@RequestHeader(HEADER_USER_ID) long userId, @RequestBody ItemRequestDto requestDto) {
        return service.create(userId, requestDto);
    }

    @GetMapping
    public List<ItemRequestDtoOut> findUserRequest(@RequestHeader(HEADER_USER_ID) long userId) {
        return service.findUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoOut> findAllRequests(@RequestHeader(HEADER_USER_ID) long userId,
                                                   @RequestParam(name = "from", defaultValue = "0") int from,
                                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("created")
                .descending());
        return service.findAllRequests(userId, pageable);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoOut findById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long requestId) {
        return service.findById(userId, requestId);
    }
}
