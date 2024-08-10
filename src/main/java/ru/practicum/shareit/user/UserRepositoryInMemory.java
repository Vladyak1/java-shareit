package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.MyNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class UserRepositoryInMemory {

    private final Map<Long, User> users;
    private long id = 0L;

    public User create(User user) {
        id += 1L;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public List<User> getAll() {
        return users.values().stream().toList();
    }

    public User get(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        log.warn("Пользователя с id={} не существует в памяти приложения для получения.", id);
        throw new MyNotFoundException("Пользователь с id " + id + " не найден");
    }

    public List<User> getByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .toList();
    }

    public void delete(long id) {
        users.remove(id);
    }
}