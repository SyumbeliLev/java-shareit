package ru.ithub.shareit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ithub.shareit.client.ItemClient;
import ru.ithub.shareit.dto.CommentDto;
import ru.ithub.shareit.dto.ItemDto;
import ru.ithub.shareit.util.Constraint;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(Constraint.HEADER_USER_ID) Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Post creating item {}, userId={}", itemDto, userId);
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(Constraint.HEADER_USER_ID) Long userId, @RequestBody ItemDto itemDto, @PathVariable("itemId") Long itemId) {
        log.info("PATCH request to update item id: {} of user with id: {}", itemId, userId);
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(Constraint.HEADER_USER_ID) Long userId, @PathVariable Long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.get(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(Constraint.HEADER_USER_ID) Long userId, @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from, @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET request to get all items of user with id: {}", userId);
        return itemClient.getAll(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(Constraint.HEADER_USER_ID) Long userId, @RequestParam(name = "text") String text, @RequestParam(value = "from", defaultValue = "0") @Min(0) Integer from, @RequestParam(value = "size", defaultValue = "10") @Min(1) Integer size) {
        log.info("GET request to search for all items with text: {}", text);
        return itemClient.searchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(Constraint.HEADER_USER_ID) Long userId, @Validated @RequestBody CommentDto commentDto, @PathVariable Long itemId) {
        log.info("Post creating a comment for an item with id: {}, user with id:{}, text:{}", itemId, userId, commentDto.getText());
        return itemClient.createComment(userId, commentDto, itemId);
    }
}