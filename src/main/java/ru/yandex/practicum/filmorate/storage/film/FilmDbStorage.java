package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        String sql = FilmSQLQueries.INSERT_FILM;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement prSt = connection.prepareStatement(
                            sql, new String[]{"film_id"});
                    prSt.setString(1, film.getName());
                    prSt.setString(2, film.getDescription());
                    prSt.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
                    prSt.setLong(4, film.getDuration());
                    prSt.setLong(5, film.getMpa().getId());
                    return prSt;
                }, keyHolder);


        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        log.info("Фильм создан: " + film);
        return film;
    }


    @Override
    public Optional<Film> getFilm(int id) {
        log.info("Получение фильма по id: " + id);
        String sql = FilmSQLQueries.SELECT_FILM_BY_ID;
        try {
            Film film = jdbcTemplate.queryForObject(sql, filmRowMapper(), id);
            log.info("Фильм с id " + id + " получен");
            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Фильм с id " + id + " не найден");
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = FilmSQLQueries.SELECT_ALL_FILMS;
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper());
        log.info("Получение списка всех фильмов");
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = FilmSQLQueries.UPDATE_FILM;
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    @Override
    public void deleteFilm(int id) {
        try {
            String sql = FilmSQLQueries.DELETE_FILM;
            jdbcTemplate.update(sql, id);
            log.info("фильм с id: " + id + " удален");
        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка при удалении фильма с id: " + id, e);
            throw new RuntimeException("Ошибка при удалении фильма с id: " + id, e);
        }
    }

    @Override
    public List<Film> getBestFilms(int count) {
        String sql = FilmSQLQueries.SELECT_BEST_FILMS;
        log.info("Получение списка " + count + " популярных фильмов");
        List<Film> films = jdbcTemplate.query(sql, filmRowMapper(), count);
        log.info("Получено " + films.size() + " популярных фильмов");
        return films;
    }

    @Override
    public boolean filmExists(int filmId) {
        String checkSql = FilmSQLQueries.FILM_EXISTS;
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, filmId);
        return count != null && count > 0;
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getInt("film_id"));
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getObject("release_date", LocalDate.class));
            film.setDuration(rs.getInt("duration"));
            film.setMpa(new RatingMPA(rs.getInt("rating_id")));

            RatingMPA ratingMPA = new RatingMPA(rs.getInt("rating_id"));
            ratingMPA.setName(rs.getString("rating_name"));
            film.setMpa(ratingMPA);

            return film;
        };
    }
}
