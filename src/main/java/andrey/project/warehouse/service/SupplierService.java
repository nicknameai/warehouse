package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.Supplier;
import andrey.project.warehouse.repository.SupplierRepository;
import org.springframework.stereotype.Service;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier addEntity(String name, String phone, String email, String address, String description) {
        Supplier supplier = new Supplier();
        supplier.setName(name);
        supplier.setPhone(phone);
        supplier.setEmail(email);
        supplier.setAddress(address);
        supplier.setDescription(description);

        return supplierRepository.saveAndFlush(supplier);
    }

    public void removeEntity(Long id) {
        supplierRepository.deleteById(id);
    }
}
