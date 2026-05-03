package andrey.project.warehouse.console;

import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class MainMenu {

    private final ProductConsoleController productConsoleController;
    private final SupplierConsoleController supplierConsoleController;
    private final CategoryConsoleController categoryConsoleController;

    public MainMenu(ProductConsoleController productConsoleController,
                    SupplierConsoleController supplierConsoleController,
                    CategoryConsoleController categoryConsoleController) {
        this.productConsoleController = productConsoleController;
        this.supplierConsoleController = supplierConsoleController;
        this.categoryConsoleController = categoryConsoleController;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String command = scanner.nextLine().trim();

            switch (command) {
                case "1" -> productConsoleController.handleAddProduct(scanner);
                case "2" -> productConsoleController.handleRemoveProduct(scanner);
                case "3" -> supplierConsoleController.handleAddSupplier(scanner);
                case "4" -> supplierConsoleController.handleRemoveSupplier(scanner);
                case "5" -> categoryConsoleController.handleAddCategory(scanner);
                case "6" -> categoryConsoleController.handleRemoveCategory(scanner);
                default -> System.out.println("Unknown command. Please enter 1, 2, 3, 4, 5 or 6.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Main menu:");
        System.out.println("1 - Add product");
        System.out.println("2 - Remove product");
        System.out.println("3 - Add supplier");
        System.out.println("4 - Remove supplier");
        System.out.println("5 - Add category");
        System.out.println("6 - Remove category");
        System.out.print("Enter command: ");
    }
}
