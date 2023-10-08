package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавление пользователя: " + userDto);
        return service.addUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        log.info("Обновление пользователя с id: {}", id);
        return service.updateUser(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@Valid @PathVariable Long id) {
        log.info("Удаление пользователя с id: {}", id);
        service.removeUserById(id);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@Valid @PathVariable Long id) {
        log.info("Запрос пользователя с id: {}", id);
        return service.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        log.info("Запрос всех пользователей ");
        return service.getUsersList();
    }
}
