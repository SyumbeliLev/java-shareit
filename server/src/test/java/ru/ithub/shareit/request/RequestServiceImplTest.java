package ru.ithub.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.ithub.shareit.exception.NotFoundException;
import ru.ithub.shareit.entity.Item;
import ru.ithub.shareit.dto.ItemRequestDto;
import ru.ithub.shareit.dto.ItemRequestDtoOut;
import ru.ithub.shareit.entity.ItemRequest;
import ru.ithub.shareit.mapper.RequestMapper;
import ru.ithub.shareit.repository.ItemRequestRepository;
import ru.ithub.shareit.service.ItemRequestServiceImpl;
import ru.ithub.shareit.dto.UserDto;
import ru.ithub.shareit.entity.User;
import ru.ithub.shareit.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
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
            .owner(user)
            .build();

    private final ItemRequest request = ItemRequest.builder()
            .id(1L)
            .description("request description")
            .items(List.of(item))
            .build();

    @Test
    void addNewRequest() {
        ItemRequestDto requestDto = RequestMapper.toDto(request);
        ItemRequestDtoOut expectedRequestDto = RequestMapper.toDtoOut(request);
        when(userService.findUserById(user.getId())).thenReturn(userDto);
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(request);

        ItemRequestDtoOut actualRequestDto = requestService.create(user.getId(), requestDto);

        assertEquals(expectedRequestDto, actualRequestDto);
    }

    @Test
    void getUserRequests() {
        List<ItemRequestDtoOut> expectedRequestsDto = List.of(RequestMapper.toDtoOut(request));
        when(userService.findUserById(user.getId())).thenReturn(userDto);
        when(requestRepository.findAllByRequesterId(userDto.getId())).thenReturn(List.of(request));

        List<ItemRequestDtoOut> actualRequestsDto = requestService.findUserRequests(userDto.getId());

        assertEquals(expectedRequestsDto, actualRequestsDto);
    }

    @Test
    void getAllRequests() {
        List<ItemRequestDtoOut> expectedRequestsDto = List.of(RequestMapper.toDtoOut(request));
        when(userService.findUserById(user.getId())).thenReturn(userDto);
        when(requestRepository.findAllByOtherUsers(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(request)));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("created")
                .descending());
        List<ItemRequestDtoOut> actualRequestsDto = requestService.findAllRequests(userDto.getId(), pageable);

        assertEquals(expectedRequestsDto, actualRequestsDto);
    }

    @Test
    void getRequestById() {
        ItemRequestDtoOut expectedRequestDto = RequestMapper.toDtoOut(request);
        when(userService.findUserById(user.getId())).thenReturn(userDto);
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));

        ItemRequestDtoOut actualRequestDto = requestService.findById(userDto.getId(), request.getId());

        assertEquals(expectedRequestDto, actualRequestDto);
    }

    @Test
    void getRequestByIdWhenRequestIdIsNotValidShouldThrowObjectNotFoundException() {
        when(userService.findUserById(user.getId())).thenReturn(userDto);
        when(requestRepository.findById(request.getId())).thenReturn(Optional.empty());

        NotFoundException requestNotFoundException = assertThrows(NotFoundException.class,
                () -> requestService.findById(userDto.getId(), request.getId()));

        assertEquals(requestNotFoundException.getMessage(), String.format("Запрос с id: %s" +
                " не был найден.", request.getId()));
    }
}