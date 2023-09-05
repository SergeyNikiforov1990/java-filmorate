package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
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

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Set<Integer> getUserLikesFilm() {
        if (userLikesFilm == null) {
            throw new DataNotFoundException("userLikesFilm пуст"); // надо ли?
        }
        return userLikesFilm;
    }
}