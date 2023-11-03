package ru.practicum.shareit.item.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final UserRepository userRepository;

    public void checkOwner(Item item, Long userId) {
        if (!item.getOwnerId().equals(userId)) {
            throw new NotOwnerException("Пользователь с id = " + userId + " не имеет доступа к изменению предмета с id = " + item.getId());
        }
    }

    public void validationCheck(Long userId) {
        userRepository.getUserById(userId);
    }
}
