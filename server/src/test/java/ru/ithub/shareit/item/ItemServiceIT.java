package ru.ithub.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.ithub.shareit.dto.ItemDto;
import ru.ithub.shareit.entity.Item;
import ru.ithub.shareit.service.ItemService;
import ru.ithub.shareit.dto.UserDto;
import ru.ithub.shareit.service.UserService;

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
public class ItemServiceIT {

    private final EntityManager em;
    private final ItemService itemService;
    private final UserService userService;
    private final UserDto userDto1 = UserDto.builder()
            .name("name1")
            .email("email1@email.com")
            .build();

    @Test
    void saveNewItem() {
        UserDto userDto = new UserDto(1L, "User", "user@mail.ru");
        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("cool item")
                .available(true)
                .build();

        UserDto user = userService.create(userDto);
        itemService.create(itemDto, user.getId());

        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name like :item", Item.class);
        Item item = queryItem.setParameter("item", itemDto.getName())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
    }

    @Test
    void getItemByIdWhenItemIdIsNotValid() {
        long itemId = 3L;

        Assertions
                .assertThrows(RuntimeException.class,
                        () -> itemService.findItemById(userDto1.getId(), itemId));
    }
}