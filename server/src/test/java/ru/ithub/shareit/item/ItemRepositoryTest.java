package ru.ithub.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.ithub.shareit.entity.Item;
import ru.ithub.shareit.repository.ItemRepository;
import ru.ithub.shareit.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final User user = User.builder()
            .name("name")
            .email("email@email.com")
            .build();

    private final Item item = Item.builder()
            .name("name")
            .description("description")
            .available(true)
            .owner(user)
            .build();

    @BeforeEach
    public void addItems() {
        testEntityManager.persist(user);
        testEntityManager.flush();
        itemRepository.save(item);
    }

    @AfterEach
    public void deleteAll() {
        itemRepository.deleteAll();
    }

    @Test
    void findAllByOwnerId() {
        Page<Item> items = itemRepository.findAllByOwnerId(1L, PageRequest.of(0, 1));

        assertEquals(items.getContent()
                .size(), 1);
        assertEquals(items.getContent()
                .get(0)
                .getName(), "name");
    }

    @Test
    void search() {
        Page<Item> items = itemRepository.search("name", PageRequest.of(0, 1));
        assertEquals(items.getContent()
                .size(), 1);
        assertEquals(items.getContent()
                .get(0)
                .getName(), "name");
    }
}