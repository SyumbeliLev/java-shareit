package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemNotExistException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validator.ItemValidator;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
class ItemRepositoryImpl implements ItemRepository {
    private final ItemValidator validator;
    private final Map<Long, Item> itemsMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item addItem(Item item) {
        validator.validationCheck(item.getOwnerId());
        itemsMap.put(++id, item.toBuilder().id(id).build());
        return itemsMap.get(id);
    }

    @Override
    public Item updateItem(Item item, Long userId) {
        Item oldItem = checkExistence(item.getId());
        item.setOwnerId(oldItem.getOwnerId());
        validator.checkOwner(item, userId);
        if (item.getName() == null) {
            item.setName(oldItem.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(oldItem.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(oldItem.getAvailable());
        }
        if (item.getRequest() == null) {
            item.setRequest(oldItem.getRequest());
        }

        itemsMap.put(item.getId(), item);
        return getItemById(item.getId());
    }

    @Override
    public Item getItemById(Long itemId) {
        return checkExistence(itemId);
    }

    @Override
    public List<Item> getItems() {
        return new ArrayList<>(itemsMap.values());
    }

    @Override
    public List<Item> getItemsSearch(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemsMap.values().stream().filter(item -> item.getAvailable() &&
                        (item.getName().toUpperCase().contains(text.toUpperCase()) ||
                                item.getDescription().toUpperCase().contains(text.toUpperCase())))
                .collect(Collectors.toList());
    }

    private Item checkExistence(Long itemId) {
        Optional<Item> item = Optional.ofNullable(itemsMap.get(itemId));
        return item.orElseThrow(() -> new ItemNotExistException("Предмет с id = " + itemId + " не существует!"));
    }
}
