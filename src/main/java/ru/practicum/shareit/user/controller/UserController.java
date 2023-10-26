package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.ValidationGroups;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto create(@Validated(ValidationGroups.Create.class) @RequestBody UserDto userDto) {
        log.info("Добавление пользователя: " + userDto);
        return service.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@Validated({ValidationGroups.Update.class}) @RequestBody UserDto userDto, @PathVariable Long id) {
        log.info("Обновление пользователя с id: {}", id);
        return service.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        log.info("Удаление пользователя с id: {}", id);
        service.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        log.info("Запрос пользователя с id: {}", id);
        return service.findUserById(id);
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Запрос всех пользователей ");
        return service.findAll();
    }
}
