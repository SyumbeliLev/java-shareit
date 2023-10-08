package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class User {
    private Long id;
    @NotNull(message = "Имя пользователя пустое!")
    @NotEmpty
    @NotBlank
    private String name;
    @Email(message = "Неверный формат email!")
    @NotNull(message = "Поле email пустое!!")
    private String email;
}
