package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

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
    public Film deleteFilm(int id) {
        return filmStorage.deleteFilm(id);
    }

    @Override
    public void addLikeFilm(int id, int userId) {
        log.info("Пользователь с id " + userId + "поставил лайк фильму c id: " + id);
        Film film = filmStorage.getFilm(id);
        User user = userStorage.getUser(userId);
        film.getUserLikesFilm().add(user.getId());
    }

    @Override
    public void deleteLikeFilm(int id, int userId) {
        Film film = filmStorage.getFilm(id);
        if (!film.getUserLikesFilm().remove(userId)) {
            throw new DataNotFoundException("Лайк от пользователя с id: " + userId + " не найден");
        }
        log.info("Пользователь с id: " + userId + " убрал лайк фильму c id: " + id);
    }

    @Override
    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }

    @Override
    public List<Film> getListBestMovies(Integer count) {
        log.info("запрос на вывод лучших фильмов");
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getUserLikesFilm().size() - o1.getUserLikesFilm().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
