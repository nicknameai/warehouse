package andrey.project.warehouse.console;

import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class MainMenu {

    private final ProductConsoleController productConsoleController;
    private final SupplierConsoleController supplierConsoleController;
    private final ClientConsoleController clientConsoleController;
    private final InvoiceConsoleController invoiceConsoleController;
    private final WarehouseStockConsoleController warehouseStockConsoleController;

    public MainMenu(ProductConsoleController productConsoleController,
                    SupplierConsoleController supplierConsoleController,
                    ClientConsoleController clientConsoleController,
                    InvoiceConsoleController invoiceConsoleController,
                    WarehouseStockConsoleController warehouseStockConsoleController) {
        this.productConsoleController = productConsoleController;
        this.supplierConsoleController = supplierConsoleController;
        this.clientConsoleController = clientConsoleController;
        this.invoiceConsoleController = invoiceConsoleController;
        this.warehouseStockConsoleController = warehouseStockConsoleController;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String command = scanner.nextLine().trim();

            switch (command) {
                case "1" -> productConsoleController.handleAddProduct(scanner);
                case "2" -> productConsoleController.handleFindProductById(scanner);
                case "3" -> productConsoleController.handleFindProductByName(scanner);
                case "4" -> productConsoleController.handleFindProductByArticle(scanner);
                case "5" -> supplierConsoleController.handleAddSupplier(scanner);
                case "6" -> supplierConsoleController.handleFindSupplierById(scanner);
                case "7" -> supplierConsoleController.handleFindSupplierByName(scanner);
                case "8" -> clientConsoleController.handleAddClient(scanner);
                case "9" -> clientConsoleController.handleFindClientById(scanner);
                case "10" -> clientConsoleController.handleFindClientByName(scanner);
                case "11" -> invoiceConsoleController.handleCreateIncomingInvoice(scanner);
                case "12" -> invoiceConsoleController.handleCreateOutgoingInvoice(scanner);
                case "13" -> invoiceConsoleController.handleFindInvoiceById(scanner);
                case "14" -> warehouseStockConsoleController.handleShowAll();
                case "15" -> warehouseStockConsoleController.handleFindByProductId(scanner);
                default -> System.out.println("Unknown command. Please enter 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 or 15.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("Main menu:");
        System.out.println("1 - Add product");
        System.out.println("2 - Find product by id");
        System.out.println("3 - Find product by name");
        System.out.println("4 - Find product by article");
        System.out.println("5 - Add supplier");
        System.out.println("6 - Find supplier by id");
        System.out.println("7 - Find supplier by name");
        System.out.println("8 - Add client");
        System.out.println("9 - Find client by id");
        System.out.println("10 - Find client by name");
        System.out.println("11 - Create incoming invoice");
        System.out.println("12 - Create outgoing invoice");
        System.out.println("13 - Find invoice by id");
        System.out.println("14 - Show all warehouse stock");
        System.out.println("15 - Find warehouse stock by product id");
        System.out.print("Enter command: ");
    }
}
