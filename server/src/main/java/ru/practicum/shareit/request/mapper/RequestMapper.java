package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public ItemRequestDtoOut toDtoOut(ItemRequest itemRequest) {
        List<ItemDto> itemsDto = new ArrayList<>();
        if (itemRequest.getItems() != null) {
            itemsDto = itemRequest.getItems()
                    .stream()
                    .map(ItemMapper::toDto)
                    .collect(Collectors.toList());
        }
        return ItemRequestDtoOut.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .items(itemsDto)
                .created(itemRequest.getCreated())
                .build();
    }

    public ItemRequestDto toDto(ItemRequest request) {

        return ItemRequestDto.builder()
                .description(request.getDescription())
                .build();
    }

    public ItemRequest toEntity(User user, ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .requester(user)
                .build();
    }
}
