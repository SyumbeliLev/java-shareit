package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.ValidationGroups.Create;
import ru.practicum.shareit.user.ValidationGroups.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = Create.class, message = "Имя пользователя пустое!")
    private String name;
    @Email(groups = {Create.class, Update.class}, message = "Неверный формат email!")
    @NotBlank(groups = Create.class, message = "Поле email пустое!!")
    private String email;
}