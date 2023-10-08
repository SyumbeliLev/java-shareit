package ru.practicum.shareit.user.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository repository;

    public void checkValidation(UserDto userDto) {
        if (!repository.getUsersList().isEmpty()) {
            for (User userCheck : repository.getUsersList()) {
                if (userCheck.getEmail().equals(userDto.getEmail())) {
                    throw new ConflictException("Этот email уже зарегистрирован!");
                }
            }
        }
    }
}
