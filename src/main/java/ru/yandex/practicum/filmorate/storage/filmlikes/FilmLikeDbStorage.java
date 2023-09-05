package ru.yandex.practicum.filmorate.storage.filmlikes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmLikeDbStorage implements FilmLikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void deleteLike(int filmId, int userId) {
        String sql = FilmLikeSQLQueries.DELETE_FILM_USER_LIKES;
        jdbcTemplate.update(sql, filmId, userId);
        log.info("Пользователь с id: " + userId + " убрал лайк фильму с id: " + filmId);

    }

    @Override
    public Set<Integer> getFilmLikes(int filmId) {
        String sql = FilmLikeSQLQueries.SELECT_FILM_LIKES;
        log.info("Получение лайков к фильму с id: " + filmId);
        List<Integer> likes = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), filmId);
        Set<Integer> likesSet = new HashSet<>(likes);
        log.info("Получено " + likesSet.size() + " лайков к фильму с id " + filmId);
        return null;
    }

    @Override
    public void addLikeToFilm(int filmId, int userId) {
        String sql = FilmLikeSQLQueries.INSERT_FILM_USER_LIKES;
        jdbcTemplate.update(sql, filmId, userId);
        log.info("Пользователь с id: " + userId + " поставил лайк фильму с id: " + filmId);
    }
}
