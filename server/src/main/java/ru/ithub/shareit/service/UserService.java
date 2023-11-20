package ru.ithub.shareit.service;

import ru.ithub.shareit.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, long userId);

    void delete(long userId);

    UserDto findUserById(long userId);

    List<UserDto> findAll();
}