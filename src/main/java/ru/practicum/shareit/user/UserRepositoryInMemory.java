package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.MyNotFoundException;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryInMemory implements UserRepository {

    private final Map<Long, User> users;
    private long id = 0L;

    @Override
    public User create(User user) {
        id += 1L;
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return users.values().stream().toList();
    }

    @Override
    public User get(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        log.warn("Пользователя с id={} не существует в памяти приложения для получения.", id);
        throw new MyNotFoundException("Пользователь с id " + id + " не найден");
    }

    @Override
    public List<User> getByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .toList();
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }
}