package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, long userId, long itemId);

    ItemDto findItemById(long itemId, long userId);

    List<ItemDto> findAll(long userId);

    List<ItemDto> search(String text);

    CommentDto createComment(long userId, CommentDto commentDto, long itemId);
}
