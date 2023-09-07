package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    List<User> getAllUsers();

    User getUser(int id);

    User updateUser(User user);

    void deleteUser(int id);

    boolean userExists(int userId);
}
