package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        return ItemMapper.toItemDto(repository.addItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long userId, Long itemId) {
        Item itemUpdate = ItemMapper.toItem(itemDto);
        itemUpdate.setId(itemId);
        return ItemMapper.toItemDto(repository.updateItem(itemUpdate, userId));
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
        return repository.getItemsSearch(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
