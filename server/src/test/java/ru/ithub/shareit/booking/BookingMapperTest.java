package ru.ithub.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.ithub.shareit.dto.BookingDtoOut;
import ru.ithub.shareit.entity.Booking;
import ru.ithub.shareit.entity.BookingStatus;
import ru.ithub.shareit.mapper.BookingMapper;
import ru.ithub.shareit.entity.Item;
import ru.ithub.shareit.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {
    private final Booking booking = Booking.builder()
            .id(1L)
            .booker(User.builder()
                    .id(1L)
                    .name("name")
                    .email("email@email.com")
                    .build())
            .item(new Item())
            .startDate(LocalDateTime.now()
                    .plusMinutes(5))
            .endDate(LocalDateTime.now()
                    .plusMinutes(10))
            .status(BookingStatus.WAITING)
            .build();

    @Test
    void toBookingItemDto() {
        BookingDtoOut actualBookingItemDto = BookingMapper.toDto(booking);

        assertEquals(1L, actualBookingItemDto.getId());
        assertEquals(1L, actualBookingItemDto.getBookerId());
    }
}