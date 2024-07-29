package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {

    User create(User user);

    User update(User newUser);

    List<User> getAll();

    User get(long id);

    List<User> getByEmail(String email);

    void delete(long id);
}