package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.Client;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.repository.ClientRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client addEntity(String name, String phone, String email, String address, String description) {
        validateClient(name, phone, email, address, description);
        validateUniqueness(name);

        Client client = new Client();
        client.setName(name);
        client.setPhone(phone);
        client.setEmail(email);
        client.setAddress(address);
        client.setDescription(description);

        return clientRepository.saveAndFlush(client);
    }

    public Client findById(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client with this id was not found."));
    }

    public Client findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Client name must not be empty");
        }

        return clientRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Client with this name was not found."));
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    private void validateClient(String name, String phone, String email, String address, String description) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Client name must not be empty");
        }

        if (phone == null || phone.isBlank()) {
            throw new ValidationException("Client phone must not be empty");
        }

        if (email == null || email.isBlank()) {
            throw new ValidationException("Client email must not be empty");
        }

        if (address == null || address.isBlank()) {
            throw new ValidationException("Client address must not be empty");
        }

        if (description == null || description.isBlank()) {
            throw new ValidationException("Client description must not be empty");
        }
    }

    private void validateUniqueness(String name) {
        if (clientRepository.existsByName(name)) {
            throw new ValidationException("Client name must be unique");
        }
    }
}
