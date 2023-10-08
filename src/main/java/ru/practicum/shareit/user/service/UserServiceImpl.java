package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserValidator validator;

    @Override
    public UserDto addUser(UserDto userDto) {
        validator.checkValidation(userDto);
        return UserMapper.toUserDto(repository.addUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        User oldUser = repository.getUserById(userId);

        if (userDto.getId() == null) {
            userDto.setId(oldUser.getId());
        }
        if (userDto.getName() == null) {
            userDto.setName(oldUser.getName());
        }
        if (userDto.getEmail() == null) {
            userDto.setEmail(oldUser.getEmail());
        } else if (!userDto.getEmail().equals(oldUser.getEmail())) {
            validator.checkValidation(userDto);
        }
        User updatedUser = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(repository.updateUser(userId, updatedUser));
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
