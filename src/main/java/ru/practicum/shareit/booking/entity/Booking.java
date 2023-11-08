package ru.practicum.shareit.booking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    private User booker;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BookingStatus status;
}
