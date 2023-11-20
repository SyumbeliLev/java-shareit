package ru.ithub.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.ithub.shareit.dto.BookingDto;
import ru.ithub.shareit.dto.BookingDtoOut;
import ru.ithub.shareit.entity.Booking;
import ru.ithub.shareit.entity.BookingStatus;
import ru.ithub.shareit.entity.Item;
import ru.ithub.shareit.entity.User;

@UtilityClass
public class BookingMapper {
    public Booking toBooking(User booker, Item item, BookingDto bookingDto) {
        return Booking.builder()
                .item(item)
                .startDate(bookingDto.getStart())
                .endDate(bookingDto.getEnd())
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();
    }

    public BookingDtoOut toDto(Booking booking) {
        return new BookingDtoOut(
                booking.getId(),
                ItemMapper.toDtoBooking(booking.getItem()),
                booking.getStartDate(),
                booking.getEndDate(),
                UserMapper.toDto(booking.getBooker()),
                booking.getStatus());
    }
}
