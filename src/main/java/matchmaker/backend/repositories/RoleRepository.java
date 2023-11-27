package matchmaker.backend.repositories;

import matchmaker.backend.models.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
  Optional<Role> findById(Long id);
}
