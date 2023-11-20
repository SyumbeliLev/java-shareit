package ru.ithub.shareit.service;

import org.springframework.data.domain.Pageable;
import ru.ithub.shareit.dto.ItemRequestDto;
import ru.ithub.shareit.dto.ItemRequestDtoOut;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoOut create(long userId, ItemRequestDto requestDto);

    List<ItemRequestDtoOut> findUserRequests(long userId);

    List<ItemRequestDtoOut> findAllRequests(long userId, Pageable pageable);

    ItemRequestDtoOut findById(long userId, long requestId);
}
