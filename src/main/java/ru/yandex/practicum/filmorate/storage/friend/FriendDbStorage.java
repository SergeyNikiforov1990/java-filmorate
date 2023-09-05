package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userRowMapper;

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = FriendSQLQueries.INSERT_FRIEND;
        jdbcTemplate.update(sql, userId, friendId, "CONFIRMED");
        log.info("Пользователь id: {}, добавлен в друзья пользователю id: {}", userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sqlRemoveFriendship = FriendSQLQueries.DELETE_FRIEND;
        jdbcTemplate.update(sqlRemoveFriendship, userId, friendId);
        log.info("Пользователь id: {}, удален из друзей пользователя id: {}", userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        String sql = FriendSQLQueries.SELECT_COMMON_FRIENDS;
        log.info("Получение общих друзей пользовтелей с id {} и {}", userId, otherId);
        return jdbcTemplate.query(sql, new Object[]{userId, otherId}, userRowMapper);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sqlGetFriends = FriendSQLQueries.SELECT_GET_FRIENDS;
        List<User> friends = jdbcTemplate.query(sqlGetFriends, new Object[]{userId}, userRowMapper);
        log.info("Получение списка друзей пользователя с id: {}", userId);
        return friends;
    }
}
