package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.user.entity.User;

public class CommentMapper {

    public static Comment toEntity(CommentDto commentDto, User author, Item item) {
        return Comment.builder()
                .author(author)
                .item(item)
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();
    }

    public static CommentDto toDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor()
                        .getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .item(ItemMapper.toDto(comment.getItem()))
                .build();
    }
}