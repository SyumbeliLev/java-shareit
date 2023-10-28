package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;
@UtilityClass
public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest()
                        .getId() : null)
                .build();
    }

    public static ItemDto toDto(Item item, BookingDtoOut lastBooking, List<CommentDto> comments, BookingDtoOut nextBooking) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest()
                        .getId() : null)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }

    public static Item toEntity(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemDtoBooking toDtoBooking(Item item) {
        return new ItemDtoBooking(
                item.getId(),
                item.getName());
    }

    public static void update(ItemDto dto, Item entity) {
        String name = dto.getName();
        if (name != null && !name.isBlank()) {
            entity.setName(name);
        }
        String description = dto.getDescription();
        if (description != null && !description.isBlank()) {
            entity.setDescription(description);
        }
        if (dto.getAvailable() != null) {
            entity.setAvailable(dto.getAvailable());
        }
        Long requestId = dto.getRequestId();
        if (requestId != null) {
            entity.setRequest(null);
        }
    }
}
