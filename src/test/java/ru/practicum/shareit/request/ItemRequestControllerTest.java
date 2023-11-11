package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    private static final String USER_ID = "X-Sharer-User-id";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService requestService;

    @MockBean
    private UserService userService;
    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final ItemRequestDtoOut requestDto = ItemRequestDtoOut.builder()
            .id(1L)
            .description("description")
            .created(LocalDateTime.now())
            .items(List.of())
            .build();

    @Test
    @SneakyThrows
    void createRequest() {
        when(requestService.create(anyLong(), any())).thenReturn(requestDto);
        when(userService.findUserById(anyLong())).thenReturn(UserDto.builder()
                .build());

        assert user != null;
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/requests")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header(USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }

    @Test
    @SneakyThrows
    void getUserRequests() {
        when(requestService.findUserRequests(user.getId())).thenReturn(List.of(requestDto));
        when(userService.findUserById(anyLong())).thenReturn(UserDto.builder()
                .build());
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header(USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @Test
    @SneakyThrows
    void getAllRequests() {
        int from = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(from, size, Sort.by("created")
                .descending());
        when(requestService.findAllRequests(user.getId(), pageable)).thenReturn(List.of(requestDto));
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header(USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(requestDto)), result);
    }

    @Test
    @SneakyThrows
    void get() {
        long requestId = 1L;
        when(requestService.findById(user.getId(), requestId)).thenReturn(requestDto);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType("application/json")
                        .header(USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(requestDto), result);
    }
}