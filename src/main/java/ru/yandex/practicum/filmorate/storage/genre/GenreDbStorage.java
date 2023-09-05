package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper = createRowMapper();

    private RowMapper<Genre> createRowMapper() {
        return (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        };
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        String sql = GenreSQLQueries.SELECT_GENRE_BY_ID;
        try {
            Genre genre = jdbcTemplate.queryForObject(sql, createRowMapper(), genreId);
            log.info("Жанр под id: " + genreId);
            return Optional.of(genre);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Жанр под id " + genreId + " не найден");
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = GenreSQLQueries.SELECT_ALL_GENRES;
        log.info("Получение списка всех жанров");
        return jdbcTemplate.query(sqlQuery, createRowMapper());
    }

    @Override
    public LinkedHashSet<Genre> getGenresByFilmId(int filmId) {
        String sql = GenreSQLQueries.SELECT_GENRE_BY_FILM_ID;
        List<Genre> genres = jdbcTemplate.query(sql, createRowMapper(), filmId);
        log.info("Жанры для фильма под id: " + filmId + genres);
        return new LinkedHashSet<>(genres);
    }

    @Override
    public void load(List<Film> films) {
        if (films.isEmpty()) {
            // Если список films пуст, просто завершаем метод
            return;
        }
        log.info("Переданный в метод список films: {}", films);
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        List<Integer> filmIds = new ArrayList<>(filmById.keySet());

        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final String sqlQuery = String.format(GenreSQLQueries.SELECT_LOAD, inSql);

        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            if (film != null) {
                film.getGenres().add(genreRowMapper.mapRow(rs, rowNum));
            } else {
                log.warn("Фильм с id {} не найден в filmById", rs.getInt("film_id"));
            }
            return null;
        }, filmIds.toArray());

    }

    @Override
    public void saveGenres(Film film) {
        int filmId = film.getId();

        String deleteSql = GenreSQLQueries.DELETE_FILM_GENRES;
        jdbcTemplate.update(deleteSql, filmId);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String insertSql = GenreSQLQueries.INSERT_FILM_GENRES;
            List<Object[]> batchArgs = new ArrayList<>();

            for (Genre genre : film.getGenres()) {
                Object[] params = {filmId, genre.getId()};
                batchArgs.add(params);
            }

            jdbcTemplate.batchUpdate(insertSql, batchArgs);
        }
        log.info("Жанры фильма " + film.getGenres());
    }
}
