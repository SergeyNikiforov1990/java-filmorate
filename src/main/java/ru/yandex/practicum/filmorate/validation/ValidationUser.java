package ru.yandex.practicum.filmorate.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class ValidationUser {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public void validation(User user) throws ValidationException {
        char[] mail = user.getEmail().toCharArray();
        char[] login = user.getLogin().toCharArray();
        boolean validMail = false;
        boolean validLogin = false;
        for (char c : mail) {
            if (c == '@') {
                validMail = true;
                break;
            }
        }
        for (char c : login) {
            if (c == ' ') {
                validLogin = true;
                log.error("Ошибка в поле login  " + user);
                break;
            }
        }
        if (!validMail | user.getEmail().length() == 0) {
            log.info("Ошибка в поле email " + user);
            throw new ValidationException("Неверно введен email");
        } else if (validLogin) {
            throw new ValidationException("Неверно введен login");
        } else if (user.getLogin().length() == 0) {
            throw new ValidationException("login слишком короткий");
        }
        if (user.getName() == null) { //
            log.info("Ошибка в поле имени " + user);
            user.setName(user.getLogin());
        } else if (user.getName().length() == 0) {
            log.info("Ошибка в поле имени " + user);
            user.setName(user.getLogin());
        }
        LocalDate localDateNow = LocalDate.now();

        if (user.getBirthday().isAfter(localDateNow)) {
            log.error("Ошибка в поле dateOfBirth " + user);
            throw new ValidationException("Дата рождения должна быть ранее текущей даты");
        }
    }

    public void validationId(User user) {
        if (((user.getId()) == null) || (user.getId() < 1)) {
            log.error("Ошибка в поле ID " + user);
            throw new ValidationException("Ошибка валидации");
        }
    }
}
