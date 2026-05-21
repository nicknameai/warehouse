package andrey.project.warehouse.security;

import andrey.project.warehouse.entity.Role;
import andrey.project.warehouse.entity.User;
import andrey.project.warehouse.repository.RoleRepository;
import andrey.project.warehouse.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBootstrap {

    @Bean
    ApplicationRunner securityDataBootstrap(RoleRepository roleRepository,
                                            UserRepository userRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            Role adminRole = ensureRole(roleRepository, "ADMIN");
            ensureRole(roleRepository, "STOREKEEPER");

            if (!userRepository.existsByLogin("admin")) {
                User admin = new User();
                admin.setLogin("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin"));
                admin.setName("admin");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        };
    }

    private Role ensureRole(RoleRepository roleRepository, String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }
}
