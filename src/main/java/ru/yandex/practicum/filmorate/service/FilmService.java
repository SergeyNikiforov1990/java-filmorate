package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public interface FilmService {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    void updateFilm(Film film);

    Film getFilm(int id);

    List<Film> getListBestMovies(int count);

    Film deleteFilm(int id);

    void addLikeFilm(int id, int userId);

    void deleteLikeFilm(int id, int userId);

    public Set<Integer> getFilmLikes(int filmId);

    public Genre getGenreById(int genreId);

    public List<Genre> getAllGenres();

    public LinkedHashSet<Genre> getGenresByFilmId(int filmId);

    public void load(List<Film> films);

    public void saveGenres(Film film);

    public RatingMPA getRatingMpaById(int ratingId);

    public List<RatingMPA> getAllRatings();
}
