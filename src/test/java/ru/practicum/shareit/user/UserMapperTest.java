package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;


class UserMapperTest {

    @Test
    void toDto() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@Mail.ru")
                .build();
        User userFromConstructor = new User(1L, "name", "mail@mail.ru");
        UserDto userDto = UserMapper.toDto(user);
        UserDto userDtoConstructor = UserMapper.toDto(userFromConstructor);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(userDtoConstructor.getName(), userFromConstructor.getName());
    }

    @Test
    void toEntity() {
        UserDto userDto = UserDto.builder()
                .id(2L)
                .email("email@mail.ru")
                .name("name")
                .build();
        User user = UserMapper.toEntity(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());
    }

    @Test
    void toUserUpdate() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@Mail.ru")
                .build();
        UserDto userDto = UserDto.builder()
                .id(2L)
                .email("email@mail.ru")
                .name("name")
                .build();

        User userToUpdate = UserMapper.update(user, userDto);
        assertEquals(userToUpdate.getName(), user.getName());
        assertEquals(userToUpdate.getEmail(), userDto.getEmail());
    }
}