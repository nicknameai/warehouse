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
                .orElseThrow(() -> new IllegalArgumentException("User with this login was not found."));
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
                .orElseThrow(() -> new IllegalArgumentException("User with this login was not found."));
        user.setName(name);
        return userRepository.saveAndFlush(user);
    }

    @Transactional
    public User changePassword(String login, String rawPassword) {
        validateLogin(login);
        validatePassword(rawPassword);

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User with this login was not found."));
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        return userRepository.saveAndFlush(user);
    }

    private void validateLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new ValidationException("User login must not be empty.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("User name must not be empty.");
        }
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ValidationException("User password must not be empty.");
        }
    }
}
