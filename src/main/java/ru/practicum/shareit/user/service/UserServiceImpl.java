package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto create(UserDto userDto) {
        try {
            return UserMapper.toDto(repository.save(UserMapper.toEntity(userDto)));
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new DuplicateEmailException(userDto.getEmail() + " такой email уже зарегистрирован!");
        }
    }

    @Override
    public UserDto update(UserDto userDto, Long userId) {
        User entity = getEntity(userId);
        UserMapper.update(entity, userDto);
        return UserMapper.toDto(repository.save(entity));
    }

    @Override
    public void delete(Long userId) {
        repository.deleteById(userId);
    }

    @Override
    public UserDto findUserById(Long userId) {
        return UserMapper.toDto(getEntity(userId));
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    private User getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь c id = " + id + " не найден!"
                ));
    }
}
