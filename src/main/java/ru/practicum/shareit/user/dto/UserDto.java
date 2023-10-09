package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank(message = "Имя пользователя пустое!")
    private String name;
    @Email(message = "Неверный формат email!")
    @NotBlank(message = "Поле email пустое!!")
    private String email;
}