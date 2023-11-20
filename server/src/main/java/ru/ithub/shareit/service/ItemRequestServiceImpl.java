package ru.ithub.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ithub.shareit.dto.ItemRequestDto;
import ru.ithub.shareit.dto.ItemRequestDtoOut;
import ru.ithub.shareit.entity.ItemRequest;
import ru.ithub.shareit.entity.User;
import ru.ithub.shareit.exception.NotFoundException;
import ru.ithub.shareit.mapper.RequestMapper;
import ru.ithub.shareit.mapper.UserMapper;
import ru.ithub.shareit.repository.ItemRequestRepository;

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
