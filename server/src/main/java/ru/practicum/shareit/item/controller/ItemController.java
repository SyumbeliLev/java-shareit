package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

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
    public ItemDto create(@RequestBody ItemDto itemDto, @RequestHeader(USER_ID) long userId) {
        log.info("POST запрос на добавление предмета: " + itemDto);
        return service.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId, @RequestBody ItemDto itemDto, @RequestHeader(USER_ID) long userId) {
        log.info("PATCH запрос на обновление предмета с id: {}", itemId);
        return service.update(itemDto, userId, itemId);
    }

    @GetMapping()
    public List<ItemDto> findAllByUserId(@RequestHeader(USER_ID) long userId,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        log.info("Get запрос на получение всех предметов пользователя с id: {}", userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return service.findAll(userId, pageable);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@RequestHeader(USER_ID) long userId, @PathVariable("itemId") long itemId) {
        log.info("Get запрос на получение предмета с id = {} пользователем с id = {} ", itemId, userId);
        return service.findItemById(itemId, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchForItem(@RequestParam(name = "text") String query,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        log.info("Get запрос на поиск предмета через поисковик : {}", query);
        Pageable pageable = PageRequest.of(from / size, size);
        return service.search(query, pageable);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(USER_ID) long userId, @RequestBody CommentDto commentDto, @PathVariable long itemId) {
        log.info("POST Запрос на создание комментария id = {}", itemId);
        return service.createComment(userId, commentDto, itemId);
    }
}
