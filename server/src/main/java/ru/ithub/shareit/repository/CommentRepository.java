package ru.ithub.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ithub.shareit.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemId(Long itemId);

    List<Comment> findAllByItemIdIn(List<Long> itemId);
}