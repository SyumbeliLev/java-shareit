package ru.ithub.shareit.user;

import org.junit.jupiter.api.Test;
import ru.ithub.shareit.dto.UserDto;
import ru.ithub.shareit.entity.User;
import ru.ithub.shareit.mapper.UserMapper;

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
}