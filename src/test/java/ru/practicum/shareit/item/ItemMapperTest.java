package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    private Item item;
    private ItemDto itemDto;

    private final BookingDtoOut lastBooking = BookingDtoOut.builder()
            .id(2L)
            .status(BookingStatus.REJECTED)
            .build();
    private final BookingDtoOut nextBooking = BookingDtoOut.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .build();

    CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .text("test")
            .created(LocalDateTime.now())
            .build();
    List<CommentDto> comments = List.of(commentDto);

    @BeforeEach
    public void fillData() {
        item = Item.builder()
                .available(true)
                .id(2L)
                .name("name_item")
                .description("desc")
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .description("text")
                .available(true)
                .name("name")
                .build();
    }

    @Test
    void toDtoTest() {
        ItemDto actual = ItemMapper.toDto(item);
        assertEquals(actual.getId(), item.getId());
        assertEquals(actual.getName(), item.getName());
    }

    @Test
    void toDtoAllTest() {
        ItemDto actual = ItemMapper.toDto(item, lastBooking, comments, nextBooking);
        assertEquals(actual.getId(), item.getId());
        assertEquals(actual.getName(), item.getName());
        assertEquals(actual.getLastBooking(), lastBooking);
        assertEquals(actual.getNextBooking(), nextBooking);
        assertEquals(actual.getComments(), comments);
        assertEquals(actual.getComments()
                .size(), comments.size());
    }

    @Test
    void toEntityTest() {
        Item actual = ItemMapper.toEntity(itemDto);
        assertEquals(actual.getId(), itemDto.getId());
        assertEquals(actual.getName(), itemDto.getName());
    }

    @Test
    void toDtoBookingTest() {
        ItemDtoBooking actual = ItemMapper.toDtoBooking(item);
        assertEquals(actual.getId(), item.getId());
        assertEquals(actual.getName(), item.getName());
    }
}