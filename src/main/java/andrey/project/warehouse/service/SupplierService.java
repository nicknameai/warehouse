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
                .orElseThrow(() -> new IllegalArgumentException("Supplier with this id was not found."));
    }

    public Supplier findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Supplier name must not be empty");
        }

        return supplierRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Supplier with this name was not found."));
    }

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    private void validateSupplier(String name, String phone, String email, String address, String description) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Supplier name must not be empty");
        }

        if (phone == null || phone.isBlank()) {
            throw new ValidationException("Supplier phone must not be empty");
        }

        if (email == null || email.isBlank()) {
            throw new ValidationException("Supplier email must not be empty");
        }

        if (address == null || address.isBlank()) {
            throw new ValidationException("Supplier address must not be empty");
        }

        if (description == null || description.isBlank()) {
            throw new ValidationException("Supplier description must not be empty");
        }
    }

    private void validateUniqueness(String name) {
        if (supplierRepository.existsByName(name)) {
            throw new ValidationException("Supplier name must be unique");
        }
    }
}
