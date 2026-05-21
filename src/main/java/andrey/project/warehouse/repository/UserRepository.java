package andrey.project.warehouse.repository;

import andrey.project.warehouse.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByOrderByLoginAsc();

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);
}
