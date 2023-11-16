package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.item.mapper.ItemMapper.toDto;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final User user2 = User.builder()
            .id(2L)
            .name("username2")
            .email("email2@email.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .available(true)
            .owner(user)
            .build();

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .available(true)
            .comments(Collections.emptyList())
            .build();
    private final Comment comment = Comment.builder()
            .id(1L)
            .text("comment")
            .created(LocalDateTime.now())
            .author(user)
            .item(item)
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .item(item)
            .booker(user)
            .status(BookingStatus.APPROVED)
            .startDate(LocalDateTime.now()
                    .minusDays(1L))
            .endDate(LocalDateTime.now()
                    .plusDays(1L))
            .build();

    @Test
    void addNewItemWhenInvoked() {
        Item expectedItem = Item.builder()
                .available(true)
                .description("desc")
                .name("name")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(User.builder()
                .build()));
        when(itemRepository.save(any())).thenReturn(expectedItem);

        ItemDto actualItem = itemService.create(toDto(expectedItem), 1L);
        assertEquals(expectedItem.getName(), actualItem.getName());
        assertEquals(expectedItem.getDescription(), actualItem.getDescription());
        assertEquals(expectedItem.getAvailable(), actualItem.getAvailable());
    }

    @Test
    void getItemById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        ItemDto actualItemDto = itemService.findItemById(user.getId(), item.getId());

        assertEquals(itemDto, actualItemDto);
    }

    @Test
    void updateItem() {
        ItemRequest itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now(), null);
        Item updatedItem = Item.builder()
                .id(1L)
                .name("updated name")
                .description("updated description")
                .available(false)
                .owner(user)
                .request(itemRequest)
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(updatedItem));
        ItemDto savedItem = itemService.update(toDto(updatedItem), user.getId(), updatedItem.getId());
        assertEquals("updated name", savedItem.getName());
        assertEquals("updated description", savedItem.getDescription());
    }

    @Test
    void updateItemWhenUserIsNotItemOwnerShouldThrowException() {
        Item updatedItem = Item.builder()
                .id(1L)
                .name("updated name")
                .description("updated description")
                .available(false)
                .owner(user2)
                .build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(updatedItem));
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> itemService.update(toDto(Objects.requireNonNull(updatedItem)), itemDto.getId(), user.getId()));

        assertEquals(notFoundException.getMessage(), "Пользователь с id = " + user.getId() + " не является собственником вещи id = " + item.getId());
    }

    @Test
    void updateItemWhenItemIdIsNotValid() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        NotFoundException itemNotFoundException = assertThrows(NotFoundException.class, () -> itemService.update(toDto(item), itemDto.getId(), user.getId()));
        assertEquals(itemNotFoundException.getMessage(), "Предмет c id = 1 не найден!");
    }

    @Test
    void getAllComments() {
        List<CommentDto> expectedCommentsDto = List.of(CommentMapper.toDto(comment));
        when(commentRepository.findAllByItemId(item.getId())).thenReturn(List.of(comment));

        List<CommentDto> actualComments = itemService.getAllItemComments(item.getId());

        assertEquals(actualComments.size(), 1);
        assertEquals(actualComments, expectedCommentsDto);
    }

    @Test
    void searchItems() {
        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(List.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        List<ItemDto> actualItemsDto = itemService.findAll(1L, PageRequest.of(0 / 10, 10));

        assertEquals(1, actualItemsDto.size());
        assertEquals(1, actualItemsDto.get(0)
                .getId());
        assertEquals("item name", actualItemsDto.get(0)
                .getName());
    }

    @Test
    void createComment() {
        CommentDto expectedCommentDto = CommentMapper.toDto(comment);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(anyLong(), anyLong(), any())).thenReturn(List.of(booking));

        CommentDto actualCommentDto = itemService.createComment(user.getId(), CommentMapper.toDto(comment), item.getId());

        assertEquals(expectedCommentDto, actualCommentDto);
    }

    @Test
    void createComment_whenItemIdIsNotValid_thenThrowObjectNotFoundException() {
        NotFoundException itemNotFoundException = assertThrows(NotFoundException.class, () -> itemService.createComment(user.getId(), CommentMapper.toDto(comment), item.getId()));

        assertEquals(itemNotFoundException.getMessage(), "Пользователь c id = 1 не найден!");
    }

    @Test
    void createCommentWhenUserHaveNotAnyBookingsShouldThrowValidationException() {
        NotFoundException userBookingsNotFoundException = assertThrows(NotFoundException.class, () -> itemService.createComment(user.getId(), CommentMapper.toDto(comment), item.getId()));

        assertEquals(userBookingsNotFoundException.getMessage(), "Пользователь c id = 1 не найден!");
    }
}