package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    private static final String USER_ID = "X-Sharer-User-id";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("my@email.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .owner(user)
            .build();

    @Test
    @SneakyThrows
    void createItemWhenItemIsValid() {
        long userId = 0L;
        ItemDto itemDtoToCreate = ItemDto.builder()
                .description("some item description")
                .name("some item name")
                .available(true)
                .build();

        when(itemService.create(itemDtoToCreate, userId)).thenReturn(ItemMapper.toDto(ItemMapper.toEntity(itemDtoToCreate)));

        String result = mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDtoToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ItemDto resultItemDto = objectMapper.readValue(result, ItemDto.class);
        assertEquals(itemDtoToCreate.getDescription(), resultItemDto.getDescription());
        assertEquals(itemDtoToCreate.getName(), resultItemDto.getName());
        assertEquals(itemDtoToCreate.getAvailable(), resultItemDto.getAvailable());
    }

    @Test
    @SneakyThrows
    void createItemWhenItemIsNotValidShouldReturnBadRequest() {
        long userId = 0L;
        ItemDto itemDtoToCreate = ItemDto.builder()
                .description(" ")
                .name(" ")
                .available(null)
                .build();

        when(itemService.create(itemDtoToCreate, userId)).thenReturn(ItemMapper.toDto(ItemMapper.toEntity(itemDtoToCreate)));

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDtoToCreate)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).create(itemDtoToCreate, userId);
    }

    @Test
    @SneakyThrows
    void updateWhenItemIsValidShouldReturnStatusIsOk() {
        long itemId = 0L;
        long userId = 0L;
        ItemDto itemDtoToCreate = ItemDto.builder()
                .description("some item description")
                .name("some item name")
                .available(true)
                .build();

        when(itemService.update(itemDtoToCreate, itemId, userId)).thenReturn(itemDtoToCreate);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDtoToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ItemDto resultItemDto = objectMapper.readValue(result, ItemDto.class);
        assertEquals(itemDtoToCreate.getDescription(), resultItemDto.getDescription());
        assertEquals(itemDtoToCreate.getName(), resultItemDto.getName());
        assertEquals(itemDtoToCreate.getAvailable(), resultItemDto.getAvailable());
    }

    @Test
    @SneakyThrows
    void getShouldReturnStatusOk() {
        long itemId = 0L;
        long userId = 0L;
        ItemDto itemDtoToCreate = ItemDto.builder()
                .id(itemId)
                .description("")
                .name("")
                .available(true)
                .build();

        when(itemService.findItemById(userId, itemId)).thenReturn(itemDtoToCreate);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header(USER_ID, userId)
                        .content(objectMapper.writeValueAsString(itemDtoToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtoToCreate), result);
    }

    @Test
    @SneakyThrows
    void getAllShouldReturnStatusOk() {
        long userId = 0L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(0, size);
        List<ItemDto> itemsDtoToExpect = List.of(ItemDto.builder()
                .name("some item name")
                .description("some item description")
                .available(true)
                .build());

        when(itemService.findAll(userId, pageable)).thenReturn(itemsDtoToExpect);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items", from, size)
                        .header(USER_ID, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemsDtoToExpect), result);
    }

    @Test
    @SneakyThrows
    void searchItemsShouldReturnStatusOk() {
        long userId = 0L;
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(0, size);
        String text = "find";
        List<ItemDto> itemsDtoToExpect = List.of(ItemDto.builder()
                .name("some item name")
                .description("some item description")
                .available(true)
                .build());

        when(itemService.search(text, pageable)).thenReturn(itemsDtoToExpect);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items/search", from, size)
                        .header(USER_ID, userId)
                        .param("text", text))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemsDtoToExpect), result);
    }

    @Test
    @SneakyThrows
    void createCommentWhenCommentIsValidShouldReturnStatusIsOk() {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .build();
        CommentDto commentToAdd = CommentDto.builder()
                .item(itemDto)
                .text("some comment")
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .item(itemDto)
                .text(commentToAdd.getText())
                .build();
        when(itemService.createComment(user.getId(), commentToAdd, item.getId())).thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .contentType("application/json")
                        .header(USER_ID, user.getId())
                        .content(objectMapper.writeValueAsString(commentToAdd)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }

    @Test
    @SneakyThrows
    void findAllItemsShouldReturnBadRequest() {
        int from = -1;
        int size = 10;
        Pageable pageable = PageRequest.of(0, size);
        mockMvc.perform(get("/items")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .contentType("application/json")
                        .header(USER_ID, user.getId()))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).findAll(user.getId(), pageable);
    }

    @Test
    @SneakyThrows
    void searchShouldReturnBadRequest() {
        int from = -1;
        int size = 10;
        String text = "item";
        Pageable pageable = PageRequest.of(0, size);
        mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .contentType("application/json")
                        .header(USER_ID, user.getId()))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).search(text, pageable);
    }
}
