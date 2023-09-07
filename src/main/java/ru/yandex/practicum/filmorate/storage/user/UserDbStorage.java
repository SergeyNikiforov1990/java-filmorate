package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserMapper;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        Map<String, Object> params = Map.of("login", user.getLogin(),
                "user_name", user.getName(),
                "email", user.getEmail(),
                "birthday", user.getBirthday());
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        return user;
    }

    @Override
    public User getUser(int id) {
        String sql = UserSQLQueries.SELECT_USER_BY_ID;
        List<User> users = jdbcTemplate.query(sql, userMapper, id);
        if (users.size() != 1) {
            throw new DataNotFoundException("Пользователь с таким id = " + id + " не найден");
        }
        return users.get(0);
    }

    @Override
    public List<User> getAllUsers() {
        String sql = UserSQLQueries.SELECT_ALL_USERS;
        ;
        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public User updateUser(User user) {
        String sql = UserSQLQueries.UPDATE_USER;
        int affectedRows = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        if (affectedRows == 0) {
            log.warn("Попытка обновить пользователя с id: " + user.getId() + " Пользователь не найден");
            throw new DataNotFoundException("Пользователь с указанным id: " + user.getId() + " не найден");
        } else {
            log.info("Пользователь обновлен: " + user);
            return user;
        }
    }

    @Override
    public void deleteUser(int id) {
        String sql = UserSQLQueries.DELETE_USER;
        int affectedRows = jdbcTemplate.update(sql, id);
        if (affectedRows == 0) {
            log.warn("Попытка удалить пользователя с id: {}. Пользователь не найден", id);
            throw new DataNotFoundException("Пользователь с указанным ID не найден: " + id);
        } else {
            log.info("Пользователь удален, id: {}", id);
        }
    }

    public boolean userExists(int userId) {
        String sql = UserSQLQueries.USER_EXISTS;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }
}
