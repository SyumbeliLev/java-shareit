package ru.ithub.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.ithub.shareit.dto.UserDto;
import ru.ithub.shareit.entity.User;

@UtilityClass
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User toEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }
}