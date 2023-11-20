package ru.ithub.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.ithub.shareit.dto.ItemDto;
import ru.ithub.shareit.dto.ItemRequestDto;
import ru.ithub.shareit.dto.ItemRequestDtoOut;
import ru.ithub.shareit.entity.ItemRequest;
import ru.ithub.shareit.entity.User;

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
