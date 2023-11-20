package ru.ithub.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import ru.ithub.shareit.controller.BookingController;
import ru.ithub.shareit.dto.BookingDto;
import ru.ithub.shareit.dto.BookingDtoOut;
import ru.ithub.shareit.entity.BookingState;
import ru.ithub.shareit.entity.BookingStatus;
import ru.ithub.shareit.service.BookingService;
import ru.ithub.shareit.entity.Item;
import ru.ithub.shareit.mapper.ItemMapper;
import ru.ithub.shareit.entity.User;
import ru.ithub.shareit.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    private static final String USER_ID = "X-Sharer-User-id";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .owner(user)
            .build();

    private final BookingDto bookingDto = BookingDto.builder()
            .itemId(1L)
            .start(LocalDateTime.now()
                    .plusDays(1L))
            .end(LocalDateTime.now()
                    .plusDays(2L))
            .build();

    private final BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
            .id(1L)
            .start(LocalDateTime.now()
                    .plusDays(1L))
            .end(LocalDateTime.now()
                    .plusDays(2L))
            .status(BookingStatus.WAITING)
            .booker(UserMapper.toDto(user))
            .item(ItemMapper.toDtoBooking(item))
            .build();

    @Test
    @SneakyThrows
    void createBookingWhenBookingIsValid() {
        when(bookingService.create(user.getId(), bookingDto)).thenReturn(bookingDtoOut);

        String result = mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header(USER_ID, user.getId())
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }


    @Test
    @SneakyThrows
    void updateWhenBookingIsValid() {
        Boolean approved = true;
        long bookingId = 1L;

        when(bookingService.update(user.getId(), bookingId, approved)).thenReturn(bookingDtoOut);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .header(USER_ID, user.getId())
                        .param("approved", String.valueOf(approved)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }

    @Test
    @SneakyThrows
    void getByIdWhenBookingIsValid() {
        long bookingId = 1L;

        when(bookingService.findDetailsBooking(user.getId(), bookingId)).thenReturn(bookingDtoOut);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDtoOut), result);
    }

    @Test
    @SneakyThrows
    void getAllShouldReturnStatusIsOk() {
        int from = 0;
        int size = 10;
        String state = "ALL";

        when(bookingService.findAll(user.getId(), BookingState.ALL.toString(), PageRequest.of(from, size)))
                .thenReturn(List.of(bookingDtoOut));

        String result = mockMvc.perform(get("/bookings")
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .header(USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDtoOut)), result);
    }

    @Test
    @SneakyThrows
    void getAllByOwner() {
        int from = 0;
        int size = 10;
        String state = "ALL";

        when(bookingService.findAllOwner(user.getId(), BookingState.ALL.toString(), PageRequest.of(from, size)))
                .thenReturn(List.of(bookingDtoOut));

        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .header(USER_ID, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(bookingDtoOut)), result);
    }
}