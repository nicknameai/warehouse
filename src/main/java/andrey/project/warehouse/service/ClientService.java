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
                .orElseThrow(() -> new IllegalArgumentException("Клиент с таким ID не найден."));
    }

    public Client findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название клиента не должно быть пустым.");
        }

        return clientRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Клиент с таким названием не найден."));
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    private void validateClient(String name, String phone, String email, String address, String description) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название клиента не должно быть пустым.");
        }

        if (phone == null || phone.isBlank()) {
            throw new ValidationException("Телефон клиента не должен быть пустым.");
        }

        if (email == null || email.isBlank()) {
            throw new ValidationException("Эл. почта клиента не должна быть пустой.");
        }

        if (address == null || address.isBlank()) {
            throw new ValidationException("Адрес клиента не должен быть пустым.");
        }

        if (description == null || description.isBlank()) {
            throw new ValidationException("Описание клиента не должно быть пустым.");
        }
    }

    private void validateUniqueness(String name) {
        if (clientRepository.existsByName(name)) {
            throw new ValidationException("Название клиента должно быть уникальным.");
        }
    }
}
