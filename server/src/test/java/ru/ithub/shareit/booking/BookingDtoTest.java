package ru.ithub.shareit.booking;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.ithub.shareit.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    private static final String DATE_TIME = "2023-11-07T04:35:00";

    private BookingDto bookingDto = null;

    @BeforeEach
    public void init() {
        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.parse("2023-11-07T04:35:00"))
                .end(LocalDateTime.parse("2023-11-07T04:35:00"))
                .build();
    }

    @Test
    @SneakyThrows
    void startSerializes() {
        assertThat(json.write(bookingDto)).extractingJsonPathStringValue("$.start")
                .isEqualTo(DATE_TIME);
    }

    @Test
    @SneakyThrows
    void endSerializes() {
        assertThat(json.write(bookingDto)).extractingJsonPathStringValue("$.end")
                .isEqualTo(DATE_TIME);
    }
}