package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    private Long id;
    @NotBlank(message = "Название предмета не должно быть пустым.")
    @Size(max = 32)
    private String name;
    @NotBlank(message = "Описание предмета не должно быть пустым.")
    @Size(max = 256)
    private String description;
    @NotNull(message = "Доступ предмета не указан")
    private Boolean available;
    private Long requestId;
}