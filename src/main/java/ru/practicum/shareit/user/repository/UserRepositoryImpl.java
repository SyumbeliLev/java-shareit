package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UserNotExistException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> usersMap = new HashMap<>();
    private final Set<String> userEmail = new HashSet<>();

    private Long id = 0L;

    @Override
    public User addUser(User user) {
        validationEmail(user.getEmail());
        user.setId(++id);
        usersMap.put(id, user);
        return usersMap.get(id);
    }

    @Override
    public User getUserById(Long userId) {
        return checkExistence(userId);
    }

    @Override
    public List<User> getUsersList() {
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User updateUser(Long userId, User updateUser) {
        checkExistence(userId);
        User oldUser = checkExistence(userId);

        if (!oldUser.getEmail().equals(updateUser.getEmail()) && updateUser.getEmail() != null) {
            validationEmail(updateUser.getEmail());
            userEmail.remove(oldUser.getEmail());
            oldUser = oldUser.toBuilder()
                    .email(updateUser.getEmail())
                    .build();
        }
        if (updateUser.getName() != null && !updateUser.getName().isBlank()) {
            oldUser = oldUser.toBuilder()
                    .name(updateUser.getName())
                    .build();
        }
        usersMap.put(updateUser.getId(), oldUser);
        return oldUser;
    }

    @Override
    public void removeUserById(Long userId) {
        userEmail.remove(usersMap.remove(userId).getEmail());
    }

    private void validationEmail(String email) {
        if (!userEmail.add(email)) {
            throw new DuplicateEmailException("Пользователь с такой почтой уже существует");
        }
    }

    private User checkExistence(Long userId) {
        Optional<User> user = Optional.ofNullable(usersMap.get(userId));
        return user.orElseThrow(() -> new UserNotExistException("Пользователя с id = " + userId + " не существует!"));
    }
}
