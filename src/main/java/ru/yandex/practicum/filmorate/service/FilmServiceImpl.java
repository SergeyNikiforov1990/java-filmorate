package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final InMemoryFilmStorage filmStorage;

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public void addLikeFilm(int id, int userId) {
        Film film = filmStorage.films.get(id);
        film.addLikeFilm(userId);
        log.info("Пользователь с id " + userId + "поставил лайк фильму " + film  );
        filmStorage.updateFilm(film);
    }

    @Override
    public void deleteLikeFilm(int id, int userId) {
        Film film = filmStorage.films.get(id);
        film.deleteLikeFilm(userId);
        log.info("Пользователь с id " + userId + "убрал лайк с фильма " + film  );
        filmStorage.updateFilm(film);
    }

    @Override
    public Film getFilm(Integer id) {
        return filmStorage.films.get(id);
    }

    @Override
    public List<Film> getListBestMovies(Integer count) {
        if (count == null) {
            return getListBestTenMovies();
        }
        log.info("count" + count);
        log.info("запрос на вывод списка лучших фильмов");
        return filmStorage.films.values().stream().sorted().limit(count).collect(Collectors.toList());
    }

    @Override
    public List<Film> getListBestTenMovies() {
        log.info("запрос на вывод списка 10 лучших фильмов");
        return filmStorage.films.values().stream().sorted().limit(10).collect(Collectors.toList());
    }
}
