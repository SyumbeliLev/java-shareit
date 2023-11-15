package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentMapperTest {

    private User owner;
    private Item item;

    @BeforeEach
    public void fillData() {
        owner = User.builder()
                .name("name")
                .id(1L)
                .build();
        item = Item.builder()
                .id(1L)
                .name("name")
                .owner(owner)
                .description("desc")
                .available(true)
                .build();
    }

    @Test
    void toEntity() {
        CommentDto commentDto = CommentDto.builder()
                .text("text")
                .created(LocalDateTime.now())
                .id(1L)
                .authorName("Bob")
                .item(ItemMapper.toDto(item))
                .build();

        Comment comment = CommentMapper.toEntity(commentDto, owner, item);

        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(comment.getAuthor()
                .getId(), owner.getId());
        assertEquals(comment.getItem()
                .getId(), item.getId());
    }

    @Test
    void toDto() {
        Comment comment = Comment.builder()
                .author(owner)
                .created(LocalDateTime.now())
                .item(item)
                .text("text")
                .id(1L)
                .build();

        CommentDto actual = CommentMapper.toDto(comment);

        assertEquals(actual.getText(), comment.getText());
        assertEquals(actual.getItem()
                .getId(), item.getId());
        assertEquals(actual.getAuthorName(), owner.getName());
    }
}