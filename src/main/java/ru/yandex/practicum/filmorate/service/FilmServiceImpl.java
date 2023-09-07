package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmlikes.FilmLikeStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmServiceImpl implements FilmService {
    private final ValidationFilm validationFilm = new ValidationFilm();
    private final FilmStorage filmDbStorage;
    private final UserStorage userDbStorage;
    private final GenreStorage genreDbStorage;
    private final FilmLikeStorage filmLikeDbStorage;
    private final RatingMpaStorage ratingMpaDbStorage;

    @Override
    public Film getFilm(int id) {
        Film film = filmDbStorage.getFilm(id)
                .orElseThrow(() -> new DataNotFoundException("Фильм с указанным id не найден: " + id));
        LinkedHashSet<Genre> genres = genreDbStorage.getGenresByFilmId(film.getId());
        film.setGenres(genres);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = filmDbStorage.getAllFilms();
        genreDbStorage.load(films);
        films.sort(Comparator.comparing(Film::getId));
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        validationFilm.validationForAdd(film);
        Film addedFilm = filmDbStorage.addFilm(film);
        genreDbStorage.saveGenres(addedFilm);
        return addedFilm;
    }

    @Override
    public void updateFilm(Film updatedFilm) {
        validateFilmId(updatedFilm.getId());
        validationFilm.validationForAdd(updatedFilm);
        Film film = filmDbStorage.updateFilm(updatedFilm);
        try {
            genreDbStorage.saveGenres(film);
        } catch (DataNotFoundException e) {
            log.error("не удалось получить жанры и рейтинги для фильма с id: " + film.getId(), e);
            throw new DataNotFoundException("не удалось получить жанры и рейтинги для фильма с id: " + film.getId());
        }
        log.info("Фильм обновлен: " + film);
    }

    @Override
    public Film deleteFilm(int filmId) {
        validateFilmId(filmId);
        filmDbStorage.deleteFilm(filmId);
        return null;
    }

    @Override
    public List<Film> getListBestMovies(int count) {
        List<Film> films = filmDbStorage.getBestFilms(count);
        genreDbStorage.load(films);
        return films;
    }

    @Override
    public void addLikeFilm(int filmId, int userId) {
        validateFilmIdAndUserId(filmId, userId);
        filmLikeDbStorage.addLikeToFilm(filmId, userId);
    }

    @Override
    public void deleteLikeFilm(int filmId, int userId) {
        validateFilmIdAndUserId(filmId, userId);
        filmLikeDbStorage.deleteLike(filmId, userId);
    }

    @Override
    public Set<Integer> getFilmLikes(int filmId) {
        validateFilmId(filmId);
        return filmLikeDbStorage.getFilmLikes(filmId);
    }

    @Override
    public Genre getGenreById(int genreId) {
        return genreDbStorage.getGenreById(genreId).orElseThrow(() -> new DataNotFoundException("Жанр с " +
                "указанным id:" + genreId + " не найден"));
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDbStorage.getAllGenres();
    }

    @Override
    public LinkedHashSet<Genre> getGenresByFilmId(int filmId) {
        return genreDbStorage.getGenresByFilmId(filmId);
    }

    @Override
    public void load(List<Film> films) {
        genreDbStorage.load(films);
    }

    @Override
    public void saveGenres(Film film) {
        genreDbStorage.saveGenres(film);
    }

    @Override
    public RatingMPA getRatingMpaById(int ratingId) {
        return ratingMpaDbStorage.getRatingMpaById(ratingId).orElseThrow(() -> new DataNotFoundException("Рейтинг MPA с " +
                "указанным ID не найден: " + ratingId));
    }

    @Override
    public List<RatingMPA> getAllRatings() {
        return ratingMpaDbStorage.getAllRatings();
    }

    public void validateFilmId(int filmId) {
        if (filmId <= 0 || !filmDbStorage.filmExists(filmId)) {
            log.error("Фильм с id: " + filmId + " не найден");
            throw new DataNotFoundException("Film with id " + filmId + " not found");
        }
    }

    private void validateUserId(int userId) {
        if (userId <=0 || !userDbStorage.userExists(userId)) {
            log.warn("Пользователь с id: " + userId + " не найден");
            throw new DataNotFoundException("Пользователь с id " + userId + " не найден.");
        }
    }

    private void validateFilmIdAndUserId(int filmId, int userId) {
        validateFilmId(filmId);
        validateUserId(userId);
    }
}
