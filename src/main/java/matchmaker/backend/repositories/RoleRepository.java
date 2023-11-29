package matchmaker.backend.repositories;

import matchmaker.backend.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
  Optional<Role> findById(Long id);
  Iterable<Role> findAllByIsAssignable(boolean isAssignable);
}
