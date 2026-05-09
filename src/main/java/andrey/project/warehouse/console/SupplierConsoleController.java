package andrey.project.warehouse.console;

import andrey.project.warehouse.entity.Supplier;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.SupplierService;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class SupplierConsoleController {

    private final SupplierService supplierService;

    public SupplierConsoleController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    public void handleAddSupplier(Scanner scanner) {
        try {
            System.out.print("Enter supplier name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter supplier phone: ");
            String phone = scanner.nextLine().trim();

            System.out.print("Enter supplier email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter supplier address: ");
            String address = scanner.nextLine().trim();

            System.out.print("Enter supplier description: ");
            String description = scanner.nextLine().trim();

            Supplier supplier = supplierService.addEntity(name, phone, email, address, description);
            System.out.println(supplier);
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleFindSupplierById(Scanner scanner) {
        try {
            System.out.print("Enter supplier id: ");
            Integer id = readInteger(scanner);
            System.out.println(supplierService.findById(id));
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleFindSupplierByName(Scanner scanner) {
        try {
            System.out.print("Enter supplier name: ");
            String name = scanner.nextLine().trim();
            System.out.println(supplierService.findByName(name));
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
