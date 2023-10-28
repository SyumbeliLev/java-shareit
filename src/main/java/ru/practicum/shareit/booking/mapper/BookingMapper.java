package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;

@UtilityClass
public class BookingMapper {
    public static Booking toBooking(User booker, Item item, BookingDto bookingDto) {
        return Booking.builder()
                .item(item)
                .startDate(bookingDto.getStart())
                .endDate(bookingDto.getEnd())
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }

    public static BookingDtoOut toDto(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                ItemMapper.toDtoBooking(booking.getItem()),
                booking.getStartDate(),
                booking.getEndDate(),
                UserMapper.toDto(booking.getBooker()),
                booking.getStatus());
    }
}
