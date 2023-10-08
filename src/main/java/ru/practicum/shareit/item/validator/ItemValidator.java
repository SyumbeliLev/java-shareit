package ru.practicum.shareit.item.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class ItemValidator {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public void checkOwner(Long itemId, Long userId) {
        Item item = itemRepository.getItemById(itemId);
        User user = userRepository.getUserById(userId);
        if (!item.getOwnerId().equals(userId)) {
            throw new NotOwnerException("Пользователь с id = " + user.getId() + " не имеет доступа к изменению предмета с id = " + item.getId());
        }
    }

    public void validationCheck(Long userId) {
        userRepository.getUserById(userId);
    }
}
