package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final Sort sortByStartDesc = Sort.by(Sort.Direction.DESC, "startDate");
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoOut create(long userId, BookingDto dto) {
        User user = checkExistUser(userId);
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Предмет c id = " + dto.getItemId() + " не найден!"));
        createValid(dto, user, item);
        Booking booking = BookingMapper.toBooking(user, item, dto);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDtoOut update(long userId, long bookingId, Boolean approved) {
        Booking booking = valid(userId, bookingId, "update");
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        Objects.requireNonNull(booking)
                .setStatus(newStatus);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOut findDetailsBooking(long userId, long bookingId) {
        Booking booking = valid(userId, bookingId, "findDetails");
        return BookingMapper.toDto(Objects.requireNonNull(booking));
    }

    @Override
    public List<BookingDtoOut> findAll(long bookerId, String state, Pageable pageable) {
        checkExistUser(bookerId);
        List<Booking> bookings = new ArrayList<>();
        Pageable pageForBookings = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByStartDesc);
        switch (BookingState.checkExist(state)) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(bookerId, pageForBookings);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndDateBefore(bookerId, LocalDateTime.now(), pageForBookings);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndEndDateAfter(bookerId, LocalDateTime.now(), pageForBookings);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndCurrentDate(bookerId, LocalDateTime.now(), pageForBookings);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.WAITING, pageForBookings);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.REJECTED, pageForBookings);
                break;
        }
        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoOut> findAllOwner(long ownerId, String state, Pageable pageable) {
        checkExistUser(ownerId);
        List<Booking> bookings = new ArrayList<>();
        Pageable pageForBookings = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sortByStartDesc);
        switch (BookingState.checkExist(state)) {
            case ALL:
                bookings = bookingRepository.findAllByItem_Owner_Id(ownerId, pageForBookings);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndDateBefore(ownerId, LocalDateTime.now(), pageForBookings);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItem_Owner_IdAndEndDateAfter(ownerId, LocalDateTime.now(), pageForBookings);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItem_Owner_IdAndCurrentDate(ownerId, LocalDateTime.now(), pageForBookings);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatus(ownerId, BookingStatus.WAITING, pageForBookings);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItem_Owner_IdAndStatus(ownerId, BookingStatus.REJECTED, pageForBookings);
                break;
        }
        return bookings.stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    private User checkExistUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id = " + userId + " не найден!"));
    }

    private void createValid(BookingDto dto, User user, Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования.");
        }
        if (user.getId()
                .equals(item.getOwner()
                        .getId())) {
            throw new NotFoundException("Вещь не найдена.");
        }
        if (dto.getStart()
                .isAfter(dto.getEnd()) || dto.getStart()
                .isEqual(dto.getEnd())) {
            throw new ValidationException("Дата окончания не может быть раньше или равна дате начала");
        }
    }

    private Booking valid(long userId, long bookingId, String operation) {
        checkExistUser(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь с id: " + bookingId + " не найдена!"));
        switch (operation) {
            case "update":
                if (!booking.getItem()
                        .getOwner()
                        .getId()
                        .equals(userId)) {
                    throw new NotFoundException("Пользователь с id = " + userId +
                            " не является собственником вещи id = " + booking.getItem()
                            .getId());
                }
                if (!booking.getStatus()
                        .equals(BookingStatus.WAITING)) {
                    throw new ValidationException("Бронь cо статусом WAITING");
                }
                return booking;
            case "findDetails":
                if (!booking.getBooker()
                        .getId()
                        .equals(userId)
                        && !booking.getItem()
                        .getOwner()
                        .getId()
                        .equals(userId)) {
                    throw new NotFoundException("Пользователь не является владельцем / автором бронирования!");
                }
                return booking;
            default:
                return null;
        }
    }
}
