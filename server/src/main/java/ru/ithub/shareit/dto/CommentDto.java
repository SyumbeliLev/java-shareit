package ru.ithub.shareit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String authorName;
    private ItemDto item;
    private String text;
    private LocalDateTime created;

    public Long getItemId() {
        return item.getId();
    }
}