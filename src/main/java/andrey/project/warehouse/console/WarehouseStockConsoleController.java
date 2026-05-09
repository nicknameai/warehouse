package andrey.project.warehouse.console;

import andrey.project.warehouse.entity.WarehouseStock;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.WarehouseStockService;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class WarehouseStockConsoleController {

    private final WarehouseStockService warehouseStockService;

    public WarehouseStockConsoleController(WarehouseStockService warehouseStockService) {
        this.warehouseStockService = warehouseStockService;
    }

    public void handleFindByProductId(Scanner scanner) {
        try {
            System.out.print("Enter product id: ");
            Integer productId = readInteger(scanner);
            System.out.println(warehouseStockService.findByProductId(productId));
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleShowAll() {
        List<WarehouseStock> items = warehouseStockService.findAll();
        if (items.isEmpty()) {
            System.out.println("Warehouse is empty.");
            return;
        }

        for (WarehouseStock item : items) {
            System.out.println(item);
        }
    }

    private Integer readInteger(Scanner scanner) {
        String value = scanner.nextLine().trim();

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Product id must be a valid integer.");
        }
    }
}
