package ru.practicum.shareit.request;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RequestRepositoryTest {

    @Autowired
    ItemRequestRepository requestRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final User user1 = User.builder()
            .name("name")
            .email("email@email.com")
            .build();

    private final User user2 = User.builder()
            .name("name2")
            .email("email2@email.com")
            .build();

    private final Item item = Item.builder()
            .name("name")
            .description("description")
            .available(true)
            .owner(user1)
            .build();

    private final ItemRequest request1 = ItemRequest.builder()
            .items(List.of(item))
            .description("request description")
            .created(LocalDateTime.now())
            .requester(user1)
            .build();

    private final ItemRequest request2 = ItemRequest.builder()
            .items(List.of(item))
            .description("request2 description")
            .created(LocalDateTime.now())
            .requester(user2)
            .build();

    @BeforeEach
    public void init() {
        testEntityManager.persist(user1);
        testEntityManager.persist(user2);
        testEntityManager.persist(item);
        testEntityManager.flush();
        requestRepository.save(request1);
        requestRepository.save(request2);
    }

    @Test
    void findAllByRequesterId() {
        List<ItemRequest> requests = requestRepository.findAllByRequesterId(1L);

        assertEquals(requests.size(), 1);
        assertEquals(requests.get(0)
                .getDescription(), "request description");
    }

    @Test
    void findAllByAllOtherUsers_whenUserIdIsUserOneId_thenReturnListOfUserTwoRequest() {
        Pageable page = PageRequest.of(0, 10);
        List<ItemRequest> requestList = new ArrayList<>(requestRepository.findAllByOtherUsers(user1.getId(), page));

        assertEquals(requestList.size(), 1);
        assertEquals(requestList.get(0)
                .getRequester()
                .getName(), "name2");
    }

    @AfterEach
    public void deleteItems() {
        requestRepository.deleteAll();
    }
}