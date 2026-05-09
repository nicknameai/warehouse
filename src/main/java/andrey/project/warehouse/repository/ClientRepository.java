package andrey.project.warehouse.repository;

import andrey.project.warehouse.entity.Client;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

    Optional<Client> findByName(String name);

    boolean existsByName(String name);
}
