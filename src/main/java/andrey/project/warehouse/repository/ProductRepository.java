package andrey.project.warehouse.repository;

import andrey.project.warehouse.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Optional<Product> findByArticle(String article);

    boolean existsByName(String name);

    boolean existsByArticle(String article);
}
