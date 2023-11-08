package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicateEmailException;
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
        try {
            User user = UserMapper.toEntity(userDto);
            repository.save(user);
            return UserMapper.toDto(user);
        } catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            throw new DuplicateEmailException(userDto.getEmail() + " такой email уже зарегистрирован!");
        }
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, long userId) {
        User entity = getEntity(userId);
        UserMapper.update(entity, userDto);
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
