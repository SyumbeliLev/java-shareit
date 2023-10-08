package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserNotExistException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> usersMap = new HashMap<>();
    private Long id = 0L;

    @Override
    public User addUser(User user) {
        user.setId(++id);
        usersMap.put(id, user);
        return usersMap.get(id);
    }

    @Override
    public User getUserById(Long userId) {
        checkExistence(userId);
        return usersMap.get(userId);
    }

    @Override
    public List<User> getUsersList() {
        return new ArrayList<>(usersMap.values());
    }

    @Override
    public User updateUser(Long userId, User updateUser) {
        checkExistence(userId);
        usersMap.put(userId, updateUser);
        return usersMap.get(userId);
    }

    @Override
    public void removeUserById(Long userId) {
        checkExistence(userId);
        usersMap.remove(userId);
    }

    private void checkExistence(Long userId) {
        if (!usersMap.containsKey(userId)) {
            throw new UserNotExistException("Пользователя с id = " + userId + " не существует!");
        }
    }
}
