package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto addUser(UserDto userDto) {
        return UserMapper.toUserDto(repository.addUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        userDto.setId(userId);
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.updateUser(userId, user));
    }

    @Override
    public void removeUserById(Long userId) {
        repository.removeUserById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return UserMapper.toUserDto(repository.getUserById(userId));
    }

    @Override
    public List<UserDto> getUsersList() {
        return repository.getUsersList().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
