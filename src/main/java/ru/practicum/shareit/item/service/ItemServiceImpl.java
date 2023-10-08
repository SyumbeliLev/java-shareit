package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validator.ItemValidator;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemValidator validator;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        validator.validationCheck(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(repository.addItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        validator.checkOwner(itemId, userId);
        Item item = repository.getItemById(itemId);

        Item itemUpdate = ItemMapper.toItem(itemDto);
        itemUpdate.setId(itemId);
        itemUpdate.setOwnerId(item.getOwnerId());

        if (itemUpdate.getName() == null) {
            itemUpdate.setName(item.getName());
        }
        if (itemUpdate.getDescription() == null) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (itemUpdate.getAvailable() == null) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        if (itemUpdate.getRequest() == null) {
            itemUpdate.setRequest(item.getRequest());
        }

        return ItemMapper.toItemDto(repository.updateItem(itemUpdate));
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.toItemDto(repository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getItemsOfUser(Long userId) {
        return repository.getItems().stream().filter(item -> item.getOwnerId().equals(userId)).map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsSearch(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return repository.getItems().stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toUpperCase().contains(text.toUpperCase()) ||
                                item.getDescription().toUpperCase().contains(text.toUpperCase()))).map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
