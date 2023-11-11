package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "spring.datasource.username=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceIT {
    private final UserService userService;
    private final EntityManager em;

    @Test
    void addNewUser() {
        UserDto userDto = UserDto.builder()
                .name("name")
                .email("my@email.com")
                .build();
        userService.create(userDto);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.name like :nameUser", User.class);
        User user = query.setParameter("nameUser", userDto.getName())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void getUserByIdWhenUserIdIsNotValid() {
        long userId = 2L;
        Assertions
                .assertThrows(NotFoundException.class, () -> userService.findUserById(userId));
    }
}

