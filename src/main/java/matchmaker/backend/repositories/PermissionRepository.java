package matchmaker.backend.repositories;

import matchmaker.backend.models.Permission;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PermissionRepository extends CrudRepository<Permission, Long> {
  Optional<Permission> findById(Long id);

  Optional<Permission> findByCodeName(String code);
}
