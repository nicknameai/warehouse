package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.User;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserChecker {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserChecker(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByLogin(String login) {
        validateLogin(login);
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким логином не найден."));
    }

    public boolean existsByLogin(String login) {
        validateLogin(login);
        return userRepository.existsByLogin(login);
    }

    @Transactional
    public User changeName(String login, String name) {
        validateLogin(login);
        validateName(name);

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким логином не найден."));
        user.setName(name);
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public User changePassword(String login, String rawPassword) {
        validateLogin(login);
        validatePassword(rawPassword);

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким логином не найден."));
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return userRepository.saveAndFlush(user);
    }

    private void validateLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new ValidationException("Логин пользователя не должен быть пустым.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Имя пользователя не должно быть пустым.");
        }
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ValidationException("Пароль пользователя не должен быть пустым.");
        }
    }
}
