package ru.ithub.shareit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ithub.shareit.dto.BookingDtoOut;
import ru.ithub.shareit.dto.CommentDto;
import ru.ithub.shareit.dto.ItemDto;
import ru.ithub.shareit.dto.UserDto;
import ru.ithub.shareit.entity.*;
import ru.ithub.shareit.exception.NotFoundException;
import ru.ithub.shareit.exception.ValidationException;
import ru.ithub.shareit.mapper.BookingMapper;
import ru.ithub.shareit.mapper.CommentMapper;
import ru.ithub.shareit.mapper.ItemMapper;
import ru.ithub.shareit.mapper.UserMapper;
import ru.ithub.shareit.repository.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, long userId) {
        UserDto userDto = UserMapper.toDto(getUser(userId));
        Item item = ItemMapper.toEntity(itemDto);
        if (itemDto.getRequestId() != null) {
            item.setRequest(itemRequestRepository.getReferenceById(itemDto.getRequestId()));
        }
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
            throw new NotFoundException("Пользователь с id = " + userId + " не является собственником вещи id = " + itemId);
        }
        String name = itemDto.getName();
        if (name != null && !name.isBlank()) {
            entity.setName(name);
        }
        String description = itemDto.getDescription();
        if (description != null && !description.isBlank()) {
            entity.setDescription(description);
        }
        if (itemDto.getAvailable() != null) {
            entity.setAvailable(itemDto.getAvailable());
        }
        Long requestId = itemDto.getRequestId();
        if (requestId != null) {
            entity.setRequest(entity.getRequest());
        }

        itemRepository.save(entity);
        return ItemMapper.toDto(entity);
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
                .collect(Collectors.toList());

        itemDtoOut.setLastBooking(getLastBooking(bookingDTOList, LocalDateTime.now()));
        itemDtoOut.setNextBooking(getNextBooking(bookingDTOList, LocalDateTime.now()));
        return itemDtoOut;
    }

    @Override
    public List<ItemDto> findAll(long userId, Pageable pageable) {
        getUser(userId);
        List<Item> itemList = itemRepository.findAllByOwnerId(userId, pageable)
                .stream()
                .sorted((o1, o2) -> Math.toIntExact(o1.getId() - o2.getId()))
                .collect(Collectors.toList());
        List<Long> idList = itemList.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        Map<Long, List<CommentDto>> comments = commentRepository.findAllByItemIdIn(idList)
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.groupingBy(CommentDto::getItemId, Collectors.toList()));

        Map<Long, List<BookingDtoOut>> bookings = bookingRepository.findAllByItemInAndStatusOrderByStartDateAsc(itemList, BookingStatus.APPROVED)
                .stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.groupingBy(BookingDtoOut::getItemId, Collectors.toList()));

        return itemList.stream()
                .map(item -> ItemMapper.toDto(item, getLastBooking(bookings.get(item.getId()), LocalDateTime.now()), comments.get(item.getId()), getNextBooking(bookings.get(item.getId()), LocalDateTime.now())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text, Pageable pageable) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text, pageable)
                .stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
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
                .collect(Collectors.toList());
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
