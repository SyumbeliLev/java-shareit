package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId);

    ItemDto getItemById(Long itemId);

    List<ItemDto> getItemsOfUser(Long userId);

    List<ItemDto> getItemsSearch(String text);
}
