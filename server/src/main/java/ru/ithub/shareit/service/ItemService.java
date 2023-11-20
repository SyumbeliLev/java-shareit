package ru.ithub.shareit.service;

import org.springframework.data.domain.Pageable;
import ru.ithub.shareit.dto.CommentDto;
import ru.ithub.shareit.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, long userId);

    ItemDto update(ItemDto itemDto, long userId, long itemId);

    ItemDto findItemById(long itemId, long userId);

    List<ItemDto> findAll(long userId, Pageable pageable);

    List<ItemDto> search(String text, Pageable pageable);

    CommentDto createComment(long userId, CommentDto commentDto, long itemId);
}
