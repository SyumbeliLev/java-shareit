package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long userId);

    void delete(long userId);

    UserDto findUserById(long userId);

    List<UserDto> findAll();
}