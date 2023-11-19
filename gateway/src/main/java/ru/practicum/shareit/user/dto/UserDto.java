package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.util.ValidationGroups;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = ValidationGroups.Create.class, message = "Имя пользователя пустое!")
    private String name;
    @Email(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class}, message = "Неверный формат email!")
    @NotBlank(groups = ValidationGroups.Create.class, message = "Поле email пустое!!")
    private String email;
}