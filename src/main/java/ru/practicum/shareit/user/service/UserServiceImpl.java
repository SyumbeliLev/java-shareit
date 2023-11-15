package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        repository.save(user);
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, long userId) {
        User entity = getEntity(userId);

        String name = userDto.getName();
        if (name != null && !name.isBlank()) {
            entity.setName(name);
        }
        String email = userDto.getEmail();
        if (email != null && !email.isBlank()) {
            entity.setEmail(email);
        }

        repository.save(entity);
        return UserMapper.toDto(entity);
    }

    @Override
    @Transactional
    public void delete(long userId) {
        repository.deleteById(userId);
    }

    @Override
    public UserDto findUserById(long userId) {
        return UserMapper.toDto(getEntity(userId));
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    private User getEntity(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь c id = " + id + " не найден!"
                ));
    }
}
