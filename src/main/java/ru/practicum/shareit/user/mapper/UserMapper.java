package ru.practicum.shareit.user.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
@UtilityClass
public class UserMapper {
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static void update(User entity, UserDto dto) {
        String name = dto.getName();
        if (name != null && !name.isBlank()) {
            entity.setName(name);
        }
        String email = dto.getEmail();
        if (email != null && !email.isBlank()) {
            entity.setEmail(email);
        }
    }
}