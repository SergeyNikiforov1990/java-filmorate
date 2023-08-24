package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @EqualsAndHashCode.Exclude
    private Integer id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    @NonNull
    private String name;
    @NonNull
    private LocalDate birthday;
    private Set<Integer> friendList = new HashSet<>();

    public User(@NonNull String email, @NonNull String login, @NonNull String name, @NonNull LocalDate dateOfBirth) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = dateOfBirth;
    }

    public Set<Integer> getFriendList() {
        if (friendList == null) {
            throw new DataNotFoundException("friendList пользователя пуст"); // надо ли?
        }
        return friendList;
    }


}