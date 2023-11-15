package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String authorName;
    private ItemDto item;
    @NotBlank(message = "Комментарий не может быть пустым!")
    private String text;
    private LocalDateTime created;

    public Long getItemId() {
        return item.getId();
    }
}