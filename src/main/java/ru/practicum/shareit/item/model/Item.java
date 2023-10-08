package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
public class Item {
    private Long id;
    @NotNull
    @NotEmpty
    @NotBlank(message = "Имя предмета пустое")
    private String name;
    @NotNull
    private String description;
    @NotNull(message = "Доступ предмета не указан")
    private Boolean available;
    private Long ownerId;
    private ItemRequest request;
}
