package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends ShareItException {
    public NotFoundException(String s) {
        super(s);
    }
}