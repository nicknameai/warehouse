package andrey.project.warehouse.console;

import andrey.project.warehouse.entity.Client;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.service.ClientService;
import java.util.Scanner;
import org.springframework.stereotype.Component;

@Component
public class ClientConsoleController {

    private final ClientService clientService;

    public ClientConsoleController(ClientService clientService) {
        this.clientService = clientService;
    }

    public void handleAddClient(Scanner scanner) {
        try {
            System.out.print("Enter client name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter client phone: ");
            String phone = scanner.nextLine().trim();

            System.out.print("Enter client email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter client address: ");
            String address = scanner.nextLine().trim();

            System.out.print("Enter client description: ");
            String description = scanner.nextLine().trim();

            Client client = clientService.addEntity(name, phone, email, address, description);
            System.out.println(client);
        } catch (ValidationException | IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleFindClientById(Scanner scanner) {
        try {
            System.out.print("Enter client id: ");
            Integer id = readInteger(scanner);
            System.out.println(clientService.findById(id));
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    public void handleFindClientByName(Scanner scanner) {
        try {
            System.out.print("Enter client name: ");
            String name = scanner.nextLine().trim();
            System.out.println(clientService.findByName(name));
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
