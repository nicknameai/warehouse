package andrey.project.warehouse.console;

import andrey.project.warehouse.entity.Product;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.ProductService;
import java.util.Scanner;
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

            System.out.print("Enter purchase price: ");
            Integer purchasePrice = readInteger(scanner);

            System.out.print("Enter sale price: ");
            Integer salePrice = readInteger(scanner);

            System.out.print("Enter unit: ");
            String unit = scanner.nextLine().trim();

            Product product = productService.addEntity(
                    name,
                    article,
                    description,
                    purchasePrice,
                    salePrice,
                    unit
            );

            System.out.println(product);
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleFindProductById(Scanner scanner) {
        try {
            System.out.print("Enter product id: ");
            Integer id = readInteger(scanner);
            System.out.println(productService.findById(id));
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleFindProductByName(Scanner scanner) {
        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine().trim();
            System.out.println(productService.findByName(name));
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleFindProductByArticle(Scanner scanner) {
        try {
            System.out.print("Enter product article: ");
            String article = scanner.nextLine().trim();
            System.out.println(productService.findByArticle(article));
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private Integer readInteger(Scanner scanner) {
        String value = scanner.nextLine().trim();

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Id must be a valid number.");
        }
    }
}
