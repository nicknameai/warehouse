package andrey.project.warehouse.service;

import andrey.project.warehouse.entity.WarehouseStock;
import andrey.project.warehouse.exception.ValidationException;
import andrey.project.warehouse.repository.WarehouseStockRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WarehouseStockService {

    private final WarehouseStockRepository warehouseStockRepository;

    public WarehouseStockService(WarehouseStockRepository warehouseStockRepository) {
        this.warehouseStockRepository = warehouseStockRepository;
    }

    public WarehouseStock findByProductId(Integer productId) {
        if (productId == null || productId <= 0) {
            throw new ValidationException("Product id must be a positive number.");
        }

        return warehouseStockRepository.findByProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse stock for this product was not found."));
    }

    public List<WarehouseStock> findAll() {
        return warehouseStockRepository.findAll();
    }
}
