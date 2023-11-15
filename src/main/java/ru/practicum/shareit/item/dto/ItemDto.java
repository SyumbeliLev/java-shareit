package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.user.ValidationGroups;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemDto {
    private Long id;
    @NotBlank(message = "Название предмета не должно быть пустым.", groups = ValidationGroups.Create.class)
    @Size(max = 32)
    private String name;
    @NotBlank(message = "Описание предмета не должно быть пустым.", groups = ValidationGroups.Create.class)
    @Size(max = 256, groups = ValidationGroups.Create.class)
    private String description;
    @NotNull(message = "Доступ предмета не указан", groups = ValidationGroups.Create.class)
    private Boolean available;
    private Long requestId;
    private BookingDtoOut lastBooking;
    private List<CommentDto> comments;
    private BookingDtoOut nextBooking;
}