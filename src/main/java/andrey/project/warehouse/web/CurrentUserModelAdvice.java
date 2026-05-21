package andrey.project.warehouse.web;

import andrey.project.warehouse.entity.User;
import andrey.project.warehouse.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserModelAdvice {

    private final UserRepository userRepository;

    public CurrentUserModelAdvice(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("currentUserName")
    public String currentUserName(Authentication authentication) {
        User user = resolveUser(authentication);
        return user != null ? user.getName() : null;
    }

    @ModelAttribute("currentUserRole")
    public String currentUserRole(Authentication authentication) {
        User user = resolveUser(authentication);
        return user != null && user.getRole() != null ? user.getRole().getName() : null;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        User user = resolveUser(authentication);
        return user != null && user.getRole() != null && "ADMIN".equals(user.getRole().getName());
    }

    private User resolveUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        return userRepository.findByLogin(authentication.getName()).orElse(null);
    }
}
