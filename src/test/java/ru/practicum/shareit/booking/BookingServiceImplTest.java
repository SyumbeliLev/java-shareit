package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final User owner = User.builder()
            .id(2L)
            .name("username2")
            .email("email2@email.com")
            .build();

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .available(true)
            .owner(owner)
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .startDate(LocalDateTime.now()
                    .plusDays(1L))
            .endDate(LocalDateTime.now()
                    .plusDays(2L))
            .status(BookingStatus.APPROVED)
            .item(item)
            .booker(user)
            .build();

    private final Booking bookingWaiting = Booking.builder()
            .id(1L)
            .startDate(LocalDateTime.now()
                    .plusDays(1L))
            .endDate(LocalDateTime.now()
                    .plusDays(2L))
            .status(BookingStatus.WAITING)
            .item(item)
            .booker(user)
            .build();

    private final BookingDto bookingDto = BookingDto.builder()
            .itemId(1L)
            .start(LocalDateTime.now()
                    .plusDays(1L))
            .end(LocalDateTime.now()
                    .plusDays(2L))
            .build();

    private final BookingDto bookingDtoEndBeforeStart = BookingDto.builder()
            .itemId(1L)
            .start(LocalDateTime.now()
                    .plusDays(1L))
            .end(LocalDateTime.now()
                    .minusDays(1L))
            .build();

    @Test
    void createTest() {
        BookingDtoOut expectedBookingDtoOut = BookingMapper.toDto(BookingMapper.toBooking(user, item, bookingDto));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(user, item, bookingDto));

        BookingDtoOut actualBookingDtoOut = bookingService.create(userDto.getId(), bookingDto);

        assertEquals(expectedBookingDtoOut, actualBookingDtoOut);
    }

    @Test
    void createWhenEndIsBeforeStartShouldThrowValidationException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ValidationException bookingValidationException = assertThrows(ValidationException.class,
                () -> bookingService.create(userDto.getId(), bookingDtoEndBeforeStart));

        assertEquals(bookingValidationException.getMessage(), "Дата окончания не может быть раньше или равна дате начала");
    }

    @Test
    void createWhenItemIsNotAvailableShouldThrowValidationException() {
        item.setAvailable(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ValidationException bookingValidationException = assertThrows(ValidationException.class,
                () -> bookingService.create(userDto.getId(), bookingDto));

        assertEquals(bookingValidationException.getMessage(), "Вещь недоступна для бронирования.");
    }

    @Test
    void createWhenItemOwnerEqualsBookerShouldThrowValidationException() {
        item.setOwner(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        NotFoundException bookingNotFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.create(userDto.getId(), bookingDto));

        assertEquals(bookingNotFoundException.getMessage(), "Вещь не найдена.");
    }

    @Test
    void updateTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingWaiting);

        BookingDtoOut actualBookingDtoOut = bookingService.update(owner.getId(), bookingWaiting.getId(), true);

        assertEquals(BookingStatus.APPROVED, actualBookingDtoOut.getStatus());
    }

    @Test
    void updateWhenStatusNotApproved() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingWaiting);

        BookingDtoOut actualBookingDtoOut = bookingService.update(owner.getId(), bookingWaiting.getId(), false);

        assertEquals(BookingStatus.REJECTED, actualBookingDtoOut.getStatus());
    }

    @Test
    void updateShouldStatusNotWaiting() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        ValidationException bookingValidationException = assertThrows(ValidationException.class,
                () -> bookingService.update(owner.getId(), booking.getId(), false));

        assertEquals(bookingValidationException.getMessage(), "Бронь cо статусом WAITING");
    }

    @Test
    void updateWhenUserIsNotItemOwnerShouldThrowNotFoundException() {
        NotFoundException bookingNotFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.update(userDto.getId(), booking.getId(), true));

        assertEquals(bookingNotFoundException.getMessage(), "Пользователь c id = 1 не найден!");
    }

    @Test
    void findDetailsBookingTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        BookingDtoOut expectedBookingDtoOut = BookingMapper.toDto(booking);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDtoOut actualBookingDtoOut = bookingService.findDetailsBooking(user.getId(), booking.getId());

        assertEquals(expectedBookingDtoOut, actualBookingDtoOut);
    }

    @Test
    void findDetailsBookingWhenBookingIdIsNotValidShouldThrowObjectNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        NotFoundException bookingNotFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.findDetailsBooking(1L, booking.getId()));

        assertEquals(bookingNotFoundException.getMessage(), "Бронь с id: 1 не найдена!");
    }

    @Test
    void findDetailsBookingWhenUserIsNotItemOwnerShouldThrowObjectNotFoundException() {
        NotFoundException bookingNotFoundException = assertThrows(NotFoundException.class,
                () -> bookingService.findDetailsBooking(3L, booking.getId()));

        assertEquals(bookingNotFoundException.getMessage(), "Пользователь c id = 3 не найден!");
    }

    @Test
    void findAllByBookerWhenBookingStateAll() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerId(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAll(user.getId(), "ALL", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByBooker_whenBookingStateCURRENT() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndCurrentDate(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAll(user.getId(), "CURRENT", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByBookerWhenBookingStatePAST() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndEndDateBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAll(user.getId(), "PAST", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByBookerWhenBookingStateFUTURE() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndEndDateAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAll(user.getId(), "FUTURE", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByBookerWhenBookingStateWAITING() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(BookingStatus.WAITING.getDeclaringClass()), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAll(user.getId(), "WAITING", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByBookerWhenBookingStateIsNotValidShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> bookingService.findAll(user.getId(), "ERROR", PageRequest.of(0 / 10, 10)));
    }

    @Test
    void findAllByOwnerWhenBookingStateAll() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_Owner_Id(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAllOwner(user.getId(), "ALL", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByOwnerWhenBookingStateCURRENT() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_Owner_IdAndCurrentDate(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAllOwner(user.getId(), "CURRENT", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByOwnerWhenBookingStatePAST() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_Owner_IdAndEndDateBefore(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAllOwner(user.getId(), "PAST", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByOwnerWhenBookingStateFUTURE() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_Owner_IdAndEndDateAfter(anyLong(), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAllOwner(user.getId(), "FUTURE", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByOwnerWhenBookingStateWAITING() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_Owner_IdAndStatus(anyLong(), any(BookingStatus.WAITING.getDeclaringClass()), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAllOwner(user.getId(), "WAITING", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByOwnerWhenBookingStateREJECTED() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByItem_Owner_IdAndStatus(anyLong(), any(BookingStatus.REJECTED.getDeclaringClass()), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAllOwner(user.getId(), "REJECTED", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByBookerWhenBookingStateREJECTED() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toDto(booking));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(BookingStatus.WAITING.getDeclaringClass()), any(Pageable.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.findAll(user.getId(), "REJECTED", PageRequest.of(0 / 10, 10));

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void findAllByOwnerWhenBookingStateIsNotValidThenThrowIllegalArgumentException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class,
                () -> bookingService.findAllOwner(user.getId(), "ERROR", PageRequest.of(0 / 10, 10)));
    }
}