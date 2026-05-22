package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.Role;
import andrey.project.warehouse.entity.User;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.repository.RoleRepository;
import andrey.project.warehouse.repository.UserRepository;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(String login, String rawPassword, String name, String roleName) {
        validateLogin(login);
        validatePassword(rawPassword);
        validateName(name);
        validateRoleName(roleName);

        if (userRepository.existsByLogin(login)) {
            throw new ValidationException("Логин пользователя должен быть уникальным.");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Роль с таким названием не найдена."));

        User user = new User();
        user.setLogin(login);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setName(name);
        user.setRole(role);

        return userRepository.saveAndFlush(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAllByOrderByLoginAsc();
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAllByOrderByNameAsc();
    }

    private void validateLogin(String login) {
        if (login == null || login.isBlank()) {
            throw new ValidationException("Логин пользователя не должен быть пустым.");
        }
    }

    private void validatePassword(String rawPassword) {
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ValidationException("Пароль пользователя не должен быть пустым.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Имя пользователя не должно быть пустым.");
        }
    }

    private void validateRoleName(String roleName) {
        if (roleName == null || roleName.isBlank()) {
            throw new ValidationException("Роль пользователя не должна быть пустой.");
        }
    }
}
