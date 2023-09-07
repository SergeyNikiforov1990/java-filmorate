package ru.yandex.practicum.filmorate.storage.filmlikes;

import java.util.Set;

public interface FilmLikeStorage {
    void deleteLike(int filmId, int userId);

    Set<Integer> getFilmLikes(int filmId);

    void addLikeToFilm(int filmId, int userId);
}
