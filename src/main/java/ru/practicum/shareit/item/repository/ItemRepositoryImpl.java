package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemNotExistException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> itemsMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item addItem(Item item) {
        itemsMap.put(++id, item.toBuilder().id(id).build());
        return itemsMap.get(id);
    }

    @Override
    public Item updateItem(Item item) {
        checkExistence(item.getId());
        itemsMap.put(item.getId(), item);
        return getItemById(item.getId());
    }

    @Override
    public Item getItemById(Long itemId) {
        checkExistence(itemId);
        return itemsMap.get(itemId);
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(itemsMap.values());
    }

    private void checkExistence(Long itemId) {
        if (!itemsMap.containsKey(itemId)) {
            throw new ItemNotExistException("Предмет с id = " + itemId + " не существует!");
        }
    }
}
