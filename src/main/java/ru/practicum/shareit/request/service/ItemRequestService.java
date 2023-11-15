package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut create(long userId, ItemRequestDto requestDto);

    List<ItemRequestDtoOut> findUserRequests(long userId);

    List<ItemRequestDtoOut> findAllRequests(long userId, Pageable pageable);

    ItemRequestDtoOut findById(long userId, long requestId);
}
