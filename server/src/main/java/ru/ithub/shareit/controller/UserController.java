package ru.ithub.shareit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.ithub.shareit.dto.UserDto;
import ru.ithub.shareit.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        log.info("Добавление пользователя: " + userDto);
        return service.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto userDto, @PathVariable long id) {
        log.info("Обновление пользователя с id: {}", id);
        return service.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable long id) {
        log.info("Удаление пользователя с id: {}", id);
        service.delete(id);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable long id) {
        log.info("Запрос пользователя с id: {}", id);
        return service.findUserById(id);
    }

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Запрос всех пользователей ");
        return service.findAll();
    }
}
