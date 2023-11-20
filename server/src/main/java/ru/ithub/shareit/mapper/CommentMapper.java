package ru.ithub.shareit.mapper;

import lombok.experimental.UtilityClass;
import ru.ithub.shareit.dto.CommentDto;
import ru.ithub.shareit.entity.Comment;
import ru.ithub.shareit.entity.Item;
import ru.ithub.shareit.entity.User;

@UtilityClass
public class CommentMapper {

    public Comment toEntity(CommentDto commentDto, User author, Item item) {
        return Comment.builder()
                .author(author)
                .item(item)
                .text(commentDto.getText())
                .created(commentDto.getCreated())
                .build();
    }

    public CommentDto toDto(Comment comment) {
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