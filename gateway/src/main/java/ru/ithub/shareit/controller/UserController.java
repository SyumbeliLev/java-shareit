package ru.ithub.shareit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ithub.shareit.client.UserClient;
import ru.ithub.shareit.dto.UserDto;
import ru.ithub.shareit.util.ValidationGroups;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> add(@Validated({ValidationGroups.Create.class}) @RequestBody UserDto user) {
        log.info("Creating user {}", user);
        return userClient.add(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Validated({ValidationGroups.Update.class}) @RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("PATCH request to update user: {} userId: {}", userDto, userId);
        return userClient.update(userId, userDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("GET all users.");
        return userClient.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable Long userId) {
        log.info("GET user by id: {}", userId);
        return userClient.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        log.info("DELETE user by id: {}", userId);
        return userClient.deleteById(userId);
    }
}