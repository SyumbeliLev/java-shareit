package ru.ithub.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ithub.shareit.exception.NotFoundException;
import ru.ithub.shareit.dto.UserDto;
import ru.ithub.shareit.entity.User;
import ru.ithub.shareit.mapper.UserMapper;
import ru.ithub.shareit.repository.UserRepository;
import ru.ithub.shareit.service.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("name")
            .email("my@email.com")
            .build();

    @Test
    void createNewUserReturnUserDto() {
        User userToSave = User.builder()
                .id(1L)
                .name("name")
                .email("my@email.com")
                .build();
        when(userRepository.save(any())).thenReturn(userToSave);
        UserDto actualUserDto = userService.create(UserMapper.toDto(userToSave));
        assertEquals(userDto.getId(), actualUserDto.getId());
        assertEquals(userDto.getName(), actualUserDto.getName());
        assertEquals(userDto.getEmail(), actualUserDto.getEmail());
        verify(userRepository).save(any());
    }

    @Test
    void updateUserTest() {
        UserDto fieldsToUpdate = UserDto.builder()
                .build();
        fieldsToUpdate.setEmail("updated@example.com");
        fieldsToUpdate.setName("Updated User");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(UserMapper.toEntity(userDto)));
        UserDto updatedUserDto = userService.update(fieldsToUpdate, userDto.getId());
        assertNotNull(updatedUserDto);
        assertEquals("Updated User", updatedUserDto.getName());
        assertEquals("updated@example.com", updatedUserDto.getEmail());
    }

    @Test
    void findUserByIdWhenUserFound() {
        long userId = 1L;
        User expectedUser = User.builder()
                .id(1L)
                .name("name")
                .email("my@email.com")
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));
        UserDto expectedUserDto = UserMapper.toDto(expectedUser);

        UserDto actualUserDto = userService.findUserById(userId);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void findUserByIdWhenUserNotFound() {
        long userId = 0L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        NotFoundException userNotFoundException = assertThrows(NotFoundException.class, () -> userService.findUserById(userId));

        assertEquals(userNotFoundException.getMessage(), "Пользователь c id = 0 не найден!");
    }

    @Test
    void findAllUsersTest() {
        List<User> expectedUsers = List.of(new User());
        List<UserDto> expectedUserDto = expectedUsers.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserDto> actualUsersDto = userService.findAll();

        assertEquals(actualUsersDto.size(), 1);
        assertEquals(actualUsersDto, expectedUserDto);
    }

    @Test
    void deleteUser() {
        long userId = 0L;
        userService.delete(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}