package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item updateItem(Item item, Long userId);

    Item getItemById(Long itemId);

    List<Item> getItems();

    List<Item> getItemsSearch(String text);
}
