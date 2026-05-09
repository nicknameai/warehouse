package andrey.project.warehouse.repository;

import andrey.project.warehouse.entity.Supplier;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    Optional<Supplier> findByName(String name);

    boolean existsByName(String name);
}
