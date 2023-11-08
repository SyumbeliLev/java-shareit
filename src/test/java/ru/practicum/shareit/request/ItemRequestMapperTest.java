package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestMapperTest {

    @Test
    void toEntity() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("text")
                .build();
        User requester = User.builder()
                .id(1L)
                .build();

        ItemRequest actual = RequestMapper.toEntity(requester, itemRequestDto);

        assertEquals(itemRequestDto.getDescription(), actual.getDescription());
        assertEquals(actual.getRequester()
                .getId(), requester.getId());
    }

    @Test
    void toDtoOut() {
        User owner = User.builder()
                .id(1L)
                .build();
        ItemRequest requestToItem = ItemRequest.builder()
                .created(LocalDateTime.now())
                .id(2L)
                .description("desc")
                .build();
        Item item = Item.builder()
                .name("name")
                .owner(owner)
                .request(requestToItem)
                .description("desc")
                .available(true)
                .build();
        ItemRequest request = ItemRequest.builder()
                .created(LocalDateTime.now())
                .requester(owner)
                .id(1L)
                .description("desc")
                .items(List.of(item))
                .build();

        ItemRequestDtoOut actual = RequestMapper.toDtoOut(request);

        assertEquals(actual.getDescription(), request.getDescription());
        assertEquals(actual.getItems()
                .size(), 1);
        assertEquals(actual.getCreated(), request.getCreated());
        assertEquals(actual.getId(), request.getId());
    }


    @Test
    void toDto() {
        User owner = User.builder()
                .id(1L)
                .build();
        ItemRequest requestToItem = ItemRequest.builder()
                .created(LocalDateTime.now())
                .id(2L)
                .description("desc")
                .build();
        Item item = Item.builder()
                .name("name")
                .owner(owner)
                .request(requestToItem)
                .description("desc")
                .available(true)
                .build();
        ItemRequest request = ItemRequest.builder()
                .created(LocalDateTime.now())
                .requester(owner)
                .id(1L)
                .description("desc")
                .items(List.of(item))
                .build();

        ItemRequestDto actual = RequestMapper.toDto(request);
        assertEquals(actual.getDescription(), request.getDescription());
    }
}