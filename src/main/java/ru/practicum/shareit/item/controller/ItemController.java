package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
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
    public ItemDto postItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader(USER_ID) Long userId) {
        log.info("Добавление предмета: " + itemDto);
        return service.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@PathVariable Long itemId, @RequestBody ItemDto itemDto, @RequestHeader(USER_ID) Long userId) {
        log.info("Обновление предмета с id: {}", itemId);
        return service.updateItem(itemDto, userId, itemId);
    }

    @GetMapping()
    public List<ItemDto> getAllItemsOfUser(@RequestHeader(USER_ID) Long userId) {
        log.info("Запрос всех предметов пользователя с id: {}", userId);
        return service.getItemsOfUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        log.info("Запрос предмета с id: {}", itemId);
        return service.getItemById(itemId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchForItem(@NotNull @RequestParam(name = "text") String query) {
        log.info("Запрос предмета через поисковик : {}", query);
        return service.getItemsSearch(query);
    }
}
