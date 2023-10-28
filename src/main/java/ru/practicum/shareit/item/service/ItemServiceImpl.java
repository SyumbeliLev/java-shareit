package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, long userId) {
        UserDto userDto = UserMapper.toDto(getUser(userId));
        Item item = ItemMapper.toEntity(itemDto);
        item.setOwner(UserMapper.toEntity(userDto));
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, long userId, long itemId) {
        Item entity = getEntity(itemId);
        if (!entity.getOwner()
                .getId()
                .equals(userId)) {
            throw new NotOwnerException("Пользователь с id = " + userId + " не является собственником вещи id = " + itemId);
        }
        ItemMapper.update(itemDto, entity);
        return ItemMapper.toDto(itemRepository.save(entity));
    }

    @Override
    public ItemDto findItemById(long itemId, long userId) {
        getUser(userId);
        Item item = getEntity(itemId);

        ItemDto itemDtoOut = ItemMapper.toDto(item);
        itemDtoOut.setComments(getAllItemComments(itemId));
        if (!item.getOwner()
                .getId()
                .equals(userId)) {
            return itemDtoOut;
        }
        List<Booking> bookings = bookingRepository.findAllByItemAndStatusOrderByStartDateAsc(item, BookingStatus.APPROVED);
        List<BookingDtoOut> bookingDTOList = bookings.stream()
                .map(BookingMapper::toDto)
                .collect(toList());

        itemDtoOut.setLastBooking(getLastBooking(bookingDTOList, LocalDateTime.now()));
        itemDtoOut.setNextBooking(getNextBooking(bookingDTOList, LocalDateTime.now()));
        return itemDtoOut;
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        getUser(userId);
        List<Item> itemList = itemRepository.findAllByOwnerId(userId);
        List<Long> idList = itemList.stream()
                .map(Item::getId)
                .collect(toList());
        Map<Long, List<CommentDto>> comments = commentRepository.findAllByItemIdIn(idList)
                .stream()
                .map(CommentMapper::toDto)
                .collect(groupingBy(CommentDto::getItemId, toList()));

        Map<Long, List<BookingDtoOut>> bookings = bookingRepository.findAllByItemInAndStatusOrderByStartDateAsc(itemList, BookingStatus.APPROVED)
                .stream()
                .map(BookingMapper::toDto)
                .collect(groupingBy(BookingDtoOut::getItemId, toList()));

        return itemList.stream()
                .map(item -> ItemMapper.toDto(item, getLastBooking(bookings.get(item.getId()), LocalDateTime.now()), comments.get(item.getId()), getNextBooking(bookings.get(item.getId()), LocalDateTime.now())))
                .collect(toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text)
                .stream()
                .map(ItemMapper::toDto)
                .collect(toList());
    }

    @Override
    @Transactional
    public CommentDto createComment(long userId, CommentDto commentDto, long itemId) {
        User user = getUser(userId);
        Item item = getEntity(itemId);

        List<Booking> bookings = bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(userId, itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("У пользователя с id   " + userId + " должно быть хотя бы одно бронирование предмета с id " + itemId);
        }

        Comment entityComment = commentRepository.save(CommentMapper.toEntity(commentDto, user, item));
        return CommentMapper.toDto(entityComment);
    }

    private Item getEntity(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Предмет c id = " + id + " не найден!"));
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь c id = " + userId + " не найден!"));
    }

    public List<CommentDto> getAllItemComments(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);

        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(toList());
    }


    private BookingDtoOut getLastBooking(List<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                .filter(bookingDTO -> !bookingDTO.getStart()
                        .isAfter(time))
                .reduce((booking1, booking2) -> booking1.getStart()
                        .isAfter(booking2.getStart()) ? booking1 : booking2)
                .orElse(null);
    }

    private BookingDtoOut getNextBooking(List<BookingDtoOut> bookings, LocalDateTime time) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                .filter(bookingDTO -> bookingDTO.getStart()
                        .isAfter(time))
                .findFirst()
                .orElse(null);
    }
}
