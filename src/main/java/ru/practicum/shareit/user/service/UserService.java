package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    void removeUserById(Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getUsersList();
}