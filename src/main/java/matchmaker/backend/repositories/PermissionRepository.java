package matchmaker.backend.repositories;

import matchmaker.backend.models.Challenge;
import matchmaker.backend.models.Permission;
import matchmaker.backend.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends CrudRepository<Permission, Long> {
    Optional<Permission> findById(Long id);
}
