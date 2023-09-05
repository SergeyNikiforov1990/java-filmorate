package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {
    private final ValidationUser validationUser = new ValidationUser();
    private final UserStorage userDbStorage;
    private final FriendStorage friendDbStorage;

    @Override
    public User addUser(User user) {
        validationUser.validationForAdd(user);
        return userDbStorage.addUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    @Override
    public User getUser(int userId) {
        return userDbStorage.getUser(userId);
    }

    @Override
    public User updateUser(User user) {
        validationUser.validationId(user);
        validationUser.validationForAdd(user);
        return userDbStorage.updateUser(user);
    }

    @Override
    public void deleteUser(int id) {
        validateUserId(id);
        userDbStorage.deleteUser(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("Неверный запрос на добавление себя в список своих друзей");
        }
        validateUserId(userId);
        validateUserId(friendId);
        log.trace("Пользователь: " + userId + " добвил в список друзей пользователя: " + friendId);
        friendDbStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new ValidationException("Неверный запрос на удаление себя из списка своих друзей");
        }
        validateUserId(userId);
        validateUserId(friendId);
        friendDbStorage.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getUserFriends(int userId) {
        validateUserId(userId);
        return friendDbStorage.getFriends(userId);
    }

    @Override
    public List<User> getListCommonFriends(int userId, int otherId) {
        validateUserId(userId);
        validateUserId(otherId);
        return friendDbStorage.getCommonFriends(userId, otherId);
    }

    private void validateUserId(int userId) {
        if (!userDbStorage.userExists(userId)) {
            log.warn("Пользователь с id: " + userId + "не найден");
            throw new DataNotFoundException("Пользователь с id " + userId + " не найден.");
        }
    }
}
