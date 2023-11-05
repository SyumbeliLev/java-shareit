package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.ValidationGroups;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    private static final String USER_ID = "X-Sharer-User-id";

    @PostMapping()
    public ItemDto create(@RequestBody @Validated(ValidationGroups.Create.class) ItemDto itemDto, @RequestHeader(USER_ID) long userId) {
        log.info("POST запрос на добавление предмета: " + itemDto);
        return service.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId, @RequestBody ItemDto itemDto, @RequestHeader(USER_ID) long userId) {
        log.info("PATCH запрос на обновление предмета с id: {}", itemId);
        return service.update(itemDto, userId, itemId);
    }

    @GetMapping()
    public List<ItemDto> findAllByUserId(@RequestHeader(USER_ID) long userId) {
        log.info("Get запрос на получение всех предметов пользователя с id: {}", userId);
        return service.findAll(userId);
    }


    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId) {
        log.info("Get запрос на получение предмета с id = {} пользователем с id = {} ", itemId, userId);
        return service.findItemById(itemId, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchForItem(@NotNull @RequestParam(name = "text") String query) {
        log.info("Get запрос на поиск предмета через поисковик : {}", query);
        return service.search(query);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID) long userId, @Validated @RequestBody CommentDto commentDto, @PathVariable long itemId) {
        log.info("POST Запрос на создание комментария id = {}", itemId);
        return service.createComment(userId, commentDto, itemId);
    }
}
