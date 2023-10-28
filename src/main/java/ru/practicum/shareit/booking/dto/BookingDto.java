package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.ValidationGroups;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    @NotNull(groups = ValidationGroups.Create.class)
    private Long itemId;
    @NotNull(groups = ValidationGroups.Create.class)
    @FutureOrPresent(groups = ValidationGroups.Create.class)
    private LocalDateTime start;
    @NotNull(groups = ValidationGroups.Create.class)
    @Future(groups = ValidationGroups.Create.class)
    private LocalDateTime end;
}
