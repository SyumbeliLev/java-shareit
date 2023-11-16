package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingStatus;
import ru.practicum.shareit.item.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItemInAndStatusOrderByStartDateAsc(List<Item> items, BookingStatus status);

    List<Booking> findAllByItemAndStatusOrderByStartDateAsc(Item item, BookingStatus bookingStatus);

    List<Booking> findAllByBookerId(Long bookerId, Pageable page);

    List<Booking> findAllByBookerIdAndEndDateBefore(Long bookerId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByBookerIdAndEndDateAfter(Long bookerId, LocalDateTime currentTime, Pageable page);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.startDate < :currentTime AND b.endDate > :currentTime")
    List<Booking> findAllByBookerIdAndCurrentDate(Long bookerId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus status, Pageable page);

    List<Booking> findAllByItem_Owner_Id(Long ownerId, Pageable page);

    List<Booking> findAllByItem_Owner_IdAndEndDateBefore(Long ownerId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByItem_Owner_IdAndEndDateAfter(Long ownerId, LocalDateTime currentTime, Pageable page);

    @Query("select b from Booking b where b.item.owner.id = :ownerId AND b.startDate < :currentTime AND b.endDate > :currentTime")
    List<Booking> findAllByItem_Owner_IdAndCurrentDate(Long ownerId, LocalDateTime currentTime, Pageable page);

    List<Booking> findAllByItem_Owner_IdAndStatus(Long ownerId, BookingStatus status, Pageable page);

    @Query("select b from Booking b where b.item.id = :itemId AND b.booker.id = :bookerId AND b.endDate <= :currentTime")
    List<Booking> findAllByUserIdAndItemIdAndEndDateIsPassed(Long bookerId, Long itemId, LocalDateTime currentTime);
}
