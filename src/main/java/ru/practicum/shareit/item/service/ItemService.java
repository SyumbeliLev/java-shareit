package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long userId);

    ItemDto update(ItemDto itemDto, Long userId, Long itemId);

    ItemDto findItemById(Long itemId, Long userId);

    List<ItemDto> findAll(Long userId);

    List<ItemDto> search(String text);

    CommentDto createComment(Long userId, CommentDto commentDto, Long itemId);
}
