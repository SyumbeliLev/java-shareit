package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserService userService;

    @Override
    @Transactional
    public ItemRequestDtoOut create(long userId, ItemRequestDto requestDto) {
        User user = UserMapper.toEntity(userService.findUserById(userId));
        ItemRequest itemRequest = RequestMapper.toEntity(user, requestDto);
        return RequestMapper.toDtoOut(repository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDtoOut> findUserRequests(long requestorId) {
        userService.findUserById(requestorId);
        return repository.findAllByRequesterId(requestorId)
                .stream()
                .map(RequestMapper::toDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDtoOut> findAllRequests(long userId, Pageable pageable) {
        userService.findUserById(userId);
        return repository.findAllByOtherUsers(userId, pageable)
                .stream()
                .map(RequestMapper::toDtoOut)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDtoOut findById(long userId, long requestId) {
        userService.findUserById(userId);

        return RequestMapper.toDtoOut(repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Запрос с id: %s " +
                        "не был найден.", requestId))));
    }
}
