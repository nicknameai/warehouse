package andrey.project.warehouse.console;

import andrey.project.warehouse.contract.invoice.CreateIncomingInvoiceCommand;
import andrey.project.warehouse.contract.invoice.CreateOutgoingInvoiceCommand;
import andrey.project.warehouse.contract.invoice.InvoiceItemCommand;
import andrey.project.warehouse.contract.invoice.InvoiceResult;
import andrey.project.warehouse.exception.NotEnoughStockException;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.InvoiceService;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class InvoiceConsoleController {

    private final InvoiceService invoiceService;

    public InvoiceConsoleController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void handleCreateIncomingInvoice(Scanner scanner) {
        try {
            CreateIncomingInvoiceCommand command = new CreateIncomingInvoiceCommand();
            command.setSupplierId(readInteger(scanner, "Enter supplier id: "));
            command.setComment(readString(scanner, "Enter comment: "));
            command.setItems(readItems(scanner));

            InvoiceResult result = invoiceService.createIncomingInvoice(command);
            System.out.println("Incoming invoice saved and warehouse stock updated.");
            System.out.println(result);
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleCreateOutgoingInvoice(Scanner scanner) {
        try {
            CreateOutgoingInvoiceCommand command = new CreateOutgoingInvoiceCommand();
            command.setClientId(readInteger(scanner, "Enter client id: "));
            command.setComment(readString(scanner, "Enter comment: "));
            command.setItems(readItems(scanner));

            InvoiceResult result = invoiceService.createOutgoingInvoice(command);
            System.out.println("Outgoing invoice saved and warehouse stock updated.");
            System.out.println(result);
        } catch (NotEnoughStockException ex) {
            System.out.println("Not enough stock: " + ex.getMessage());
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleFindInvoiceById(Scanner scanner) {
        try {
            Integer id = readInteger(scanner, "Enter invoice id: ");
            System.out.println(invoiceService.findById(id));
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private List<InvoiceItemCommand> readItems(Scanner scanner) {
        Integer count = readInteger(scanner, "Enter number of items: ");
        if (count <= 0) {
            throw new IllegalArgumentException("Number of items must be greater than 0.");
        }

        List<InvoiceItemCommand> items = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            System.out.println("Item " + i + ":");
            InvoiceItemCommand item = new InvoiceItemCommand();
            item.setProductId(readInteger(scanner, "Enter product id: "));
            item.setQuantity(readInteger(scanner, "Enter quantity: "));
            item.setPrice(readInteger(scanner, "Enter price: "));
            items.add(item);
        }

        return items;
    }

    private Integer readInteger(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String value = scanner.nextLine().trim();

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Value must be a valid integer.");
        }
    }

    private String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
