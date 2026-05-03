package andrey.project.warehouse.console;

import andrey.project.warehouse.entity.Category;
import andrey.project.warehouse.service.CategoryService;
import java.util.Scanner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Component
public class CategoryConsoleController {

    private final CategoryService categoryService;

    public CategoryConsoleController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void handleAddCategory(Scanner scanner) {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter category description: ");
        String description = scanner.nextLine().trim();

        Category category = categoryService.addEntity(name, description);
        System.out.println("Category saved with id: " + category.getId());
    }

    public void handleRemoveCategory(Scanner scanner) {
        try {
            System.out.print("Enter category id to remove: ");
            Long id = readLong(scanner);
            categoryService.removeEntity(id);
            System.out.println("Category removed.");
        } catch (EmptyResultDataAccessException ex) {
            System.out.println("Error: category with this id was not found.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private Long readLong(Scanner scanner) {
        String value = scanner.nextLine().trim();

        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Id must be a valid number.");
        }
    }
}
