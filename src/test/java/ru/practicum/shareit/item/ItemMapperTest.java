package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    private Item item;
    private ItemDto itemDto;


    @BeforeEach
    public void fillData() {
        item = Item.builder()
                .available(true)
                .id(1L)
                .name("name")
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
    void toItemDto() {
        ItemDto actual = ItemMapper.toDto(item);
        assertEquals(actual.getId(), item.getId());
        assertEquals(actual.getName(), item.getName());
    }


    @Test
    void toItem() {

        Item actual = ItemMapper.toEntity(itemDto);

        assertEquals(actual.getId(), itemDto.getId());
        assertEquals(actual.getName(), itemDto.getName());
    }

    @Test
    void toItemDb() {
        Item actual = ItemMapper.toEntity(itemDto);
        assertEquals(actual.getName(), itemDto.getName());

    }

    @Test
    void toItemUpdate() {
        Item actual = ItemMapper.update(itemDto, item);
        assertEquals(actual.getDescription(), itemDto.getDescription());
    }
}