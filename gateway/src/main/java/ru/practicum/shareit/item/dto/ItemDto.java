package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.util.ValidationGroups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    @NotBlank(message = "Название предмета не должно быть пустым.")
    @Size(max = 32)
    private String name;
    @NotBlank(message = "Описание предмета не должно быть пустым.")
    @Size(max = 256, groups = ValidationGroups.Create.class)
    private String description;
    @NotNull(message = "Доступ предмета не указан")
    private Boolean available;
    private Long requestId;
}
