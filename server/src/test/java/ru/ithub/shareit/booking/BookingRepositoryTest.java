package ru.ithub.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.ithub.shareit.entity.Booking;
import ru.ithub.shareit.entity.BookingStatus;
import ru.ithub.shareit.repository.BookingRepository;
import ru.ithub.shareit.entity.Item;
import ru.ithub.shareit.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final User user = User.builder()
            .name("name")
            .email("email@email.com")
            .build();

    private final User owner = User.builder()
            .name("name2")
            .email("email2@email.com")
            .build();

    private final Item item = Item.builder()
            .name("name")
            .description("description")
            .available(true)
            .owner(owner)
            .build();

    private final Booking booking = Booking.builder()
            .item(item)
            .booker(user)
            .status(BookingStatus.APPROVED)
            .startDate(LocalDateTime.now()
                    .minusHours(1L))
            .endDate(LocalDateTime.now()
                    .plusDays(1L))
            .build();

    private final Booking pastBooking = Booking.builder()
            .item(item)
            .booker(user)
            .status(BookingStatus.APPROVED)
            .startDate(LocalDateTime.now()
                    .minusDays(2L))
            .endDate(LocalDateTime.now()
                    .minusDays(1L))
            .build();

    private final Booking futureBooking = Booking.builder()
            .item(item)
            .booker(user)
            .status(BookingStatus.APPROVED)
            .startDate(LocalDateTime.now()
                    .plusDays(1L))
            .endDate(LocalDateTime.now()
                    .plusDays(2L))
            .build();

    @BeforeEach
    public void init() {
        testEntityManager.persist(user);
        testEntityManager.persist(owner);
        testEntityManager.persist(item);
        testEntityManager.flush();
        bookingRepository.save(booking);
        bookingRepository.save(pastBooking);
        bookingRepository.save(futureBooking);
    }

    @AfterEach
    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    @Test
    void findAllByBookerId() {
        Page<Booking> bookings = bookingRepository.findAllByBookerId(1L, PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 3);
        Assertions.assertEquals(bookings.getContent()
                .get(0)
                .getBooker()
                .getId(), 1L);
    }

    @Test
    void findAllCurrentBookingsByBookerId() {
        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndCurrentDate(1L, LocalDateTime.now(),
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 1);
        Assertions.assertEquals(bookings.getContent()
                .get(0)
                .getBooker()
                .getId(), 1L);
    }

    @Test
    void findAllPastBookingsByBookerId() {
        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndEndDateBefore(1L, LocalDateTime.now(),
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 1);
        assertEquals(bookings.getContent()
                .get(0)
                .getId(), 2L);
    }

    @Test
    void findAllFutureBookingsByBookerId() {
        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndEndDateAfter(1L, LocalDateTime.now(),
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 2);
        assertEquals(bookings.getContent()
                .get(0)
                .getId(), 1L);
    }

    @Test
    void findAllWaitingBookingsByBookerId() {
        Booking waitingBooking = Booking.builder()
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .startDate(LocalDateTime.now()
                        .plusDays(1L))
                .endDate(LocalDateTime.now()
                        .plusDays(2L))
                .build();

        bookingRepository.save(waitingBooking);
        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndStatus(1L, BookingStatus.WAITING,
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 1);
        assertEquals(bookings.getContent()
                .get(0)
                .getStatus(), BookingStatus.WAITING);
    }

    @Test
    void findAllRejectedBookingsByBookerId() {
        Booking rejectedBooking = Booking.builder()
                .item(item)
                .booker(user)
                .status(BookingStatus.REJECTED)
                .startDate(LocalDateTime.now()
                        .plusDays(1L))
                .endDate(LocalDateTime.now()
                        .plusDays(2L))
                .build();

        bookingRepository.save(rejectedBooking);
        Page<Booking> bookings = bookingRepository.findAllByBookerIdAndStatus(1L, BookingStatus.REJECTED,
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 1);
        assertEquals(bookings.getContent()
                .get(0)
                .getStatus(), BookingStatus.REJECTED);
    }

    @Test
    void findAllByOwnerId() {
        Page<Booking> bookings = bookingRepository.findAllByItem_Owner_Id(2L, PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 3);
    }

    @Test
    void findAllCurrentBookingsByOwnerId() {
        Page<Booking> bookings = bookingRepository.findAllByItem_Owner_IdAndCurrentDate(2L, LocalDateTime.now(),
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 1);
        Assertions.assertEquals(bookings.getContent()
                .get(0)
                .getItem()
                .getOwner()
                .getId(), 2L);
    }

    @Test
    void findAllPastBookingsByOwnerId() {
        Page<Booking> bookings = bookingRepository.findAllByItem_Owner_IdAndEndDateBefore(2L, LocalDateTime.now(),
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 1);
        Assertions.assertEquals(bookings.getContent()
                .get(0)
                .getItem()
                .getOwner()
                .getId(), 2L);
    }

    @Test
    void findAllFutureBookingsByOwnerId() {
        Page<Booking> bookings = bookingRepository.findAllByItem_Owner_IdAndEndDateAfter(2L, LocalDateTime.now(),
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 2);
        Assertions.assertEquals(bookings.getContent()
                .get(0)
                .getItem()
                .getOwner()
                .getId(), 2L);
    }

    @Test
    void findAllWaitingBookingsByOwnerId() {
        Booking waitingBooking = Booking.builder()
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .startDate(LocalDateTime.now()
                        .plusDays(1L))
                .endDate(LocalDateTime.now()
                        .plusDays(2L))
                .build();

        bookingRepository.save(waitingBooking);
        Page<Booking> bookings = bookingRepository.findAllByItem_Owner_IdAndStatus(2L, BookingStatus.WAITING,
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 1);
        assertEquals(bookings.getContent()
                .get(0)
                .getStatus(), BookingStatus.WAITING);
    }

    @Test
    void findAllRejectedBookingsByOwnerId() {
        Booking rejectedBooking = Booking.builder()
                .item(item)
                .booker(user)
                .status(BookingStatus.REJECTED)
                .startDate(LocalDateTime.now()
                        .plusDays(1L))
                .endDate(LocalDateTime.now()
                        .plusDays(2L))
                .build();

        bookingRepository.save(rejectedBooking);
        Page<Booking> bookings = bookingRepository.findAllByItem_Owner_IdAndStatus(2L, BookingStatus.REJECTED,
                PageRequest.of(0, 10));

        assertEquals(bookings.getContent()
                .size(), 1);
        assertEquals(bookings.getContent()
                .get(0)
                .getStatus(), BookingStatus.REJECTED);
    }

    @Test
    void findAllByUserBookings() {
        List<Booking> bookings = bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(1L, 1L, LocalDateTime.now());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0)
                .getStatus(), BookingStatus.APPROVED);
    }
}