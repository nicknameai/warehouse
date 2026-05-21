package andrey.project.warehouse.repository;

import andrey.project.warehouse.entity.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    List<Role> findAllByOrderByNameAsc();
}
