package andrey.project.warehouse.console;

import andrey.project.warehouse.entity.Supplier;
import andrey.project.warehouse.service.SupplierService;
import java.util.Scanner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Component
public class SupplierConsoleController {

    private final SupplierService supplierService;

    public SupplierConsoleController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    public void handleAddSupplier(Scanner scanner) {
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
        System.out.println("Supplier saved with id: " + supplier.getId());
    }

    public void handleRemoveSupplier(Scanner scanner) {
        try {
            System.out.print("Enter supplier id to remove: ");
            Long id = readLong(scanner);
            supplierService.removeEntity(id);
            System.out.println("Supplier removed.");
        } catch (EmptyResultDataAccessException ex) {
            System.out.println("Error: supplier with this id was not found.");
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
