package andrey.project.warehouse.repository;

import andrey.project.warehouse.entity.WarehouseStock;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, Integer> {

    Optional<WarehouseStock> findByProductId(Integer productId);

    boolean existsByProductId(Integer productId);
}
