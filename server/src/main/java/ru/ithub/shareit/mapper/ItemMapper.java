package ru.ithub.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.ithub.shareit.dto.BookingDtoOut;
import ru.ithub.shareit.dto.CommentDto;
import ru.ithub.shareit.dto.ItemDto;
import ru.ithub.shareit.dto.ItemDtoBooking;
import ru.ithub.shareit.entity.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest()
                        .getId() : null)
                .build();
    }

    public ItemDto toDto(Item item, BookingDtoOut lastBooking, List<CommentDto> comments, BookingDtoOut nextBooking) {
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

    public Item toEntity(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public ItemDtoBooking toDtoBooking(Item item) {
        return new ItemDtoBooking(
                item.getId(),
                item.getName());
    }
}
