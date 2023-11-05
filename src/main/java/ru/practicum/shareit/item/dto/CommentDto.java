package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
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