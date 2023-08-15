package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private InMemoryUserStorage inMemoryUserStorage;

    @Override
    public List<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return inMemoryUserStorage.users.get(id);
    }

    @Override
    public User addUser(User user) {
        return inMemoryUserStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return inMemoryUserStorage.updateUser(user);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        log.info("userId " + userId + " friendId " + friendId);
        User user = inMemoryUserStorage.users.get(userId);
        User friendsUser = inMemoryUserStorage.users.get(friendId);
        user.addFriend(friendId);
        friendsUser.addFriend(userId);
        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friendsUser);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        log.info("userId " + userId + " friendId " + friendId);
        User user = inMemoryUserStorage.users.get(userId);
        User friendsUser = inMemoryUserStorage.users.get(friendId);
        user.deleteFriend(friendId);
        friendsUser.deleteFriend(userId);
        inMemoryUserStorage.updateUser(user);
        inMemoryUserStorage.updateUser(friendsUser);
    }

    @Override
    public List<User> getUserFriends(int userId) {
        User user = inMemoryUserStorage.users.get(userId);
        List<User> friends = new ArrayList<>();
        for (Integer id : user.getFriendList()) {
            friends.add(inMemoryUserStorage.users.get(id));
        }
        return friends;
    }

    @Override
    public List<User> getListCommonFriends(int userId, int otherId) {
        User user = inMemoryUserStorage.users.get(userId);
        User user1 = inMemoryUserStorage.users.get(otherId);
        List<Integer> friends = new ArrayList<>(user.getFriendList());
        List<Integer> friends1 = new ArrayList<>(user1.getFriendList());
        List<User> commonListFriends = new ArrayList<>();
        for (int i = 0; i < friends1.size(); i++) {
            for (int j = 0; j < friends.size(); j++) {
                if (friends1.get(i) == friends.get(j)) {
                    commonListFriends.add(inMemoryUserStorage.users.get(friends1.get(i)));
                    break;
                }
            }
        }
        return commonListFriends;
    }
}
