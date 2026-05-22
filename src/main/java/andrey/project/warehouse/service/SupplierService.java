package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.Supplier;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.repository.SupplierRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier addEntity(String name, String phone, String email, String address, String description) {
        validateSupplier(name, phone, email, address, description);
        validateUniqueness(name);

        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setPhone(phone);
        supplier.setEmail(email);
        supplier.setAddress(address);
        supplier.setDescription(description);

        return supplierRepository.saveAndFlush(supplier);
    }

    public Supplier findById(Integer id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Поставщик с таким ID не найден."));
    }

    public Supplier findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название поставщика не должно быть пустым.");
        }

        return supplierRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Поставщик с таким названием не найден."));
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    private void validateSupplier(String name, String phone, String email, String address, String description) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Название поставщика не должно быть пустым.");
        }

        if (phone == null || phone.isBlank()) {
            throw new ValidationException("Телефон поставщика не должен быть пустым.");
        }

        if (email == null || email.isBlank()) {
            throw new ValidationException("Эл. почта поставщика не должна быть пустой.");
        }

        if (address == null || address.isBlank()) {
            throw new ValidationException("Адрес поставщика не должен быть пустым.");
        }

        if (description == null || description.isBlank()) {
            throw new ValidationException("Описание поставщика не должно быть пустым.");
        }
    }

    private void validateUniqueness(String name) {
        if (supplierRepository.existsByName(name)) {
            throw new ValidationException("Название поставщика должно быть уникальным.");
        }
    }
}
