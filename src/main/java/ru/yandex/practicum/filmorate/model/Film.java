package ru.yandex.practicum.filmorate.model;

import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film implements Comparable<Film> {
    @EqualsAndHashCode.Exclude
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private int duration;
    private Set<Integer> userLikesFilm = new HashSet<>();
    private int likes = 0;

    public void setUsersLikeMovie(Set<Integer> userLikesFilm) {
        this.setLikes(userLikesFilm.size());
        this.userLikesFilm = userLikesFilm;
    }

    public void addLikeFilm(int id) {
        userLikesFilm.add(id);
        setLikes(getLikes() + 1);
    }

    public void deleteLikeFilm(int id) {
        userLikesFilm.remove(id);
        setLikes(getLikes() - 1);
    }

    @Override
    public int compareTo(Film film) {
        return Integer.compare(film.getLikes(), this.likes);
    }

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}