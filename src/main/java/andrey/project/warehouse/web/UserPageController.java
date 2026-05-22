package andrey.project.warehouse.web;

import andrey.project.warehouse.entity.User;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.UserManagementService;
import andrey.project.warehouse.web.form.UserCreateForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserPageController {

    private final UserManagementService userManagementService;

    public UserPageController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @GetMapping
    public String showPage(Model model) {
        preparePage(model, new UserCreateForm());
        return "users";
    }

    @PostMapping
    public String createUser(@ModelAttribute("userForm") UserCreateForm form, Model model) {
        preparePage(model, form);

        try {
            User user = userManagementService.createUser(
                    form.getLogin(),
                    form.getPassword(),
                    form.getName(),
                    form.getRoleName()
            );
            model.addAttribute("successMessage", "Пользователь создан: " + user.getLogin());
            model.addAttribute("userForm", new UserCreateForm());
        } catch (ValidationException | IllegalArgumentException ex) {
            model.addAttribute("formError", ex.getMessage());
        }

        return "users";
    }

    private void preparePage(Model model, UserCreateForm form) {
        model.addAttribute("pageTitle", "Пользователи");
        model.addAttribute("activePage", "users");
        model.addAttribute("userForm", form);
        model.addAttribute("users", userManagementService.findAllUsers());
        model.addAttribute("roles", userManagementService.findAllRoles());
    }
}
