package andrey.project.warehouse.console;

import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.ProductService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Component
public class ProductConsoleController {

    private final ProductService productService;

    public ProductConsoleController(ProductService productService) {
        this.productService = productService;
    }

    public void handleAddProduct(Scanner scanner) {
        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter article: ");
            String article = scanner.nextLine().trim();

            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Enter price: ");
            BigDecimal price = readBigDecimal(scanner);

            System.out.print("Enter quantity: ");
            Integer quantity = readInteger(scanner);

            System.out.print("Enter unit: ");
            String unit = scanner.nextLine().trim();

            LocalDateTime now = LocalDateTime.now();
            Product product = productService.addEntity(
                    name,
                    article,
                    description,
                    price,
                    quantity,
                    unit,
                    now,
                    now
            );

            System.out.println("Product saved with id: " + product.getId());
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleRemoveProduct(Scanner scanner) {
        try {
            System.out.print("Enter product id to remove: ");
            Long id = readLong(scanner);
            productService.removeEntity(id);
            System.out.println("Product removed.");
        } catch (EmptyResultDataAccessException ex) {
            System.out.println("Error: product with this id was not found.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private BigDecimal readBigDecimal(Scanner scanner) {
        String value = scanner.nextLine().trim();

        try {
            return new BigDecimal(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Price must be a valid number.");
        }
    }

    private Integer readInteger(Scanner scanner) {
        String value = scanner.nextLine().trim();

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Quantity must be a valid integer.");
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
