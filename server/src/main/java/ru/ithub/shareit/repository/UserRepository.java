package ru.ithub.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ithub.shareit.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
